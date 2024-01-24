package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.entity.TileMetadata;
import cn.edu.whu.gds.bean.vo.Response;

public interface TileMetadataService {
    Response getTileMetadata(String name);

    Response addTileMetadata(String name, TileMetadata tileMetadata);

    Response updateTileMetadata(String name, TileMetadata tileMetadata);
}

