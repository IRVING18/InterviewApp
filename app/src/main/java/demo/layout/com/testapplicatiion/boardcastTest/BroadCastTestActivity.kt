package demo.layout.com.testapplicatiion.boardcastTest

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import demo.layout.com.testapplicatiion.R

class BroadCastTestActivity : AppCompatActivity() {
    companion object {
        const val TAG = "BoardCastTestActivity"
    }

    val myDynamicBroadCast : MyDynamicBroadCast by lazy {
        MyDynamicBroadCast()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_cast_test)

        findViewById<View>(R.id.btSendStr).setOnClickListener {
            val intent = Intent("com.wz.test.MyTestBroadCast")
            intent.putExtra("wz", "通过广播传输数据")
            Log.e("wzzz","发送广播数据")
            sendBroadcast(intent)
        }
        findViewById<View>(R.id.btSendOrderStr).setOnClickListener {
            val intent = Intent("com.wz.test.MyTestBroadCast")
            intent.putExtra("wz", "通过广播传输有序数据")
            Log.e("wzzz","发送有序广播数据")
            sendOrderedBroadcast(intent, null)
        }
        findViewById<View>(R.id.btSendStickyStr).setOnClickListener {
            val intent = Intent("com.wz.test.MyTestBroadCast")
            intent.putExtra("wz", "粘性数据")
            Log.e("wzzz","发送粘性广播")
//            sendStickyOrderedBroadcast(intent)
        }
        findViewById<View>(R.id.btRegisterBroad).setOnClickListener {
            val intentFilter = IntentFilter("com.wz.test.MyTestBroadCast")
            registerReceiver(myDynamicBroadCast, intentFilter)
        }
        findViewById<View>(R.id.btUnRegisterBroad).setOnClickListener {
            unregisterReceiver(myDynamicBroadCast)
        }


    }
}