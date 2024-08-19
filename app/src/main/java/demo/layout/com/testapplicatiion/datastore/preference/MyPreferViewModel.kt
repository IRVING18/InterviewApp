package demo.layout.com.testapplicatiion.datastore.preference

import androidx.datastore.preferences.core.intPreferencesKey
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
class MyPreferViewModel : ViewModel() {

    //当写入数据时，能自动监听数据变化
    val myPreferencesLiveData =
        PreferencesHelper.getValueAsFlow(intPreferencesKey(PreferencesContants.KEY_VM_TEST_INT))
            .asLiveData()

    fun setPreferencesValue(value: Int) {
        viewModelScope.launch {
            PreferencesHelper.setValue(intPreferencesKey(PreferencesContants.KEY_VM_TEST_INT), value)
        }
    }
}