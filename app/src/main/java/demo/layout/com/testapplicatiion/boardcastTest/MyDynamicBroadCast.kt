package demo.layout.com.testapplicatiion.boardcastTest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * 有道领世
 * TestApplicatiion
 * Description: 自定义广播，测试进程间通信；用于动态注册
 * Created by wangzheng on 2024/7/11 11:02
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MyDynamicBroadCast : BroadcastReceiver() {
    companion object {
        val WZ_RECEIVER = "com.wz.test.MyTestBroadCast"
    }

    override fun onReceive(context: Context, intent: Intent) {
        //判断是否为某个广播，如果只注册一个Action，其实不写也没关系；但是如果注册了多个action就需要区分下了；
        if (intent.action == WZ_RECEIVER) {
            val str = intent.getStringExtra("com/wz")
            Log.e("wzzzzMyDynamicBroadCast", "动态注册-接收：$str")
        }
    }
}