package com.atguigu.gulimall;


import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import javax.annotation.Resource;

@SpringBootTest
public class ProductTest {

    @Resource
    private BrandService brandService;

    @Test
    public void test() {
        System.out.println(brandService);
        BrandEntity brand = new BrandEntity();
        brand.setName("华为");
        boolean save = brandService.save(brand);
        System.out.println("save.result: " + save);
    }

}
