package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.entity.TiffMetadata;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.TiffMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/tiff-metadata")
public class TiffMetadataController {
    @Autowired
    private TiffMetadataService tiffMetadataService;

    @RequestMapping(value = "/catalog", method = RequestMethod.GET, produces = "application/json")
    public Response getTiffCatalog() {
        return tiffMetadataService.getTiffCatalog();
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public Response getTiffPath(@RequestParam("wkt") String wkt) {
        return tiffMetadataService.getTiffPath(wkt);
    }
}
