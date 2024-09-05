package demo.layout.com.testapplicatiion.motionlayoutcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import demo.layout.com.testapplicatiion.motionlayoutcompose.consts.ComposeConsts
import demo.layout.com.testapplicatiion.motionlayoutcompose.inter.ComposeFunc
import demo.layout.com.testapplicatiion.motionlayoutcompose.theme.MyCollapsingToolbarWithMotionComposeTheme

/**
 * 有道领世
 * TestApplicatiion
 * Description: 测试Motionlayout-Compose
 * Created by wangzheng on 2024/9/5 14:55
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MotionComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motionDemoName =
            intent.extras?.getString(ComposeConsts.MOTION_COMPOSE_DEMO_NAME) ?: finish()
        val cFunc: ComposeFunc? = ComposeConsts.cmap.find { it.toString() == motionDemoName }
        if (cFunc == null) finish()
        setContent {
            MyCollapsingToolbarWithMotionComposeTheme {
                //todo 沉浸式布局怎么搞？
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0E7FC)
                ) {
                    cFunc?.Run()
                }
            }
        }
    }

}