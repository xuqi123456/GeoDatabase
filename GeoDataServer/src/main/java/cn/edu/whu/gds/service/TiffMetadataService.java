package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.vo.Response;
import io.minio.errors.*;
import org.imintel.mbtiles4j.MBTilesReadException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public interface TiffMetadataService {
    // 获取tiff影像路径
    Response getTiffPath(String wkt);
}
