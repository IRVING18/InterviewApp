package demo.layout.com.testapplicatiion.datastore.multiProcess

import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import demo.layout.com.testapplicatiion.base.BaseApplication
import demo.layout.com.testapplicatiion.datastore.proto.MyProtoBean
import demo.layout.com.testapplicatiion.datastore.proto.MyProtoBeanSerializer
import okio.Path.Companion.toPath
import java.io.File

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 19:30
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
object MultiDSHelper {
    val dataStore: DataStore<MyProtoBean> by lazy {
        MultiProcessDataStoreFactory.create(
            serializer = MyProtoBeanSerializer,
            produceFile = {
                File("${BaseApplication.application.dataStoreFile("myProtoBean.proto").absolutePath.toPath()}")
            }
        )
    }
}