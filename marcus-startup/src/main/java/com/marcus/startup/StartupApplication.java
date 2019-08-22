package com.marcus.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/21 17:36
 **/
@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.marcus"})
@EnableJpaRepositories(basePackages = { "com.marcus.**.dao" })
@EntityScan("com.marcus.**.model")
public class StartupApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(StartupApplication.class);
        springApplication.run(args);
    }
}
