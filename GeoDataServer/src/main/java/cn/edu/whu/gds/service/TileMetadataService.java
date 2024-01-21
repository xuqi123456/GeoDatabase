package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.entity.TileMetadata;
import cn.edu.whu.gds.bean.vo.Response;

public interface TileMetadataService {
    Response getTile(String name);

    Response addTile(String name, TileMetadata tileMetadata);

    Response updateTile(String name, TileMetadata tileMetadata);
}
