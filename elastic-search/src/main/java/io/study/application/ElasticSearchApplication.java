package io.study.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * 该项目目前没使用数据库，但是
 */
@SpringBootApplication(scanBasePackages ={"io.study"},
        exclude = {RedisAutoConfiguration.class, SessionAutoConfiguration.class})
//@ServletComponentScan

@EnableTransactionManagement
public class ElasticSearchApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
        System.out.println("----------------start end--------------------");
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(ElasticSearchApplication.class);

    }*/
}