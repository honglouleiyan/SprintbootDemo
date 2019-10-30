package com.example.demo.callback;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/24
 * @time: 17:49
 * @description:
 **/
public class Printer {
    public void print(Callback callback, String text) {
        System.out.println("正在打印 . . . ");
        callback.printFinished("打印完成:" + text);
    }
}
