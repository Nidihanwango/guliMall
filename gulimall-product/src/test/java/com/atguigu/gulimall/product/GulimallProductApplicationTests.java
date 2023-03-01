package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallProductApplicationTests {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void test() {
        Long[] path = categoryService.getPath(225L);
        System.out.println(Arrays.asList(path));
    }
}
