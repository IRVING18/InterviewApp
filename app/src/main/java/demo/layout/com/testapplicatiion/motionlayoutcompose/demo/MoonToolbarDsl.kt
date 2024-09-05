package demo.layout.com.testapplicatiion.motionlayoutcompose.demo

import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Arc
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.Visibility
import demo.layout.com.testapplicatiion.R

/**
 * 和Motionlayout view版本，用Compose写一遍熟悉熟悉
 */
@OptIn(ExperimentalMotionApi::class)
@Preview(group = "scroll", device = "spec:shape=Normal,width=480,height=800,unit=dp,dpi=440")
@Composable
fun MoonToolbarDsl() {
    //顶部最大、最小的高度
    val maxHeight = 250.dp
    val minHeight = 80.dp
    //顶部限位高度
    val maxSpacerTopHeight = 400.dp

    val scene = MotionScene {
        //1、通过dsl方式获取各控件id
        val spacerId = createRefFor("spacer_id")//通过顶部限位，实现背景图片和滚动方向的视差效果
        val imageBg = createRefFor("image_bg_id")
        val imageMoon = createRefFor("image_moon_id")
        val titleHint = createRefFor("title_hint_id")

        //2、初始状态约束
        val start1 = constraintSet {
            //通过顶部限位，实现背景图片和滚动方向的视差效果
            constrain(spacerId) {
                bottom.linkTo(parent.top)
                height = Dimension.value(maxSpacerTopHeight)
            }
            //背景图片
            constrain(imageBg) {
                width = Dimension.matchParent
                height = Dimension.value(maxHeight + maxSpacerTopHeight)
                top.linkTo(spacerId.top)
            }
            //月亮初始位置在左下角
            constrain(imageMoon) {
                bottom.linkTo(parent.bottom, 0.dp)
                start.linkTo(parent.start, 0.dp)
                customDistance("myPaddingBottom", Dp(0f))
                customColor("myColorFilter", Color(0xFFffffff))
            }
            //隐藏文案在左下角
            constrain(titleHint) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                alpha = 0f
            }
        }
        //2、设置结束位置约束
        val end1 = constraintSet {
            constrain(spacerId) {
                bottom.linkTo(parent.top)
                height = Dimension.value(0.dp)
            }
            //背景图，
            constrain(imageBg) {
                width = Dimension.matchParent
                height = Dimension.value(minHeight)
                top.linkTo(spacerId.top)
            }
            //隐藏文案展示
            constrain(titleHint) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                alpha = 1f
            }
            //月亮放到右下角
            constrain(imageMoon) {
                bottom.linkTo(parent.bottom, 0.dp)
                end.linkTo(parent.end, 0.dp)
                customDistance("myPaddingBottom", Dp(0f))
                customColor("myColorFilter", Color(0xFFff00ff))
            }
        }
        //4、关联start、end约束，并设置中间过程
        transition(start1, end1, "default") {
            //设置月亮
            keyAttributes(imageMoon) {
                //颜色渐变 、位移、旋转
                //注意这些自定义参数，
                // 1、每个进度上都得有，要不就报错；如果有些是只想在start、end设置，这里就都别写也行
                // 2、在start、end约束上也得有，要不就报错
                frame(0) {
                    customDistance("myPaddingBottom", Dp(0f))
                    customFloat("myRotate",0f)
                }
                frame(25) {
                    //向上移动25dp
                    customDistance("myPaddingBottom", Dp(25f))
                    customFloat("myRotate",-180f)
                }
                frame(50) {
                    customDistance("myPaddingBottom", Dp(50f))
                    customFloat("myRotate",-360f)
                }
                frame(75) {
                    customDistance("myPaddingBottom", Dp(25f))
                    customFloat("myRotate",-540f)
                }
                frame(100) {
                    customDistance("myPaddingBottom", Dp(0f))
                    customFloat("myRotate",-720f)
                }
            }
            //设置隐藏文案变化
            keyAttributes(titleHint) {
                frame(0) {
                    alpha = 0f
                }
                frame(100) {
                    alpha = 1f
                }
            }
        }
    }

    val maxPx = with(LocalDensity.current) { maxHeight.roundToPx().toFloat() }
    val minPx = with(LocalDensity.current) { minHeight.roundToPx().toFloat() }
    val toolbarHeight = remember { mutableStateOf(maxPx) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val height = toolbarHeight.value

                if (height + available.y > maxPx) {
                    toolbarHeight.value = maxPx
                    return Offset(0f, maxPx - height)
                }

                if (height + available.y < minPx) {
                    toolbarHeight.value = minPx
                    return Offset(0f, minPx - height)
                }

                toolbarHeight.value += available.y
                return Offset(0f, available.y)
            }
        }
    }

    val progress = 1 - (toolbarHeight.value - minPx) / (maxPx - minPx)

    Column {
        MotionLayout(
            motionScene = scene,
            progress = progress,
        ) {
            Spacer(modifier = Modifier.layoutId("spacer_id"))
            Image(
                modifier = Modifier
                    .layoutId("image_bg_id"),
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .layoutId("image_moon_id")
                    .padding(bottom = customDistance("image_moon_id", "myPaddingBottom"))
                    .rotate(customFloat("image_moon_id", "myRotate"))
                ,
                painter = painterResource(R.drawable.ic_moon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(customColor("image_moon_id","myColorFilter"))
            )
            Text(
                modifier = Modifier.layoutId("title_hint_id"),
                text = "隐藏文案",
                fontSize = 30.sp,
                color = Color.White
            )
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection)
        ) {
            items(100) {
                Text(text = "item $it", modifier = Modifier.padding(4.dp))
            }
        }
    }
}
