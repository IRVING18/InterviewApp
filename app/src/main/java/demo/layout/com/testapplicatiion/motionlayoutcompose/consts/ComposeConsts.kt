package demo.layout.com.testapplicatiion.motionlayoutcompose.consts

import androidx.compose.runtime.Composable
import demo.layout.com.testapplicatiion.motionlayoutcompose.demo.MoonToolbarDsl
import demo.layout.com.testapplicatiion.motionlayoutcompose.demo.ToolBarLazyExampleDsl
import demo.layout.com.testapplicatiion.motionlayoutcompose.inter.ComposeFunc

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/9/5 15:02
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
object ComposeConsts {
    const val MOTION_COMPOSE_DEMO_NAME = "MOTION_COMPOSE_FUC"

    val cmap = listOf(
//        get("CollapsingToolbar DSL") { ToolBarExampleDsl() },
//        get("CollapsingToolbar JSON") { ToolBarExample() },
        get("ToolBarLazyExample DSL") { ToolBarLazyExampleDsl() },
        get("和view相对的 MoonToolbarDsl") { MoonToolbarDsl() },
//        get("ToolBarLazyExample JSON") { ToolBarLazyExample() },
//        get("MotionInLazyColumn Dsl") { MotionInLazyColumnDsl() },
//        get("MotionInLazyColumn JSON") { MotionInLazyColumn() },
//        get("DynamicGraph") { ManyGraphs() },
//        get("ReactionSelector") { ReactionSelector() },
//        get("MotionPager") { MotionPager() },
//        get("Puzzle") { Puzzle() },
//        get("MPuzzle") { MPuzzle() },
//        get("FlyIn") { M1FlyIn() },
//        get("DragReveal") { M2DragReveal() },
//        get("MultiState") { M3MultiState() },
    )


    fun get(name: String, cRun: @Composable () -> Unit): ComposeFunc {
        return object : ComposeFunc {
            @Composable
            override fun Run() {
                cRun()
            }

            override fun toString(): String {
                return name
            }
        }
    }

}