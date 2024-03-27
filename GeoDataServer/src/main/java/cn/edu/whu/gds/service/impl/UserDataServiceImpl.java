package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.TiffMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.mapper.UserDataMapper;
import cn.edu.whu.gds.service.UserDataService;
import cn.edu.whu.gds.util.Bucket;
import cn.edu.whu.gds.util.Convert2COGUtil;
import cn.edu.whu.gds.util.HttpResponseUtil;
import cn.edu.whu.gds.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.gdal.gdal.gdal;


import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {
    private final static Bucket BUCKET = Bucket.TILES;
//    private static final String COG  = System.getProperty("../../");  // media/whu/New_Y/mnt
    private static final String COG  = System.getProperty("media/whu/New_Y/mnt");  // media/whu/New_Y/mnt
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private UserDataMapper userDataMapper;

    protected Boolean suffixCheck(String suffix, String... suffixes) {
        for (String s : suffixes) {
            if (suffix.equals(s)) {
                return true;
            }
        }
        return false;
    }
    protected String getBbox(Dataset dataset){
        double x_size = dataset.getRasterXSize();
        double y_size = dataset.getRasterYSize();
        double[] gt = (dataset.GetGeoTransform());
        double min_x = gt[0];
        double max_y = gt[3];
        double max_x = gt[0] + x_size * gt[1];
        double min_y = gt[3] + y_size * gt[5];
        String polygonStr = String.format("POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                min_x, min_y, max_x, min_y, max_x, max_y, min_x, max_y, min_x, min_y);
        return polygonStr;
    }

    @Override
    public Response addUserTiff(List<MultipartFile> files) throws IOException {
        Map<Integer, List<TiffMetadata>> tiffMetadata = new HashMap<>();
        Integer count = 1;
        // 文件大小检查
        for (MultipartFile file : files) {
            if (file.getSize() > 2147483648L) {
                return httpResponseUtil.payloadTooLarge("文件过大");
            }
            // 文件格式检查
            String suffix = Objects.requireNonNull(file.getOriginalFilename()).
                    substring(file.getOriginalFilename().indexOf(".") + 1);
            if (!suffixCheck(suffix, "tif", "tiff", "TIF", "TIFF")) {
                return httpResponseUtil.failure("保存文件非GeoTiff");
            }

            // 默认定位到当前应用根路径
            String tmpDir = System.getProperty("user.dir");
            // 影像名
            String tiffOriginalName = file.getOriginalFilename();
            String tiffPath = tmpDir + "/" + tiffOriginalName;
            // 影像暂时落地
            File tiffFile = new File(tiffPath);
            tiffFile.createNewFile();
            file.transferTo(tiffFile);
            // 读取影像元数据
            gdal.AllRegister();
            Dataset dataset = gdal.Open(tiffFile.getPath());
            String bbox = getBbox(dataset);
            // 入库
            TiffMetadata userTiffMetadata = new TiffMetadata(tiffOriginalName, tiffFile.getPath(), bbox, " ", " ");
            userDataMapper.addTiff(userTiffMetadata);

            // 文件流存储到minio
            String cogCachePath = COG + file.getOriginalFilename();
            Convert2COGUtil.process(tiffFile.getPath(), cogCachePath);

            InputStream inputStream = Files.newInputStream(new File(cogCachePath).toPath());
//            if (!minioUtil.put(inputStream, BUCKET, "TIFF/" + tiffOriginalName)) {
//                return httpResponseUtil.failure("上传影像保存到minio失败");
//            }
            if (!minioUtil.put(inputStream, BUCKET, "User/" + tiffOriginalName)) {
                return httpResponseUtil.failure("上传影像保存到minio失败");
            }

            // 删除中间文件
            File srcFile = new File(tiffPath);
            srcFile.delete();
            File cogFile = new File(cogCachePath);
            cogFile.delete();
            File ovrFile = new File(cogCachePath + ".ovr");
            ovrFile.delete();

            tiffMetadata.put(count, userDataMapper.getTiffMetadataByNames(tiffOriginalName));
            count++;
        }

        if (!tiffMetadata.isEmpty()) {
            return httpResponseUtil.ok("上传影像成功", tiffMetadata);
        }else {
            return httpResponseUtil.failure("上传影像失败");
        }
    }

}
