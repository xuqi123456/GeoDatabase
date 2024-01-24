package cn.edu.whu.gds.service;

import io.minio.errors.*;
import org.imintel.mbtiles4j.MBTilesReadException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public interface TileService {
    void getTile(String tilesName, Integer zoom, Integer x, Integer y,
                 HttpServletResponse response) throws MBTilesReadException, IOException,
            InvalidBucketNameException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, InvalidKeyException, XmlParserException,
            InvalidResponseException, InternalException;
}
