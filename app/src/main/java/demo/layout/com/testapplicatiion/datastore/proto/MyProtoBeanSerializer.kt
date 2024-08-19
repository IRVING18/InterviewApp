package demo.layout.com.testapplicatiion.datastore.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream


/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 17:22
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
object MyProtoBeanSerializer:Serializer<MyProtoBean> {
    override val defaultValue: MyProtoBean
        get() = MyProtoBean.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MyProtoBean {
        try {
            return MyProtoBean.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: MyProtoBean, output: OutputStream) {
        t.writeTo(output)
    }
}