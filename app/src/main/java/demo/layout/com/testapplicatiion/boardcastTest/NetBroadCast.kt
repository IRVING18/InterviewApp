package demo.layout.com.testapplicatiion.boardcastTest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

/**
 * 有道领世
 * TestApplicatiion
 * Description: 静态注册广播示例
 * Created by wangzheng on 2024/7/11 11:02
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class NetBroadCast : BroadcastReceiver() {
    companion object {
        val NET_RECEIVER = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        //是否为网络状态变化广播，如果只注册一个Action，其实不写也没关系；但是如果注册了多个action就需要区分下了；
        if (intent.action == NET_RECEIVER) {
            //获得网络连接服务
            val connManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return
            // 获取WIFI网络连接状态
            val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            // 判断是否正在使用WIFI网络
            if (NetworkInfo.State.CONNECTED == wifiInfo?.state) {
                Log.i("wzzzzconn--------", "wifi")
            }

            //如果不是断开wifi，则是在使用数据网络
            val mobileInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            // 判断是否正在使用移动网络
            if (NetworkInfo.State.CONNECTED == mobileInfo?.state) {
                Log.i("wzzzzconn--------", "mobile")
            }
        }
    }
}