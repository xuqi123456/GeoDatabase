package cn.edu.whu.gds.util;

import cn.edu.whu.gds.bean.vo.Response;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.BosObject;
import io.minio.errors.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class BosUtil {
    private static final String tmpDir = "/home/oge/oge-server/extended-applications/tilesTest/tmp/";
    @Autowired
    private BosClient bosClient;
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    public boolean exist(Bucket bucket, String objectName) {
        return bosClient.doesObjectExist(bucket.getBucketName(), objectName);
    }

    public InputStream get(Bucket bucket, String objectName) {
        return bosClient.getObject(bucket.getBucketName(), objectName).getObjectContent();
    }

    public File getFile(Bucket bucket, String objectName) {
        File file = new File(tmpDir + objectName);
        if (file.exists()) {
            return file;
        }

        InputStream inputStream = get(bucket, objectName);
        File dir = new File(tmpDir);
        if (!dir.exists() || dir.isFile()) {
            dir.mkdirs();
        }
        file = new File(tmpDir + objectName);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public boolean put(MultipartFile file, Bucket bucket, String objectName) throws IOException {
        InputStream inputStream = file.getInputStream();
        bosClient.putObject(bucket.getBucketName(), objectName, inputStream);
        inputStream.close();
        return true;
    }

    public boolean put(InputStream inputStream, Bucket bucket, String objectName) throws IOException {
        bosClient.putObject(bucket.getBucketName(), objectName, inputStream);
        inputStream.close();
        return true;
    }

    public void download(HttpServletResponse response, Bucket bucket, String objectName) {
        System.out.println("url: " + bucket.getBucketName() + "/" + objectName);
        BosObject object = bosClient.getObject(bucket.getBucketName(), objectName);
        InputStream inputStream = object.getObjectContent();
        String[] path = objectName.split("/");
        try {
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;filename=" + path[path.length - 1]);
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copy(Bucket bucket, String objectName, String srcObjectName) throws
            IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, InternalException,
            XmlParserException, InvalidBucketNameException, ErrorResponseException {
        bosClient.copyObject(bucket.getBucketName(), srcObjectName, bucket.getBucketName(), objectName);
    }

    public Response delete(Bucket bucket, String objectName) {
        bosClient.deleteObject(bucket.getBucketName(), objectName);
        return httpResponseUtil.ok("删除成功");
    }
}
