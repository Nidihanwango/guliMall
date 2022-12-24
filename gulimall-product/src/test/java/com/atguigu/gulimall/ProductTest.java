package com.atguigu.gulimall;


import com.atguigu.gulimall.product.service.BrandService;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductTest {


    @Autowired
    private BrandService brandService;

    @Test
    public void test() {
        System.out.println(brandService);
    }

}
