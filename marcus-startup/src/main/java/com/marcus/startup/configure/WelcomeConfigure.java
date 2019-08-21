package com.marcus.startup.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 设置启动跳转默认页
 * @author Charles
 * @since 2019-07-07
 */
@Configuration
public class WelcomeConfigure implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {


        registry.addViewController("/").setViewName("login/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

    }
}
