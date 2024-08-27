package demo.layout.com.testapplicatiion.view.flyView

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PointF
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import cn.youth.flowervideo.view.timer.FlyView
import demo.layout.com.testapplicatiion.base.BaseApplication
import demo.layout.com.testapplicatiion.utils.UiUtil
import demo.layout.com.testapplicatiion.view.evaluator.BezierThirdEvaluator

object FlyWindowHelper {

    fun flyTo(
        activity: AppCompatActivity,
        startFrame: IntArray,
        endFrame: IntArray,
        ty: Int = FlyView.TYPE_RED,
        str: String = "10",
        listener: FlyListener? = null
    ) {
        try {
            val flyView = FlyView(BaseApplication.application)
            flyView.bindData(ty, str)
            val flContainer = FrameLayout(BaseApplication.application)
            flContainer.visibility = View.VISIBLE
            flContainer.setBackgroundColor(Color.parseColor("#66ff0000"))

            val type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL //在Dialog下层
            val flag = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不消费聚焦事件，即EditText不能触发
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE // 不拦截window区域外的事件
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS // 可在屏幕外绘制
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN // 全屏可绘制，包含状态栏
                    or WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS // >5.0，会绘制状态栏背景
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 沉浸式展示，在状态栏下层

            val w = UiUtil.dp2px(30)
            val h = UiUtil.dp2px(30)
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                type,
                flag,
                PixelFormat.TRANSPARENT
            )
            layoutParams.gravity = Gravity.START or Gravity.BOTTOM
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //刘海屏位置也能绘制
                layoutParams.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            layoutParams.token = null

            //添加到window上
            if (activity == null || activity.isDestroyed || activity.isFinishing) {
                return
            }
            activity.windowManager.addView(flContainer, layoutParams)
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.leftMargin = startFrame[0]
            lp.topMargin = startFrame[1]
            flContainer.addView(flyView, lp)
            val tmpEndFrame = IntArray(2)
            tmpEndFrame[0] = endFrame[0] - w / 2
            tmpEndFrame[1] = endFrame[1] - h / 2
            val valueAnimator = getFlyAnimator(startFrame, tmpEndFrame)
            valueAnimator.addUpdateListener { animation ->
                val pointF = animation.animatedValue as PointF
                val lp1 = flyView.layoutParams as FrameLayout.LayoutParams
                lp1.leftMargin = pointF.x.toInt()
                lp1.topMargin = pointF.y.toInt()
                flyView.layoutParams = lp1
            }
            valueAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    listener?.onStart()
                }

                override fun onAnimationEnd(animation: Animator) {
                    listener?.onEnd()
                    flContainer.visibility = View.GONE
                    if (!activity.isDestroyed && flContainer.parent != null) {
                        try {
                            activity.windowManager.removeViewImmediate(flContainer)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    listener?.onEnd()
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            valueAnimator.start()
            activity.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    if (valueAnimator.isRunning) {
                        valueAnimator.cancel()
                    }
                    if (flContainer.parent != null) {
                        activity.windowManager.removeViewImmediate(flContainer)
                    }
                    activity.lifecycle.removeObserver(this)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取飞入动画
     *
     * @param startFrame
     * @param endFrame
     * @return
     */
    private fun getFlyAnimator(startFrame: IntArray, endFrame: IntArray): ValueAnimator {
        val startP = PointF()
        startP.x = startFrame[0].toFloat()
        startP.y = startFrame[1].toFloat()
        val endP = PointF()
        endP.x = endFrame[0].toFloat()
        endP.y = endFrame[1].toFloat()
        //终点在起点左侧，使用三次贝塞尔曲线
        val controlP1 = PointF()
        val controlP2 = PointF()
        //终点在起点左侧时，使用三次曲线
        if (endP.x <= startP.x) {
            //控制点默认值
            controlP1.x = startP.x + 100
            controlP1.y = endP.y + (startP.y - endP.y) / 3
            //控制点默认值
            controlP2.x = endP.x - 200
            controlP2.y = endP.y + (startP.y - endP.y) * 2 / 3
        } else {
            controlP2.x = startP.x + (endP.x - startP.x) * 4 / 5
            controlP1.x = controlP2.x
            controlP2.y = endP.y + (startP.y - endP.y) * 4 / 5
            controlP1.y = controlP2.y
        }

        val valueAnimator =
            ValueAnimator.ofObject(BezierThirdEvaluator(controlP1, controlP2), startP, endP)
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val duration: Long = 3000

        valueAnimator.interpolator = interpolator
        valueAnimator.setDuration(duration)
        return valueAnimator
    }

    interface FlyListener {
        fun onStart()

        fun onEnd()
    }
}
