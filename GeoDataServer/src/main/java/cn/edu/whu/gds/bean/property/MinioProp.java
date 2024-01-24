package cn.edu.whu.gds.bean.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProp {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}