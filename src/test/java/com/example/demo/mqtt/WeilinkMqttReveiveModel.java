package com.example.demo.mqtt;

import lombok.Data;

/**
 * @Author: heaven
 * @Date: 2019/8/6 12:00
 * @Version 1.0
 */
@Data
public class WeilinkMqttReveiveModel extends WeilinkMqttModel {
    private String value;
    private String subDin;
}
