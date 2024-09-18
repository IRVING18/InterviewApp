package demo.layout.com.testapplicatiion.contraint

import android.os.Bundle
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.databinding.ActivityConstraintTestBinding

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/9/18 10:47
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class ConstraintTestActivity : BaseActivity() {
    private lateinit var mBinding: ActivityConstraintTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityConstraintTestBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}