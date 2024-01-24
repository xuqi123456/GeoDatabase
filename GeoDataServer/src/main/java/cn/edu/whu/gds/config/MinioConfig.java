package cn.edu.whu.gds.config;

import cn.edu.whu.gds.bean.property.MinioProp;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Autowired
    private MinioProp minioProp;

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(minioProp.getEndpoint(), minioProp.getAccessKey(), minioProp.getSecretKey());
    }
}
