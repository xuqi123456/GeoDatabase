package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.TileService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import cn.edu.whu.gds.util.MinioUtil;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.imintel.mbtiles4j.MBTilesReadException;
import org.imintel.mbtiles4j.MBTilesReader;
import org.imintel.mbtiles4j.model.MetadataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/tile")
public class TileController {
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private TileService tileService;

    @RequestMapping(value = "/{tiles-name}/{zoom}/{x}/{y}.png", method = RequestMethod.GET, produces = "application/json")
    public void getTile(@PathVariable("tiles-name") String tilesName, @PathVariable("zoom") Integer zoom,
                        @PathVariable("x") Integer x, @PathVariable("y") Integer y,
                        HttpServletResponse response) throws MBTilesReadException,
            IOException, InvalidBucketNameException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException,
            XmlParserException, InvalidResponseException, InternalException {
        tileService.getTile(tilesName, zoom, x, y, response);
    }
}
