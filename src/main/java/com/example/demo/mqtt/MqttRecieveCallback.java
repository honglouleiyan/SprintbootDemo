package com.example.demo.mqtt;

import com.qq.welink.security.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/30
 * @time: 16:11
 * @description:
 **/
@Slf4j
public class MqttRecieveCallback implements MqttCallback {



    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");
        System.out.println(cause.toString());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {


        System.out.println("Client 接收消息主题 : " + topic);
        System.out.println("Client 接收消息Qos : " + message.getQos());
        System.out.println("Client 接收消息内容 : " + new String(message.getPayload()));
        String result = new String(message.getPayload());
        log.info("messageArrived message: " + result);
        } catch (Exception e) {
            log.error("解析异常:",e);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }


}
