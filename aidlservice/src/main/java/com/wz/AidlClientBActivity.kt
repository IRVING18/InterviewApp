package com.wz

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.wz.aidlservice.MyAidlService
import com.wz.aidlservice.databinding.AidlActivityTestBinding
import com.wz.base.IMessageCallback
import com.wz.base.IMsgAidlInterface
import com.wz.base.MsgBean
import com.wz.base.adapter.MsgAdapter
import com.wz.base.consts.SendConsts

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/26 11:50
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class AidlClientBActivity : AppCompatActivity() {
    private val mBinding by lazy {
        AidlActivityTestBinding.inflate(layoutInflater)
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
        mBinding.btBindService.setOnClickListener {
            //通过action隐式去绑定service
            val intent = Intent(this, MyAidlService::class.java)
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
        }
        mBinding.btSend.setOnClickListener {
            val msg = MsgBean().also {
                it.msg = mBinding.etSend.text.toString()
            }
            mAdapter.add(msg.apply {
                type = 1 //用于RecyclerView展示，自己发的在右侧
            })
            //将数据发送到远程服务
            mIMsgAIDLInterface?.sendMessage(msg, SendConsts.CLIENT_ID_B)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //关闭服务
        unbindService(mServiceConnection)
        mIMsgAIDLInterface = null
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
                mIMsgAIDLInterface?.registerCallback(mMsgCallback, SendConsts.CLIENT_ID_B)
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