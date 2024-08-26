package com.wz.base;

import com.wz.base.MsgBean;
import com.wz.base.IMessageCallback;

interface IMsgAidlInterface {
    //发送消息
    void sendMessage(in MsgBean msg, int sendId);
    //注册回调，用于接收消息
    void registerCallback(in IMessageCallback callback, int sendId);
}