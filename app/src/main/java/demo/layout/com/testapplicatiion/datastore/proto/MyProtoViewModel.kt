package demo.layout.com.testapplicatiion.datastore.proto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 15:46
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MyProtoViewModel : ViewModel() {

    //当写入数据时，能自动监听数据变化
    val myProtoLiveData = ProtoHelper.getValueAsFlow().asLiveData()

    fun setValue(myProtoBean: MyProtoBean) {
        viewModelScope.launch {
            ProtoHelper.setValue(myProtoBean)
        }
    }

    fun setValue(name: String, type: MyProtoBean.MyType) {
        viewModelScope.launch {
            ProtoHelper.setValue(name, type)
        }
    }
}