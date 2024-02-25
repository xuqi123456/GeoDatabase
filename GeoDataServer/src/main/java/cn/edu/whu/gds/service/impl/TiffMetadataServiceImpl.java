package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.TiffMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.mapper.TileMapper;
import cn.edu.whu.gds.mapper.TiffRepository;
import cn.edu.whu.gds.service.TiffMetadataService;
import cn.edu.whu.gds.util.BosUtil;
import cn.edu.whu.gds.util.Bucket;
import cn.edu.whu.gds.util.HttpResponseUtil;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.imintel.mbtiles4j.MBTilesReadException;
import org.imintel.mbtiles4j.MBTilesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
public class TiffMetadataServiceImpl implements TiffMetadataService {
    @Autowired
    private TileMapper tileMapper;
    //    @Autowired
//    private MinioUtil storage;
    @Autowired
    private BosUtil storage;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private TiffRepository tiffRepository;

    @Override
    public Response getTiffPath(String wkt){
        try {
            // 查询tiff路径
            List<String> tiffPath= tiffRepository.getPathByWkt(wkt);
            if( wkt!= null){
                return httpResponseUtil.ok("获取tiff路径", tiffPath);
            }else {
                return httpResponseUtil.ok("tiff路径不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            return httpResponseUtil.badRequest("获取tiff路径时发生异常");
        }
    }
}
