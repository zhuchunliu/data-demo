package com.person.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Darren on 2018-06-25
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.person.demo.dao")
@ComponentScan("com.person.demo")
public class MasterApplication {
    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
    }
}
