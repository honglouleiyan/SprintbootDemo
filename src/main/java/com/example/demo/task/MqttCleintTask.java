package com.example.demo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MqttCleintTask {
//    @Resource(name = "redisTemplate2")
//    private RedisTemplate redisTemplate2;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;


//    @Scheduled(cron = "0 0/30 * * * ?")
    @Scheduled(cron = "0/60 * * * * ?")
    public void showMqttSubscribe() {
        try {
            for(int i = 0; i < 100; i++) {
                redisTemplate.opsForValue().set(i+"",i+"");
                System.out.println("获取的值为:"+redisTemplate.opsForValue().get(i+""));
            }
        } catch (Exception e) {
            log.error("showMqttSubscribe error:",e);
        }

        log.info("end showMqttSubscribe task execing ...");

    }

}