package cn.edu.whu.gds.bean.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "bos")
@Component
public class BosProp {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
