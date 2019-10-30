package com.example.demo.config;

import com.example.demo.callback.Callback;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/24
 * @time: 19:27
 * @description:
 **/
public class MyCallback implements Callback {
    @Override
    public void printFinished(String msg) {
        System.out.println(msg);
    }
}
