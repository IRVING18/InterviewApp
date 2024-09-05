package demo.layout.com.testapplicatiion.motionlayoutcompose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
class MotionMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCollapsingToolbarWithMotionComposeTheme {
                //todo 沉浸式布局怎么搞？
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0E7FC)
                ) {
                    ComposableMenu(list = ComposeConsts.cmap) { composeFunc ->
                        goToActivity(composeFunc)
                    }
                }
            }
        }
    }

    private fun goToActivity(composeFunc: ComposeFunc) {
        Log.v("MAIN", " launch $composeFunc")
        val intent = Intent(this, MotionComposeActivity::class.java)
        intent.putExtra(ComposeConsts.MOTION_COMPOSE_DEMO_NAME, composeFunc.toString())
        startActivity(intent)
    }

    @Composable
    fun ComposableMenu(list: List<ComposeFunc>, onClick: (act: ComposeFunc) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            list.forEach { composeFunc ->
                Button(onClick = { onClick(composeFunc) }) {
                    Text(composeFunc.toString(), modifier = Modifier.padding(2.dp))
                }
            }
        }
    }


}