package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.catalog.TiffCatalog;
import cn.edu.whu.gds.bean.entity.TiffMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.TiffMetadataService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/tiff-metadata")
public class TiffMetadataController {
    @Autowired
    private TiffMetadataService tiffMetadataService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    @RequestMapping(value = "/catalog", method = RequestMethod.GET, produces = "application/json")
    public Response getTiffCatalog() {
        return tiffMetadataService.getTiffCatalog();
    }

    @RequestMapping(value = "/catalog/task", method = RequestMethod.GET, produces = "application/json")
    public Response getTiffTaskCatalog() {
        return tiffMetadataService.getTiffTaskCatalog();
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public Response getTiffPath(@RequestParam("wkt") String wkt) {
        return tiffMetadataService.getTiffPath(wkt);
    }

    @RequestMapping(value = "/geojson", method = RequestMethod.POST, produces = "application/json")
    public Response getTiffPathByGeoJson(@RequestBody JSONObject jsonObject) {
        return tiffMetadataService.getTiffPathByGeoJson(jsonObject.getString("geojson"));
    }

    @RequestMapping(value = "/rpc", method = RequestMethod.GET, produces = "application/json")
    public Response getTiffPathByGeoJsonRPC() throws IOException {
        File file = new File("C:\\Users\\DELL\\Desktop\\xc\\wh.geojson");
        JSONObject jsonObject = JSON.parseObject(FileUtils.readFileToString(file));
//        JSONObject jsonObject = JSON.parseObject(restTemplate.getForEntity("http://202.114.114.21:8005/gds/tiff-metadata/catalog", String.class).getBody());
        JSONArray jsonArray = jsonObject.getJSONArray("features");
        Map<String, JSONObject> map = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject geoJson = jsonArray.getJSONObject(i);
            JSONObject request = new JSONObject();
            request.put("geojson", geoJson.getString("geometry"));
            map.put(geoJson.getJSONObject("properties").getString("NAME"),
                    restTemplate.postForEntity("http://202.114.114.21:8005/gds/tiff-metadata/geojson", request, JSONObject.class).getBody()
            );
        }
        return httpResponseUtil.ok("测试", map);
    }
}
