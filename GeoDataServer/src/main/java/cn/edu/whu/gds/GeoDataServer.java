package cn.edu.whu.gds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class GeoDataServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(GeoDataServer.class, args);
    }
}