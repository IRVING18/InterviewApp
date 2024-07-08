package demo.layout.com.testapplicatiion.servicetest.start

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyStartService : Service() {
    companion object {
        const val TAG = "MyStartService"
    }

    /**
     * 服务第一次创建时调用。
     */
    override fun onCreate() {
        super.onCreate()
        Log.e("wzzz${TAG}", "onCreate()");
    }

    /**
     * 服务销毁时调用。回收不再使用的资源。
     *
     */
    override fun onDestroy() {
        Log.e("wzzz${TAG}", "onDestroy()");
        super.onDestroy()
    }

    /**
     * startService方式，才会走的方法；bindService不走这个
     * 每次启动服务时调用。服务一旦启动就立即去执行某个操作的代码逻辑现在这里
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("wzzz${TAG}", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId)
    }


    private var mIBinder: IBinder = object : Binder() {}

    /**
     * 提供给bind形式的，这个示例不用这个，直接随便反了一个；
     */
    override fun onBind(intent: Intent): IBinder {
        return mIBinder
    }
}