package com.example.demo.task;

import com.example.demo.config.Massage;
import com.example.demo.config.MqPullConsumer;
import com.example.demo.config.MqPullConsumer2;
import com.example.demo.config.MyCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lvyl
 * @date 2019/3/9 14:25
 * @description
 */
@Component
@Slf4j
public class ApplicationReadyListener implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Massage massage = new Massage(new MyCallback());
        MqPullConsumer2 consumer2 = new MqPullConsumer2(massage);
        log.info("application init complete");
    }
}
