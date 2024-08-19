package demo.layout.com.testapplicatiion.datastore.multiProcess

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.datastore.proto.MyProtoBean
import demo.layout.com.testapplicatiion.datastore.proto.MyProtoViewModel
import demo.layout.com.testapplicatiion.datastore.proto.ProtoHelper
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
class MultiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)
        findViewById<View>(R.id.bt_start_service).setOnClickListener {
            val intent = Intent(this, DataStoreService::class.java)
            startService(intent)
        }

        val tv = findViewById<TextView>(R.id.tv)
        lifecycleScope.launch {
            MultiDSHelper.dataStore.data.collect{
                tv.setText("读取远程设置的数据：${it.name}")
            }
        }
    }
}