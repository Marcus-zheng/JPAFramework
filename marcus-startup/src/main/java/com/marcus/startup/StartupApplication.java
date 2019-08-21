package com.marcus.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/21 17:36
 **/
@SpringBootApplication
@EnableAsync
public class StartupApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(StartupApplication.class);
        springApplication.run(args);
    }
}
