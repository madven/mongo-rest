package com.selcukc.mongo_rest.config;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.selcukc.mongo_rest.dao.Dao;
import com.selcukc.mongo_rest.dao.MongoDao;
import com.selcukc.mongo_rest.dao.MongoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.UnknownHostException;

@Configuration
@EnableAsync
public class BaseDataConfig {
    @Value("${mongo.server.address}")
    private String mongoAddress;
    @Value("${mongo.server.port}")
    private int mongoPort;
    @Value("${psql.server.address:127.0.0.1}")
    private String psqlAddress;
    @Value("${psql.server.user:cn}")
    private String psqlUser;
    @Value("${psql.server.password:cn}")
    private String psqlPass;

    @Bean
    public Dao mongoDao() throws UnknownHostException {
        return new MongoDao(mongoWrapper());
    }

    @Bean
    public MongoWrapper mongoWrapper() throws UnknownHostException {
        return new MongoWrapper(mongoAddress, mongoPort);
    }

    @Bean
    public Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        return executor;
    }

    @Bean
    public TaskExecutor secondaryExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        return executor;
    }

}

