package com.atguigu.gulimall;

import com.aliyun.oss.OSS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallThirdPartyApplicationTests {
    @Resource
    private OSS ossClient;

    @Test
    public void testUpload(){
        ossClient.putObject("gulimall-ndhw", "abc.jpg", new File("E:\\大型电商--谷粒商城\\1.分布式基础（全栈开发篇）\\资料源码\\pics\\6a1b2703a9ed8737.jpg"));
    }
}
