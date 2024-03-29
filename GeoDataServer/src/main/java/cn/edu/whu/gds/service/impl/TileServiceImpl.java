package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.mapper.TileMapper;
import cn.edu.whu.gds.service.TileService;
import cn.edu.whu.gds.mapper.TiffRepository;
import cn.edu.whu.gds.util.Bucket;
import cn.edu.whu.gds.util.HttpResponseUtil;
import cn.edu.whu.gds.util.MinioUtil;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.imintel.mbtiles4j.MBTilesReadException;
import org.imintel.mbtiles4j.MBTilesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class TileServiceImpl implements TileService {
    @Autowired
    private TileMapper tileMapper;
    @Autowired
    private MinioUtil storage;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private TiffRepository tiffRepository;

    @Override
    public void getTile(String tilesName, Integer zoom, Integer x, Integer y,
                        HttpServletResponse response) throws MBTilesReadException,
            IOException, InvalidBucketNameException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException,
            XmlParserException, InvalidResponseException, InternalException {
        String objectKey = tileMapper.getObjectKey(tilesName);
        log.info("objectKey: " + objectKey);

        MBTilesReader reader = new MBTilesReader(storage.getFile(Bucket.TILES, objectKey));
        InputStream data = reader.getTile(zoom, x, y).getData();

        IOUtils.copy(data, response.getOutputStream());
        log.info("get data");
        data.close();
        reader.close();
    }
}
