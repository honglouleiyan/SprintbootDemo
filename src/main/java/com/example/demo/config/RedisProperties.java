package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/18
 * @time: 14:32
 * @description:
 **/
@Component
@Data
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisProperties {
    private String nodes;
    private long maxWaitMillis;

}
