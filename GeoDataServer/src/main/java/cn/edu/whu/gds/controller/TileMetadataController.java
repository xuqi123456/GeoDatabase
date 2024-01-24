package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.entity.TileMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.TileMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/tile-metadata")
public class TileMetadataController {
    @Autowired
    private TileMetadataService tileMetadataService;

    @RequestMapping(value = "/{tile-name}", method = RequestMethod.GET, produces = "application/json")
    public Response getTile(@PathVariable("tile-name") String name) {
        return tileMetadataService.getTileMetadata(name);
    }

    @RequestMapping(value = "/{tile-name}", method = RequestMethod.POST, produces = "application/json")
    public Response addTile(@PathVariable("tile-name") String name,
                            @RequestBody TileMetadata tileMetadata) {
        return tileMetadataService.addTileMetadata(name, tileMetadata);
    }

    @RequestMapping(value = "/{tile-name}", method = RequestMethod.PUT, produces = "application/json")
    public Response updateTile(@PathVariable("tile-name") String name,
                               @RequestBody TileMetadata tileMetadata) {
        return tileMetadataService.updateTileMetadata(name, tileMetadata);
    }
}
