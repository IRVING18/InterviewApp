package demo.layout.com.testapplicatiion.motionlayout

import android.os.Bundle
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.base.BaseActivity

/**
 * 有道领世
 * TestApplicatiion
 * Description: 系统提供的手势滑动方式
 * Created by wangzheng on 2024/8/24 10:58
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class TouchScrollActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_touch_scroll)
    }
}