package cn.edu.whu.gds.util;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.encoder.datastore.GSGeoTIFFDatastoreEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;

@Component
public class PublishImageUtil {
//    public static void main(String[] args) throws Exception {
//        String wsName="publictif";
//        String layerPath="C:\\Users\\Administrator\\Desktop\\shuju\\result.tif";
//        String layerCode="geotif";
//        boolean publishImageLayer = publishImageLayer(wsName, layerPath, layerCode);
//        System.out.println(publishImageLayer);
//    }
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    public boolean publishImageLayer(String wsName, String layerPath, String layerCode) throws Exception {
        String gsImageUrl="http://localhost:8060/geoserver";
        String gsImageUser= "admin";
        String  gsImagePwd="geoserver";
        GeoServerRESTManager geoServerRESTManager = new GeoServerRESTManager(new URL(gsImageUrl), gsImageUser, gsImagePwd);
        GeoServerRESTPublisher geoServerRESTPublisher = geoServerRESTManager.getPublisher();
        GeoServerRESTReader geoServerRESTReader=geoServerRESTManager.getReader();

        // 判断workspace是否存在，不存在则创建
        List<String> workspaces = geoServerRESTReader.getWorkspaceNames();
        if (!workspaces.contains(wsName)) {
            boolean isCreateWs =geoServerRESTPublisher.createWorkspace(wsName);
            httpResponseUtil.ok("Create ws : {}" , isCreateWs);
        } else {
            httpResponseUtil.failure("Workspace already exist" );
        }
        // String fileName = layerPath.split("/")[layerPath.split("/").length-1];
//        String storeName = fileName.substring(0, fileName.length()-4);
        String storeName = layerCode;
//        if("basemap".equals(layerCode)) {
//            storeName = "raster_basemap";
//            // todo 判断raster_basemap是否存在，如果存在，则不发布。return true
//        }
        RESTDataStore restStore = geoServerRESTReader.getDatastore(wsName, storeName);
        if(restStore == null) {
            GSGeoTIFFDatastoreEncoder gtde = new GSGeoTIFFDatastoreEncoder(storeName);
            gtde.setWorkspaceName(wsName);
            gtde.setUrl(new URL("file:" + layerPath));
            boolean createStore = geoServerRESTManager.getStoreManager().create(wsName, gtde);
            httpResponseUtil.ok("Create store (TIFF文件创建状态) : " + createStore);
        }else {
//            if("raster_basemap".equals(storeName)){
//                // todo 图层组已存在返回true,否则重新发布
//            raster_basemap已存在，返回true
//                return true;
//            }
        }

        boolean isPublished =geoServerRESTPublisher.publishGeoTIFF(wsName, storeName, new File(layerPath));
        httpResponseUtil.ok("publish (TIFF文件发布状态): " + isPublished);
        return isPublished;
    }

}
