package com.example.demo.config;

import com.example.demo.callback.Callback;
import com.example.demo.callback.CallbackFor;
import com.example.demo.callback.Printer;
import org.apache.rocketmq.client.consumer.MQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullTaskCallback;
import org.apache.rocketmq.client.consumer.PullTaskContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/24
 * @time: 16:37
 * @description:
 **/
public class Massage implements PullTaskCallback {

    private Callback callback;

    public Massage(Callback callback) {
        this.callback = callback;
    }

    Printer printer = new Printer();

    @Override
    public void doPullTask(MessageQueue mq, PullTaskContext context) {
        MQPullConsumer consumer = context.getPullConsumer();
        try {

            long offset = consumer.fetchConsumeOffset(mq, false);
            if (offset < 0) {
                offset = 0;
            }

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
                        printer.print(callback, msgBody);
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

}
