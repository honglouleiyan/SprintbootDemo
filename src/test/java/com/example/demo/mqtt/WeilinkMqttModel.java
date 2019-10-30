package com.example.demo.mqtt;

import lombok.Data;

/**
 * @Author: heaven
 * @Date: 2019/8/6 11:58
 * @Version 1.0
 */
@Data
public class WeilinkMqttModel {

    private String din;
    private String msgType;
    private Long seq;
    private Integer random;
    private Long timeStamp;
    private Integer datapoint;
}
