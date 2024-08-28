package demo.layout.com.testapplicatiion.bigimg

import android.content.Intent
import android.os.Bundle
import demo.layout.com.testapplicatiion.TestActivity
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.databinding.ActivityBigImgTestBinding

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/28 11:39
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class BigImgTestActivity : BaseActivity() {

    private lateinit var mBinding: ActivityBigImgTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBigImgTestBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.bt1.setOnClickListener {
            startActivity(Intent(this, SubsamplingActivity::class.java))
        }

        mBinding.bt2.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }

    }
}