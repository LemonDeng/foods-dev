package com.dzl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "com.dzl.mapper")
@ComponentScan(basePackages = {"com.dzl", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
