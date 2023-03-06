package com.atguigu.gulimall.product.handler;

import com.atguigu.gulimall.common.exception.GulimallException;
import com.atguigu.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
@Slf4j
public class GulimallExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> map = new HashMap<>();
        log.error("GulimallExceptionHandler捕获异常: {}", e.getMessage());
        log.error("GulimallExceptionHandler捕获异常类型: {}", e.getClass());
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach((item) -> {
            String field = item.getField();
            String message = item.getDefaultMessage();
            map.put(field, message);
        });
        return R.error(GulimallException.VALID_EXCEPTION.getCode(), GulimallException.VALID_EXCEPTION.getMessage()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleCommonException(Throwable t) {
        t.printStackTrace();
        log.error("GulimallExceptionHandler捕获异常类型: {}", t.getClass());
        return R.error(GulimallException.UNKNOW_EXCEPTION.getCode(), GulimallException.UNKNOW_EXCEPTION.getMessage());
    }
}
