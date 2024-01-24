package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.TileMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.TileMetadataRepository;
import cn.edu.whu.gds.service.TileMetadataService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TileMetadataServiceImpl implements TileMetadataService {
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private TileMetadataRepository tileMetadataRepository;

    @Override
    public Response getTileMetadata(String name) {
       try {
           // 查询瓦片数据
           TileMetadata tileMetadata = tileMetadataRepository.findBySql(name);
           if(tileMetadata != null){
               return httpResponseUtil.ok("获取瓦片元数据", tileMetadata);
           }else {
               return httpResponseUtil.ok("瓦片数据不存在");
           }
       }catch (Exception e){
           e.printStackTrace();
           return httpResponseUtil.badRequest("获取瓦片元数据时发生异常");
       }
    }

    @Override
    public Response addTileMetadata(String name, TileMetadata tileMetadata) {
        return httpResponseUtil.ok("新增瓦片元数据");
    }

    @Override
    public Response updateTileMetadata(String name, TileMetadata tileMetadata) {
        return httpResponseUtil.ok("更新瓦片元数据");
    }
}
