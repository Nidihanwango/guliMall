package com.atguigu.gulimall.common.utils;

import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回远程调用结果
 */
public class F<T>{

    private final HashMap<String, Object> map = new HashMap<>();
    private T data;

    public F() {
        put("code", 0);
        put("msg", "success");
    }

    public static <T> F<T> error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static <T> F<T> error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static <T> F<T> error(int code, String msg) {
        F<T> f = new F<>();
        f.put("code", code);
        f.put("msg", msg);
        return f;
    }

    public static <T> F<T> ok(String msg) {
        F<T> f = new F<>();
        f.put("msg", msg);
        return f;
    }

    public static <T> F<T> ok(Map<String, Object> map) {
        F<T> f = new F<>();
        f.map.putAll(map);
        return f;
    }

    public static <T> F<T> ok() {
        return new F<>();
    }

    public F<T> put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Integer getCode() {
        return (Integer) this.map.get("code");
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
