package com.example.demo.callback;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/24
 * @time: 17:50
 * @description:
 **/
public class People {
    Printer printer = new Printer();

    /*
     * 同步回调
     */
    public void goToPrintSyn(Callback callback, String text) {
        printer.print(callback, text);
    }


    public static void main(String[] args) {
        People people = new People();
        Callback callback = new Callback() {
            @Override
            public void printFinished(String msg) {
                System.out.println("打印机告诉我的消息是 ---> " + msg);
            }
        };
        System.out.println("需要打印的内容是 ---> " + "打印一份简历");
        people.goToPrintSyn(callback, "打印一份简历");
        System.out.println("我在等待 打印机 给我反馈");
    }
}
