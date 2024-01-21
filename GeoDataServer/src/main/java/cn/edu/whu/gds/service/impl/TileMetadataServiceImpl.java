package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.TileMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.TileMetadataService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TileMetadataServiceImpl implements TileMetadataService {
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    @Override
    public Response getTile(String name) {
        return httpResponseUtil.ok("获取瓦片元数据");
    }

    @Override
    public Response addTile(String name, TileMetadata tileMetadata) {
        return httpResponseUtil.ok("新增瓦片元数据");
    }

    @Override
    public Response updateTile(String name, TileMetadata tileMetadata) {
        return httpResponseUtil.ok("更新瓦片元数据");
    }
}
