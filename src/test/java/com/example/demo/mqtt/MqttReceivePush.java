package com.example.demo.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.qq.welink.security.sm4.SM4Utils;
import com.qq.welink.security.util.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

/**
 * @author heaven
 */
@Slf4j
@Data
public class MqttReceivePush {

    public String host;
    public String clientId;
    public String receiveTopicId;
    public String pushTopicId;
    MemoryPersistence memoryPersistence = null;
    private String userName;
    private String passWord;

    public MqttClient client;

    private String sm4Key;
    private String sm4Iv;

    public MqttMessage message;
    public MqttTopic topic;

    public String random;


    public MqttReceivePush(String host, String userName, String passWord, String clientId, String receiveTopicId, String pushTopicId) throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        this.host = host;
//        this.userName = userName;
//        this.passWord = passWord;
        this.clientId = clientId;
        this.receiveTopicId = receiveTopicId;
        this.pushTopicId = pushTopicId;
        this.random = String.valueOf(new Random().nextInt(65535));
        memoryPersistence = new MemoryPersistence();
        // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(host, clientId, new MemoryPersistence());
        log.info("创建mqtt连接,"+clientId);
    }


    public void start() throws MqttException {

            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
//            options.setUserName(userName);
            // 设置连接的密码
//            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(60);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            options.setAutomaticReconnect(true);
            // 设置回调
//            client.setCallback(new PushCallback());
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//            options.setWill(topic, "close".getBytes(), 2, true);
//            client.connect(options);
            topic = client.getTopic(pushTopicId);
    }


    public void publish(MqttTopic topic, MqttMessage message) throws MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        log.info("message is published completely! " + token.isComplete() + ",message:{}", Util.byteToHex(message.getPayload()));
    }

    public void closeConnect() {
//		关闭连接
        if (null != client) {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                    client.close();
                } catch (MqttException e) {
                    log.error("MqttPushClient closeConnect MqttException:", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            MqttReceivePush client = new MqttReceivePush("tcp://118.89.38.157:1883","","","clientid","push","push");
            client.start();
            WeilinkMqttReveiveModel model = new WeilinkMqttReveiveModel();
            model.setDatapoint(10001001);
            for(int i = 0;i < 10; i++) {
                model.setDatapoint(10011+i);
                log.info("gwReportSubAttr info,{}", model);
                byte[] encryptData = SM4Utils.encryptDataCBCByte(JSONObject.toJSONString(model).getBytes(), "m416hDGquERmUblB", "1234567890ABCDEF");
                log.info("密文: " + Util.byteToHex(encryptData));
                client.message = new MqttMessage();
                client.message.setQos(1);
                client.message.setRetained(true);
                client.message.setPayload(encryptData);
                client.publish(client.topic, client.message);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}