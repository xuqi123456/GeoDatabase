package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.catalog.TiffCatalog;
import cn.edu.whu.gds.bean.entity.TiffMetadata;
import cn.edu.whu.gds.bean.entity.TileMetadata;
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
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.imintel.mbtiles4j.MBTilesReadException;
import org.imintel.mbtiles4j.MBTilesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Response getTiffCatalog() {
        List<TiffMetadata> tiffMetadataList = tiffRepository.getAllTiffMetadata();
        Map<String, TiffCatalog> provinceMap = new HashMap<>();
        Map<String, TiffCatalog> cityMap = new HashMap<>();
        List<TiffCatalog> ans = new ArrayList<>();

        int provinceId = 1;
        int cityId = 35;
        int tId = 1000;
        for (TiffMetadata tiffMetadata : tiffMetadataList) {
            String province = tiffMetadata.getProvince();
            String city = tiffMetadata.getCity();
            String name = tiffMetadata.getName();
            Integer tiffId = tiffMetadata.getId();

            if (!provinceMap.containsKey(province)) {
                List<TiffCatalog> children = new ArrayList<>();
                if (city == null) {
                    children.add(new TiffCatalog(tId, name, tiffId));
                } else {
                    List<TiffCatalog> cityChildren = new ArrayList<>();
                    cityChildren.add(new TiffCatalog(tId, name, tiffId));
                    TiffCatalog cityNode = new TiffCatalog(cityId, city, cityChildren);
                    cityMap.put(city, cityNode);
                    children.add(cityNode);
                    ++cityId;
                }
                TiffCatalog provinceNode = new TiffCatalog(provinceId, province, children);
                ans.add(provinceNode);
                provinceMap.put(province, provinceNode);
                ++provinceId;
            } else {
                TiffCatalog provinceNode = provinceMap.get(province);
                if (city == null) {
                    provinceNode.getChildren().add(new TiffCatalog(tId, name, tiffId));
                } else if (!cityMap.containsKey(city)) {
                    List<TiffCatalog> cityChildren = new ArrayList<>();
                    cityChildren.add(new TiffCatalog(tId, name, tiffId));
                    TiffCatalog cityNode = new TiffCatalog(cityId, city, cityChildren);
                    cityMap.put(city, cityNode);
                    provinceNode.getChildren().add(cityNode);
                    ++cityId;
                } else {
                    cityMap.get(city).getChildren().add(new TiffCatalog(tId, name, tiffId));
                }
            }
            ++tId;
        }

        return httpResponseUtil.ok("获取影像树", ans);
    }

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
            return httpResponseUtil.failure("获取tiff路径时发生异常");
        }
    }
}
