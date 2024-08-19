package demo.layout.com.testapplicatiion.datastore.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import demo.layout.com.testapplicatiion.base.BaseApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 10:52
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
object ProtoHelper {

    //创建DataStore Proto单例
    val Context.myProtoBeanDatastore: DataStore<MyProtoBean> by dataStore(
        fileName = "myProtoBean.proto",
        serializer = MyProtoBeanSerializer
    )

    /**
     * 设置数据
     */
    suspend fun setValue(name: String, type: MyProtoBean.MyType) {
        BaseApplication.application.myProtoBeanDatastore.updateData { myProtoBean ->
            myProtoBean.toBuilder()
                .setName(name)
                .setType(type)
                .build()
        }
    }

    /**
     * 全量更新
     */
    suspend fun setValue(myProtoBeanNew: MyProtoBean) {
        BaseApplication.application.myProtoBeanDatastore.updateData { myProtoBean ->
            return@updateData myProtoBeanNew
        }
    }

    /**
     * 同步获取数据
     */
    suspend fun getValue(): MyProtoBean? {
        return BaseApplication.application.myProtoBeanDatastore.data
            .firstOrNull()
    }

    /**
     * 通过flow获取
     */
    fun getValueAsFlow(): Flow<MyProtoBean> {
        return BaseApplication.application.myProtoBeanDatastore.data
    }
}