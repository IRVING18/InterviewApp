package com.wz.aidlservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wz.base.IMessageCallback
import com.wz.base.IMsgAidlInterface
import com.wz.base.MsgBean
import com.wz.base.consts.SendConsts


/**
 * TestApplicatiion
 * Description: AIDL 服务测试，是消息分发服务，sendMessage()发送数据至此，然后通过双方的callback，将msg分发出去
 * Created by wangzheng on 2024/8/26 14:10
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MyAidlService : Service() {
    private var mCallbackA: IMessageCallback? = null
    private var mCallbackB: IMessageCallback? = null

    /**
     * 这个服务就是等于一个中介，只转发数据
     */
    private val mIBinder: IBinder = object : IMsgAidlInterface.Stub() {

        override fun sendMessage(msg: MsgBean?, sendId: Int) {
            //A发过来的，分发给B；B发过来，分发给A
            when (sendId) {
                SendConsts.CLIENT_ID_A -> mCallbackB?.onMessageReceive(msg)
                SendConsts.CLIENT_ID_B -> mCallbackA?.onMessageReceive(msg)
            }
        }

        override fun registerCallback(callback: IMessageCallback?, sendId: Int) {
            when (sendId) {
                SendConsts.CLIENT_ID_A -> mCallbackA = callback
                SendConsts.CLIENT_ID_B -> mCallbackB = callback
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("wzzzzzzzzzz", "MyAidlService OnBind")
        return mIBinder
    }

    override fun onCreate() {
        Log.e("wzzzzzzzzzz", "MyAidlService onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.e("wzzzzzzzzzz", "MyAidlService onDestroy")
        super.onDestroy()
    }
}