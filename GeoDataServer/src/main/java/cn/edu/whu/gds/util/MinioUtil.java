package cn.edu.whu.gds.util;

import cn.edu.whu.gds.bean.vo.Response;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class MinioUtil {
    private static final String tmpDir = "C:\\Users\\DELL\\Desktop\\tmp\\";
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    public boolean exist(Bucket bucket, String objectName) {
        try {
            minioClient.statObject(bucket.getBucketName(), objectName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public InputStream get(Bucket bucket, String objectName) throws
            InvalidBucketNameException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, IOException, InvalidKeyException,
            XmlParserException, InvalidResponseException, InternalException {
        return minioClient.getObject(bucket.getBucketName(), objectName);
    }

    public String getUrl(Bucket bucket, String objectName) throws
            InvalidBucketNameException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, IOException, InvalidExpiresRangeException, InvalidKeyException,
            XmlParserException, InvalidResponseException, InternalException {
        return minioClient.presignedGetObject(bucket.getBucketName(), objectName);
    }

    public File getFile(Bucket bucket, String objectName) throws
            InvalidBucketNameException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, IOException, InvalidKeyException,
            XmlParserException, InvalidResponseException, InternalException {
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

    public List<String> getFilenames(Bucket bucket, String objectName) throws Exception {
        List<String> list = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(bucket.getBucketName(), objectName);
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item.isDir()) {
                List<String> files = getFilenames(bucket, item.objectName());
                list.addAll(files);
            } else {
                list.add(item.objectName());
            }
        }
        return list;
    }

    public void download(HttpServletResponse response,
                         Bucket bucket,
                         String objectName) {
        InputStream inputStream = null;
        String[] path = objectName.split("/");
        try {
            ObjectStat stat = minioClient.statObject(bucket.getBucketName(), objectName);
            // response.setContentType(stat.contentType());
            response.setContentType("multipart/form-data");
//            response.setHeader("Content-Disposition", "attachment;filename=" +
//                    URLEncoder.encode(path[path.length - 1], "UTF-8"));
            response.setHeader("Content-Disposition", "attachment;filename=" + path[path.length - 1]);

            inputStream = minioClient.getObject(bucket.getBucketName(), objectName);
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
        // return responseEntity;
    }

    public void downloadZip(HttpServletResponse response,
                            Bucket bucket,
                            String objectName) throws Exception {
        InputStream inputStream = null;
        List<String> files = getFilenames(bucket, objectName);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            for (String file : files) {
                inputStream = minioClient.getObject(bucket.getBucketName(), file);
                ZipEntry entry = new ZipEntry(file);
                zipOutputStream.putNextEntry(entry);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, len);
                }
                inputStream.close();
                zipOutputStream.closeEntry();
                zipOutputStream.flush();
            }
            zipOutputStream.finish();
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean put(MultipartFile file, Bucket bucket, String objectName) throws IOException {
        InputStream inputStream = file.getInputStream();
        try {
            minioClient.putObject(bucket.getBucketName(), objectName, inputStream,
                    new PutObjectOptions(inputStream.available(), -1));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean put(InputStream inputStream, Bucket bucket, String objectName) throws IOException {
        try {
            minioClient.putObject(bucket.getBucketName(), objectName, inputStream,
                    new PutObjectOptions(inputStream.available(), -1));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void copy(Bucket bucket, String objectName, String srcObjectName) throws
            IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, InternalException,
            XmlParserException, InvalidBucketNameException, ErrorResponseException {
        minioClient.copyObject(bucket.getBucketName(), objectName, null, null,
                bucket.getBucketName(), srcObjectName, null, null);
    }

    public Response delete(Bucket bucket, String objectName) {
        try {
            minioClient.removeObject(bucket.getBucketName(), objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return httpResponseUtil.internalServerError("删除失败");
        }
        return httpResponseUtil.ok("删除成功");
    }
}
