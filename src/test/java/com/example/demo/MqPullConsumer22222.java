package com.example.demo;

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
 * @time: 15:22
 * @description:
 **/
public class MqPullConsumer22222 {
    public static void main(String[] args) throws MQClientException {
        //消费组
        final MQPullConsumerScheduleService scheduleService = new MQPullConsumerScheduleService("GroupName1");
        //MQ NameService地址
        scheduleService.getDefaultMQPullConsumer().setNamesrvAddr("10.0.14.79:9876;10.0.14.80:9876");
        //负载均衡模式
        scheduleService.setMessageModel(MessageModel.CLUSTERING);
        //需要处理的消息topic
        scheduleService.registerPullTaskCallback("device_7253724c55d035714272128822", new PullTaskCallback() {

            @Override
            public void doPullTask(MessageQueue mq, PullTaskContext context) {
                MQPullConsumer consumer = context.getPullConsumer();
                try {

                    long offset = consumer.fetchConsumeOffset(mq, false);
                    if (offset < 0)
                        offset = 0;

                    PullResult pullResult = consumer.pull(mq, "*", offset, 32);
//                    System.out.printf("%s%n", offset + "\t" + mq + "\t" + pullResult);
                    switch (pullResult.getPullStatus()) {
                        case FOUND:
                            List<MessageExt> msgList = pullResult.getMsgFoundList();
                            if(CollectionUtils.isEmpty(msgList)){
                                return;
                            }
                            for(MessageExt msg : msgList){
                                /*推送消息处理*/
                                String msgBody = new String(msg.getBody(), Charset.forName("utf-8"));
                                System.out.printf("%s%n", offset + "\t" + mq + "\t" + pullResult + "\t" + msgBody);
                            }
                            break;
                        case NO_MATCHED_MSG:
                            break;
                        case NO_NEW_MSG:
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                    consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());


                    context.setPullNextDelayTimeMillis(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scheduleService.start();
    }

}
