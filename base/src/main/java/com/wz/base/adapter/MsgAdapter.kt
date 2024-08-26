package com.wz.base.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.wz.base.MsgBean
import com.wz.base.R
import com.wz.base.databinding.ItemMsgBinding

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/26 16:43
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MsgAdapter(list: List<MsgBean>) :
    BaseQuickAdapter<MsgBean, DataBindingHolder<ItemMsgBinding>>(list) {
    private lateinit var mBinding: ItemMsgBinding

    override fun onBindViewHolder(
        holder: DataBindingHolder<ItemMsgBinding>,
        position: Int,
        item: MsgBean?
    ) {
        mBinding = holder.binding ?: return
        if (item?.type == 1) {
            mBinding.tv.text = "我：${item?.msg}"
            mBinding.tv.setTextColor(Color.RED)
            mBinding.tv.gravity = Gravity.RIGHT
        } else {
            mBinding.tv.text = "另一个APP：${item?.msg}"
            mBinding.tv.setTextColor(Color.BLUE)
            mBinding.tv.gravity = Gravity.LEFT
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<ItemMsgBinding> {
        return DataBindingHolder(R.layout.item_msg, parent)
    }

}