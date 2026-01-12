package com.dopp.doppapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dopp.doppapi.mapper")
public class DoppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoppApiApplication.class, args);
    }

}
