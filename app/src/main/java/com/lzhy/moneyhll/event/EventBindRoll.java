package com.lzhy.moneyhll.event;

/*****************************************************************************
 * created by xu on 2017/2/9.
 * 用途: 该类是处理扫描回传房车券信息的事件消息类 我们将要发送的消息就封装到该类里面
 *       使用时，请自写 自己的消息类
 ***************************************************************************/

public class EventBindRoll {
    private String number;//房车券
    private String numberCode;//房车券激活码

    public EventBindRoll(String number, String numberCode) {
        this.number = number;
        this.numberCode = numberCode;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberCode() {
        return numberCode;
    }
}
