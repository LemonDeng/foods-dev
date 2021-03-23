package com.dzl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
/*扫描mybatis通用mapper所在的包*/
@MapperScan(basePackages = "com.dzl.mapper")
/*扫描所有包以及相关组件包*/
@ComponentScan(basePackages = {"com.dzl","org.n3r.idworker"})

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
   /* @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }*/

}
