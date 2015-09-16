package com.wiysoft.report;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(WiyReportApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WiyReportApplication.class, args);
        logger.info("WiyReport application started.");
    }
}
