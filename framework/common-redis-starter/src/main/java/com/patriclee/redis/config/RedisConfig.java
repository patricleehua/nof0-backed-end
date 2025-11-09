package com.patriclee.redis.config;

import com.patriclee.redis.service.RedisService;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

@AutoConfiguration
@Slf4j
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用 fastjson2 作为值序列化器
        FastJson2RedisSerializer<Object> fastJson2 = new FastJson2RedisSerializer<>(Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(fastJson2);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(fastJson2);

        template.afterPropertiesSet();
        return template;
    }
    /**
     * 创建 Redis 连接工厂，兼容无密码和有密码两种场景
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory(RedisProperties props) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(props.getHost());
        config.setPort(props.getPort());
        config.setDatabase(props.getDatabase());

        String password = props.getPassword();
        if (StringUtils.hasText(password)) {
            config.setPassword(password);
            log.info("Redis连接已配置密码，将使用密码认证。主机: {}:{}", props.getHost(), props.getPort());
        } else {
            log.info("Redis连接未配置密码，将尝试无密码连接。主机: {}:{}", props.getHost(), props.getPort());
        }

        Duration timeout = props.getTimeout() != null ? props.getTimeout() : Duration.ofSeconds(5);

        ClientOptions clientOptions = ClientOptions.builder()
                .protocolVersion(ProtocolVersion.RESP2)
                .autoReconnect(true)
                .socketOptions(SocketOptions.builder()
                        .connectTimeout(timeout)
                        .keepAlive(true)
                        .build())
                .build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(timeout)
                .clientOptions(clientOptions)
                .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
        factory.setValidateConnection(true); // 验证连接
        factory.afterPropertiesSet();
        // 测试连接
        try {
            factory.getConnection().ping();
            log.info("✅ Redis连接测试成功 - {}:{}", props.getHost(), props.getPort());
        } catch (Exception e) {
            log.error("❌ Redis连接测试失败", e);
            if (!StringUtils.hasText(password)) {
                log.warn("提示：当前配置为无密码连接，但连接失败。请确认Redis服务器是否要求密码认证。");
            }
            throw new IllegalStateException("Redis连接失败", e);
        }

        return factory;
    }

    /**
     * 配置 RedisTemplate
     * @param redisTemplate RedisTemplate 实例
     * @return RedisService
     */
    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
        log.info("========初始化 RedisService 自定义 Redis 缓存服务========");
        return new RedisService(redisTemplate);
    }

}
