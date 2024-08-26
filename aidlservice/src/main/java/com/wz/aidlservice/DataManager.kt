package com.wz.aidlservice

import com.wz.base.MsgBean


/**
 * 有道领世
 * TestApplicatiion
 * Description: 数据管理器
 * Created by wangzheng on 2024/8/26 14:15
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class DataManager private constructor() {

    //数据存储list中
    private val mData: MutableList<MsgBean> by lazy {
        mutableListOf()
    }

    fun add(myAIDLBean: MsgBean?) {
        myAIDLBean?.let {
            mData.add(it)
        }
    }

    fun get(id: Int): MsgBean? {
        return mData.find { it.id == id }
    }

    fun getAll(): List<MsgBean> {
        return mData
    }

    companion object {
        @Volatile
        private var INSTANCE: DataManager? = null;
        private var lock = Object()

        /**
         * 单例
         */
        fun getInstance(): DataManager {
            return INSTANCE ?: synchronized(lock) {
                INSTANCE?.let {
                    return it
                }
                val instance = DataManager()
                INSTANCE = instance
                instance
            }
        }
    }
}