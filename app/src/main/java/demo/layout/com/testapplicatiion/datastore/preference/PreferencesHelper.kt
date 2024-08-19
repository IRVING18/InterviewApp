package demo.layout.com.testapplicatiion.datastore.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import demo.layout.com.testapplicatiion.MyApp
import demo.layout.com.testapplicatiion.base.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 10:52
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
object PreferencesHelper {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_preferences")

    /**
     * 方式一：通过挂起函数写类似同步类型的获取方法
     */
    suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        val preferences = BaseApplication.application.dataStore.data
            .firstOrNull()
        return preferences?.get(key)
    }

    /**
     * 方式二：通过Flow方式获取，可以再配合ViewModel，将flow转成LiveData监听
     */
    fun getValueAsFlow(key: Preferences.Key<Int>): Flow<Int> {
        val res: Flow<Int> = BaseApplication.application.dataStore.data
            .map { preferences ->
                preferences[key] ?: 0
            }
        return res
    }

    //设置数据
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        BaseApplication.application.dataStore.edit { settings ->
            settings[key] = value
        }
    }

//    @JvmName(name = "getValueString")
//    suspend fun getValue(key: Preferences.Key<String>): String? {
//        val preferences = BaseApplication.application.dataStore.data
//            .firstOrNull()
//        return preferences?.get(key)
//    }
//    @JvmName(name = "getValueDouble")
//    suspend fun getValue(key: Preferences.Key<Double>): Double? {
//        val preferences = BaseApplication.application.dataStore.data
//            .firstOrNull()
//        return preferences?.get(key)
//    }

}