package demo.layout.com.testapplicatiion.datastore.proto

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 16:02
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class ProtoActivity : BaseActivity() {
    val myProtoViewModel by lazy {
        ViewModelProvider(this).get(MyProtoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proto)
        var i = 0
        findViewById<View>(R.id.bt_set).setOnClickListener {
            myProtoViewModel.setValue(
                MyProtoBean.newBuilder()
                    .setName("hhh${i++}")
                    .build()
            )
        }
        findViewById<View>(R.id.bt_set2).setOnClickListener {
            myProtoViewModel.setValue("hhh${i++}", MyProtoBean.MyType.NONE)
        }
        val tv = findViewById<TextView>(R.id.tv)
        val tv2 = findViewById<TextView>(R.id.tv2)
        myProtoViewModel.myProtoLiveData.observe(this) {
            tv.setText(it.name)
        }

        findViewById<View>(R.id.bt_get).setOnClickListener {
            //主动获取的方式
            lifecycleScope.launch {
                //在IO线程获取
                val myProtoBean = withContext(Dispatchers.IO) {
                    ProtoHelper.getValue()
                }
                tv2.setText("${myProtoBean?.name}")
            }
        }
    }
}