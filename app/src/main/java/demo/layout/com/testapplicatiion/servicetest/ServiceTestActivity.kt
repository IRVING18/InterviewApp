package demo.layout.com.testapplicatiion.servicetest

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.servicetest.bind.MyBindService
import demo.layout.com.testapplicatiion.servicetest.start.MyStartService

class ServiceTestActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ServiceTestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_test)

        val startServiceIntent = Intent(this, MyStartService::class.java)
        findViewById<View>(R.id.btStartService).setOnClickListener {
            startService(startServiceIntent)
        }
        findViewById<View>(R.id.btStopService).setOnClickListener {
            stopService(startServiceIntent)
        }
        val bindServiceIntent = Intent(this, MyBindService::class.java)
        //多次bind，传入相同mServiceConnection时，没啥影响
        //1、Service只创建一次，onBind只执行一次
        //2、且只会和mServiceConnection建立联系
        //3、在unBind时，只需要取消mServiceConnection就会执行onDestroy；
        findViewById<View>(R.id.btBindService).setOnClickListener {
            bindService(startServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
        }
        //多次bind，并传入不同connection时，有影响
        //1、同样只创建Service一次，onBind一次
        //2、但是会和多个mServiceConnection、mServiceConnection2多个联系
        //3、在unBind时，多个connection都要unBind，否则Service不会走onDestroy()；
        //所以，多次绑定同一个Service并不会导致Service被多次创建或者多次调用onBind()方法，
        // 而是会为每个客户端创建一个单独的ServiceConnection。
        // 同时，解除绑定时也需要注意每个ServiceConnection都需要解除，否则Service不会被销毁。
        findViewById<View>(R.id.btBindService2).setOnClickListener {
            bindService(bindServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
            bindService(bindServiceIntent, mServiceConnection2, BIND_AUTO_CREATE)
        }
        findViewById<View>(R.id.btuUnBindService).setOnClickListener {
            //通过ServiceConnection，取消绑定
            unbindService(mServiceConnection)
            //有一种常见的误解是认为调用unbindService()会自动销毁binder对象，
            // 但实际上这并非如此。unbindService()只是解除了Activity和Service的绑定，
            // 但并没有销毁binder对象。因此，你需要手动将binder对象置为null，以释放对Service的引用。
            mDownBinder = null
        }
        //bind形式启动service，通过binder和service进行通信
        findViewById<View>(R.id.btBindDown).setOnClickListener {
            mDownBinder?.startDown()
        }
        findViewById<View>(R.id.btBindGet).setOnClickListener {
            mDownBinder?.getProgress()
        }
        findViewById<View>(R.id.btBindStop).setOnClickListener {
            mDownBinder?.stopDown()
        }
    }

    private var mDownBinder: MyBindService.DownloadBinder? = null

    private var mServiceConnection = object : ServiceConnection {
        /**
         * 这个方法的第二个参数就是Service中onBind()方法中返回的Binder对象；
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("wzzz${TAG}", "onServiceConnected")
            //转成DownloadBinder对象
            mDownBinder = service as? MyBindService.DownloadBinder
        }

        /**
         * 这个方法只在异常情况下执行，比如Service崩溃或者被强制杀死等。
         * （调用unBindService）或者停止Service（调用stopService或者stopSelf）时，不会调用
         */
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("wzzz${TAG}", "onServiceDisconnected")
        }
    }

    private var mServiceConnection2 = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("wzzz${TAG}", "onServiceConnected2")
            //转成DownloadBinder对象
        }

        /**
         * 这个方法只在异常情况下执行，比如Service崩溃或者被强制杀死等。
         * （调用unBindService）或者停止Service（调用stopService或者stopSelf）时，不会调用
         */
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("wzzz${TAG}", "onServiceDisconnected2")
        }
    }
}