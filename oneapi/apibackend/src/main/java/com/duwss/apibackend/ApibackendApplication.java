package com.duwss.apibackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan("com.duwss.apibackend.mapper")
public class ApibackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApibackendApplication.class, args);
    }

}
