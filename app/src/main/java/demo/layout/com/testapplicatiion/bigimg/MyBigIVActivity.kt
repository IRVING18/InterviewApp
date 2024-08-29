package demo.layout.com.testapplicatiion.bigimg

import android.os.Bundle
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.databinding.ActivityMyBigivBinding

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/29 15:06
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MyBigIVActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMyBigivBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMyBigivBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val inputStream = resources.assets.open("qmsht.png")
        mBinding.myBigIV.setImage(inputStream)
    }
}