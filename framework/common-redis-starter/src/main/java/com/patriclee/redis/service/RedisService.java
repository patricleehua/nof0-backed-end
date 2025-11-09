package com.patriclee.redis.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis 缓存操作服务类，提供对象、集合、Map 等常用封装方法
 */
@Component
@RequiredArgsConstructor
public class RedisService {

    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存对象
     * @param key 缓存键
     * @param value 缓存值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存对象并设置过期时间
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 有效时间
     * @param unit 时间单位
     */
    public <T> void setCacheObject(final String key, final T value, final long timeout, final TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取缓存的对象
     * @param key 缓存键
     * @return 缓存对象
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (T) ops.get(key);
    }

    /**
     * 删除缓存的单个对象
     * @param key 缓存键
     * @return 是否删除成功
     */
    public boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 批量删除缓存对象
     * @param keys 多个键
     * @return 删除数量是否大于 0
     */
    public boolean deleteObject(final Collection<String> keys) {
        Long deleted = redisTemplate.delete(keys);
        return deleted != null && deleted > 0;
    }

    /**
     * 设置键的过期时间（默认单位秒）
     * @param key 缓存键
     * @param timeout 过期时间
     * @return 是否设置成功
     */
    public boolean expire(final String key, final long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }

    /**
     * 设置键的过期时间（指定单位）
     * @param key 缓存键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取键的剩余有效时间
     * @param key 缓存键
     * @return 剩余时间（秒）
     */
    public long getExpire(final String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire != null ? expire : -1L;
    }

    /**
     * 判断键是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    public Boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 缓存 List 数据
     * @param key 缓存键
     * @param dataList 要缓存的数据
     * @return 成功缓存的数量
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count != null ? count : 0;
    }

    /**
     * 获取缓存的 List 数据
     * @param key 缓存键
     * @return List 数据
     */
    public <T> List<T> getMultiCacheListValue(final String key, final Collection<Object> hKeys, Class<T> type) {
        List<Object> result = redisTemplate.opsForHash().multiGet(key, hKeys);
        return result.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    /**
     * 缓存 Set 数据
     * @param key 缓存键
     * @param dataSet 要缓存的数据
     * @return 缓存操作器
     */
    public <T> BoundSetOperations<String, Object> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, Object> setOps = redisTemplate.boundSetOps(key);
        for (T t : dataSet) setOps.add(t);
        return setOps;
    }

    /**
     * 获取缓存的 Set 数据
     * @param key 缓存键
     * @return Set 数据
     */
    public <T> Set<T> getCacheSet(final String key, Class<T> type) {
        Set<Object> result = redisTemplate.opsForSet().members(key);
        if (result == null) {
            return Set.of();
        }
        return result.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toSet());
    }

    /**
     * 缓存 Map 数据
     * @param key 缓存键
     * @param dataMap Map 数据
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        redisTemplate.opsForHash().putAll(key, dataMap);
    }

    /**
     * 获取缓存的Map对象
     *
     * @param key 缓存的键
     * @param type 值类型的class类型
     * @return 缓存键值对应的Map对象
     */
    public <T> Map<String, T> getCacheMap(final String key, Class<T> type) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            // 使用类型安全的类型转换
            result.put(String.valueOf(entry.getKey()), type.cast(entry.getValue()));
        }
        return result;
    }

    /**
     * 设置 Map 中的单个键值
     * @param key 缓存键
     * @param hKey hash 键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取 Map 中的单个值
     *
     * @param key 缓存键
     * @param hKey hash 键
     * @param type 值的类型
     * @return 值
     */
    public <T> T getCacheMapValue(final String key, final String hKey, Class<T> type) {
        Object value = redisTemplate.opsForHash().get(key, hKey);
        return type.cast(value);
    }

    /**
     * 批量获取 Map 中的多个值
     *
     * @param key 缓存键
     * @param hKeys hash 键集合
     * @param type 值的类型
     * @return 多个值
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys, Class<T> type) {
        List<Object> values = redisTemplate.opsForHash().multiGet(key, hKeys);
        return values.stream()
                .map(type::cast)          // 直接转换为目标类型
                .collect(Collectors.toList());
    }


    /**
     * 删除 Map 中的某个字段
     * @param key 缓存键
     * @param hKey hash 键
     * @return 是否删除成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获取匹配 pattern 的所有键
     * @param pattern 通配符键（如 "user:*"）
     * @return 键集合
     */
    public Set<String> keys(final String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            // 捕获连接异常并记录日志
            log.error("Redis keys operation failed", e);
            return Collections.emptySet();
        }
    }
    /**
     * 仅当 key 不存在时，设置缓存，并指定过期时间（原子操作，适用于分布式锁等场景）
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 设置成功返回 true（即 key 原本不存在，现已成功设置）；否则返回 false
     */
    public boolean setIfAbsent(final String key, final String value, final long timeout, final TimeUnit unit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        return Boolean.TRUE.equals(result);
    }
}
