package com.wiysoft.report;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created by weiliyang on 7/24/15.
 */
@SpringBootApplication
@EnableConfigurationProperties({WiyReportConfiguration.class})
public class WiyReportApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WiyReportApplication.class, args);
    }
}
