package cn.edu.whu.gds.config;

import cn.edu.whu.gds.bean.property.BosProp;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BosConfig {
    @Autowired
    private BosProp bosProp;

    @Bean
    public BosClient bosClient() {
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(bosProp.getAccessKey(), bosProp.getSecretKey()));
        config.setEndpoint(bosProp.getEndpoint());
        return new BosClient(config);
    }
}
