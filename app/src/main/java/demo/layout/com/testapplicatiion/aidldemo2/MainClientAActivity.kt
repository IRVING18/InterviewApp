package demo.layout.com.testapplicatiion.aidldemo2

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.wz.base.IMessageCallback
import com.wz.base.IMsgAidlInterface
import com.wz.base.MsgBean
import com.wz.base.adapter.MsgAdapter
import com.wz.base.consts.SendConsts
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.databinding.ActivityMyAidlTestBinding

/**
 * TestApplicatiion
 * Description: 主程序的Binder客户端，用于和MyAidlService通信；
 * Created by wangzheng on 2024/8/26 14:52
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MainClientAActivity : BaseActivity() {
    private val mBinding by lazy {
        ActivityMyAidlTestBinding.inflate(layoutInflater)
    }

    private var mIMsgAIDLInterface: IMsgAidlInterface? = null

    private val mAdapter by lazy {
        MsgAdapter(arrayListOf())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initRecyclerView()

        initView()
    }

    private fun initView() {
        mBinding.btStartApp.setOnClickListener {
            AppUtils.launchApp("com.wz.aidlservice")
        }
        mBinding.btBindService.setOnClickListener {
            //通过action隐式去绑定service
            val intent = Intent()
            intent.action = "com.wz.MyAidlService.intentFilter"//服务端的Manifest中的action
            intent.setPackage("com.wz.aidlservice")
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
//            val intent = Intent()
//            intent.component = ComponentName("com.wz.aidlservice", "com.wz.aidlservice.MyAidlService")
//            bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
        }
        mBinding.btSend.setOnClickListener {
            //将数据发送到远程服务
            val msg = MsgBean().also {
                it.msg = mBinding.etSend.text.toString()
            }
            mAdapter.add(msg.apply {
                type = 1 // 表示自己发送的，展示在左边
            })
            mIMsgAIDLInterface?.sendMessage(msg, SendConsts.CLIENT_ID_A)
        }
        mBinding.btClose.setOnClickListener {
            //关闭服务
            unbindService(mServiceConnection)
            mIMsgAIDLInterface = null
        }
    }


    private fun initRecyclerView() {
        mBinding.ry.layoutManager = LinearLayoutManager(this)
        mBinding.ry.adapter = mAdapter
    }

    private var mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            var iBinder = IMsgAidlInterface.Stub.asInterface(service)
            if (iBinder is IMsgAidlInterface) {
                ToastUtils.showLong("绑定成功！")
                mIMsgAIDLInterface = iBinder
                //注册回调Binder，用于接收回调数据
                mIMsgAIDLInterface?.registerCallback(mMsgCallback, SendConsts.CLIENT_ID_A)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    /**
     * 回调Binder，用于接收服务消息
     */
    private var mMsgCallback = object : IMessageCallback.Stub() {
        override fun onMessageReceive(msg: MsgBean?) {
            msg?.let {
                it.type = 0 //0表示远程数据，展示在左侧
                mAdapter.add(it)
            }
        }

    }
}