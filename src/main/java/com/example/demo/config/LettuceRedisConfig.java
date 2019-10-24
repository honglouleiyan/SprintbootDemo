package com.example.demo.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;

/**
 * @Auther: hongsheng.wei
 * @Date: 2018/10/23
 * @Description:
 */
@Configuration
public class LettuceRedisConfig {

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout:200}")
    private long timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWait;

    /**
     * 链接池获取方法
     * @param maxIdle
     * @param minIdle
     * @param maxActive
     * @param maxWait
     * @return
     */
    public static GenericObjectPoolConfig getGenericObjectPoolConfig(int maxIdle, int minIdle, int maxActive, long maxWait) {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        return genericObjectPoolConfig;
    }

    public static LettuceConnectionFactory getLettuceConnectionFactory(int database, String host, int port, String password, long timeout,
                                                                       GenericObjectPoolConfig genericObjectPoolConfig) {
        // 单机版配置
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        // 集群版配置
        /**RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
         String[] serverArray = clusterNodes.split(",");
         Set<RedisNode> nodes = new HashSet<RedisNode>();
         for (String ipPort : serverArray) {
         String[] ipAndPort = ipPort.split(":");
         nodes.add(new RedisNode(ipAndPort[0].trim(), Integer.valueOf(ipAndPort[1])));
         }
         redisClusterConfiguration.setPassword(RedisPassword.of(password));
         redisClusterConfiguration.setClusterNodes(nodes);
         redisClusterConfiguration.setMaxRedirects(maxRedirects);**/

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig)
                .build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration,clientConfig);
    }

    @Bean(name = {"lettuceConnectionFactory", "reactiveRedisConnectionFactory"})
    public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        return getLettuceConnectionFactory(database, host, port, password, timeout, genericObjectPoolConfig);
    }

    /**
     * GenericObjectPoolConfig 连接池配置
     *
     * @return
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        return getGenericObjectPoolConfig(maxIdle, minIdle, maxActive, maxWait);
    }

}
