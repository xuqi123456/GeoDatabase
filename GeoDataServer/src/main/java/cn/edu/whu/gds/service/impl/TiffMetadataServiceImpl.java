package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.catalog.TiffCatalog;
import cn.edu.whu.gds.bean.entity.TiffMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.mapper.TileMapper;
import cn.edu.whu.gds.mapper.TiffRepository;
import cn.edu.whu.gds.service.TiffMetadataService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import cn.edu.whu.gds.util.MinioUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class TiffMetadataServiceImpl implements TiffMetadataService {
    private final static Integer[] IDS = {2475, 2476, 2479, 2480, 2517, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    private final static String[] NAMES = {
            "H49F003031_Level_18.tif", "H49F003032_Level_18.tif",
            "H49F003035_Level_18.tif", "H49F003036_Level_18.tif",
            "H49F003044_Level_18.tif",
            "L52F048035.tif", "L52F048032.tif", "L52F048031.tif", "L52F048021.tif", "L52F048020.tif",
            "I48F048015.tif", "I48F048014.tif", "I48F048013.tif", "I48F048012.tif", "I48F048011.tif"
    };
    private final static String[] PATHS = {
            "http://202.114.114.21:8005/geoserver/gwc/service/wmts?layer=myspcoe:H49F003031_Level_18",
            "http://202.114.114.21:8005/geoserver/gwc/service/wmts?layer=myspcoe:H49F003032_Level_18",
            "http://202.114.114.21:8005/geoserver/gwc/service/wmts?layer=myspcoe:H49F003035_Level_18",
            "http://202.114.114.21:8005/geoserver/gwc/service/wmts?layer=myspcoe:H49F003036_Level_18",
            "http://202.114.114.21:8005/geoserver/gwc/service/wmts?layer=myspcoe:H49F003044_Level_18"
    };
    @Autowired
    private TileMapper tileMapper;
    @Autowired
    private MinioUtil storage;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private TiffRepository tiffRepository;

    @Override
    public Response getTiffCatalog() {
//        List<TiffMetadata> tiffMetadataList = tiffRepository.getAllTiffMetadata();
//        Map<String, TiffCatalog> provinceMap = new HashMap<>();
//        Map<String, TiffCatalog> cityMap = new HashMap<>();
//        List<TiffCatalog> ans = new ArrayList<>();
//
//        int provinceId = 1;
//        int cityId = 35;
//        int tId = 1000;
//        for (TiffMetadata tiffMetadata : tiffMetadataList) {
//            String province = tiffMetadata.getProvince();
//            String city = tiffMetadata.getCity();
//            String name = tiffMetadata.getName();
//            Integer tiffId = tiffMetadata.getId();
//
//            if (!provinceMap.containsKey(province)) {
//                List<TiffCatalog> children = new ArrayList<>();
//                if (city == null) {
//                    children.add(new TiffCatalog(tId, name, tiffId));
//                } else {
//                    List<TiffCatalog> cityChildren = new ArrayList<>();
//                    cityChildren.add(new TiffCatalog(tId, name, tiffId));
//                    TiffCatalog cityNode = new TiffCatalog(cityId, city, cityChildren);
//                    cityMap.put(city, cityNode);
//                    children.add(cityNode);
//                    ++cityId;
//                }
//                TiffCatalog provinceNode = new TiffCatalog(provinceId, province, children);
//                ans.add(provinceNode);
//                provinceMap.put(province, provinceNode);
//                ++provinceId;
//            } else {
//                TiffCatalog provinceNode = provinceMap.get(province);
//                if (city == null) {
//                    provinceNode.getChildren().add(new TiffCatalog(tId, name, tiffId));
//                } else if (!cityMap.containsKey(city)) {
//                    List<TiffCatalog> cityChildren = new ArrayList<>();
//                    cityChildren.add(new TiffCatalog(tId, name, tiffId));
//                    TiffCatalog cityNode = new TiffCatalog(cityId, city, cityChildren);
//                    cityMap.put(city, cityNode);
//                    provinceNode.getChildren().add(cityNode);
//                    ++cityId;
//                } else {
//                    cityMap.get(city).getChildren().add(new TiffCatalog(tId, name, tiffId));
//                }
//            }
//            ++tId;
//        }
        // 测试数据
//        List<TiffCatalog> ans = new ArrayList<>();
//        List<TiffCatalog> hbc = new ArrayList<>();
//        for (int i = 0; i < 5; ++i) {
//            hbc.add(new TiffCatalog(IDS[i], NAMES[i], IDS[i], PATHS[i]));
//        }
//        List<TiffCatalog> jlc = new ArrayList<>();
//        for (int i = 5; i < 10; ++i) {
//            jlc.add(new TiffCatalog(IDS[i], NAMES[i], IDS[i]));
//        }
//        List<TiffCatalog> scc = new ArrayList<>();
//        for (int i = 10; i < 15; ++i) {
//            scc.add(new TiffCatalog(IDS[i], NAMES[i], IDS[i]));
//        }
//        TiffCatalog hb = new TiffCatalog(1, "湖北省", hbc);
//        ans.add(hb);
//        TiffCatalog jl = new TiffCatalog(2, "吉林省", jlc);
//        ans.add(jl);
//        TiffCatalog sc = new TiffCatalog(3, "四川省", scc);
//        ans.add(sc);
        // 写死的返回值
        File file = new File("/home/whu/server/tiff.json");
        JSONArray jsonArray = null;
        try {
            jsonArray = JSON.parseArray(FileUtils.readFileToString(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResponseUtil.ok("获取影像树", jsonArray);
    }

    @Override
    public Response getTiffTaskCatalog() {
        List<Map<String, Object>> ans = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", IDS[i]);
            map.put("label", NAMES[i]);
            map.put("path", PATHS[i]);
            ans.add(map);
        }
        return httpResponseUtil.ok("获得任务界面待用影像", ans);
    }

    @Override
    public Response getTiffPath(String wkt){
        try {
            // 查询tiff路径
            List<String> tiffPath= tiffRepository.getPathByWkt(wkt);
            if(wkt != null){
                return httpResponseUtil.ok("获取tiff路径", tiffPath);
            } else {
                return httpResponseUtil.ok("tiff路径不存在");
            }
        } catch (Exception e){
            e.printStackTrace();
            return httpResponseUtil.failure("获取tiff路径时发生异常");
        }
    }

    @Override
    public Response getTiffPathByGeoJson(String geoJson) {
        try {
            // 查询tiff路径
            List<TiffMetadata> tiffPath= tiffRepository.getPathByGeoJson(geoJson);
            if(geoJson != null){
                return httpResponseUtil.ok("获取tiff路径", tiffPath);
            } else {
                return httpResponseUtil.ok("tiff路径不存在");
            }
        } catch (Exception e){
            e.printStackTrace();
            return httpResponseUtil.failure("获取tiff路径时发生异常");
        }
    }
}
