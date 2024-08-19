package demo.layout.com.testapplicatiion.datastore.preference

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.datastore.preferences.core.intPreferencesKey
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
class PreferencesActivity: BaseActivity() {
    val myPreferViewModel by lazy {
        ViewModelProvider(this).get(MyPreferViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        var i = 0
        findViewById<View>(R.id.bt_set).setOnClickListener {
            myPreferViewModel.setPreferencesValue(i ++)
        }
        val tv = findViewById<TextView>(R.id.tv)
        val tv2 = findViewById<TextView>(R.id.tv2)
        myPreferViewModel.myPreferencesLiveData.observe(this) {
            tv.setText("$it")
        }

        findViewById<View>(R.id.bt_get).setOnClickListener {
            //主动获取的方式
            lifecycleScope.launch {
                //在IO线程获取
                val str = withContext(Dispatchers.IO) {
                    PreferencesHelper.getValue(intPreferencesKey(PreferencesContants.KEY_VM_TEST_INT))
                }
                tv2.setText("$str")
            }
        }
    }
}