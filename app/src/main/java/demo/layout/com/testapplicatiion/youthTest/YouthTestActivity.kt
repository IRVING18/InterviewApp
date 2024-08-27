package demo.layout.com.testapplicatiion.youthTest

import android.os.Bundle
import cn.youth.flowervideo.view.timer.FlyView
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.databinding.ActivityYouthTestBinding
import demo.layout.com.testapplicatiion.utils.UIUtils
import demo.layout.com.testapplicatiion.view.flyView.FlyWindowHelper

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/27 16:59
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class YouthTestActivity: BaseActivity() {
    private lateinit var mBinding : ActivityYouthTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityYouthTestBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.btFly.setOnClickListener {
            FlyWindowHelper.flyTo(
                this@YouthTestActivity,
                UIUtils.getCenterFrame(mBinding.vStart),
                UIUtils.getCenterFrame(mBinding.btFly),
                listener = object :FlyWindowHelper.FlyListener {
                    override fun onStart() {
                    }

                    override fun onEnd() {
                    }
                }
            )
        }

    }
}