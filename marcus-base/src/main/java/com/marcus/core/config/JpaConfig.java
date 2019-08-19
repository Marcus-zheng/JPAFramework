package com.marcus.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 16:55
 **/
@Configuration
@EnableJpaRepositories(basePackages = { "com.marcus.**.dao" })
@EntityScan("com.marcus.**.model")
public class JpaConfig {
}
