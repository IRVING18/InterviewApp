package com.wz.base;

import com.wz.base.MsgBean;

interface IMessageCallback {
    //回发消息
    void onMessageReceive(in MsgBean msg);
}