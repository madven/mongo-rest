package com.selcukc.mongo_rest.config;

import com.selcukc.mongo_rest.helper.AdminHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.http.HttpSessionListener;

@Configuration
@Import({BaseDataConfig.class})
public class AppConfig {
    @Bean
    public AdminHelper adminHelper() {
        return new AdminHelper();
    }

    @Bean
    public HttpSessionListener sessionListener() {
        return new SessionListener();
    }

}

