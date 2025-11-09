package com.patriclee.redis.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    public static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("org.springframework", "com.theninefactory");

    private final Class<T> clazz;

    public FastJson2RedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) return new byte[0];
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName)
                .getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) return null;
        String json = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(json, clazz, AUTO_TYPE_FILTER);
    }
}
