package demo.layout.com.testapplicatiion.servicetest.bind

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import demo.layout.com.testapplicatiion.servicetest.start.MyStartService

class MyBindService : Service() {
    companion object {
        const val TAG = "MyBindService"
        var mProcess = 0
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

    private var mIBinder: IBinder = DownloadBinder()

    /**
     * 提供给bind形式的，会在ServiceConnection的生命周期的方法中的入参第二参数就是这个IBinder
     */
    override fun onBind(intent: Intent): IBinder {
        Log.e("wzzz${TAG}", "DownloadBinder.onBind()")
        return mIBinder
    }

    class DownloadBinder : Binder() {
        fun startDown() {
            Log.e("wzzz${TAG}", "DownloadBinder.startDown()")
        }

        fun getProgress(): Int {
            Log.e("wzzz${TAG}", "DownloadBinder.getProgress()")
            return mProcess
        }

        fun stopDown() {
            Log.e("wzzz${TAG}", "DownloadBinder.stopDown()")
        }
    }




    /**
     * startService方式，才会走的方法；bindService不走这个
     * 每次启动服务时调用。服务一旦启动就立即去执行某个操作的代码逻辑现在这里
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("wzzz${MyStartService.TAG}", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId)
    }

}