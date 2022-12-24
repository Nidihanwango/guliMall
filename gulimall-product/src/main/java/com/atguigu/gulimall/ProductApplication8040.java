package com.atguigu.gulimall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gulimall.product.dao")
public class ProductApplication8040 {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication8040.class, args);
    }
}
