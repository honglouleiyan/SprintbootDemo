package com.example.demo.config;

import org.apache.rocketmq.client.consumer.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/24
 * @time: 15:50
 * @description:
 **/
public class MqPullConsumer2 {

    Massage massage;

    public MqPullConsumer2(Massage massage){
        massage = massage;
    }

    public void getConsumer() throws MQClientException {
        //消费组
        final MQPullConsumerScheduleService scheduleService = new MQPullConsumerScheduleService("GroupName1");
        //MQ NameService地址
        scheduleService.getDefaultMQPullConsumer().setNamesrvAddr("10.0.14.79:9876;10.0.14.80:9876");
        //负载均衡模式
        scheduleService.setMessageModel(MessageModel.CLUSTERING);
        //需要处理的消息topic
        scheduleService.registerPullTaskCallback("device_7253724c55d0357142721288",massage);

        scheduleService.start();
    }
}
