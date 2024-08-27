package demo.layout.com.testapplicatiion.view.window.impl

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.MeasureSpec
import android.view.WindowManager.LayoutParams
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import demo.layout.com.testapplicatiion.base.BaseApplication
import demo.layout.com.testapplicatiion.extensions.isVisibleWithParent
import demo.layout.com.testapplicatiion.utils.CommonUtil
import demo.layout.com.testapplicatiion.utils.RunUtils
import demo.layout.com.testapplicatiion.utils.UiUtil
import demo.layout.com.testapplicatiion.utils.YouthNotchUtils
import demo.layout.com.testapplicatiion.utils.YouthRomUtils
import demo.layout.com.testapplicatiion.view.PointEvaluator
import demo.layout.com.testapplicatiion.view.window.DEFAULT_IGNORE_ACTIVITY
import demo.layout.com.testapplicatiion.view.window.OnWindowLifecycleCallbacks
import demo.layout.com.testapplicatiion.view.window.YouthWindowManager
import demo.layout.com.testapplicatiion.view.window.isNotYouthActivity
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.math.abs
import kotlin.math.min


private const val TAG = "SongPlayWindowManager"

private val floatViewSet = mutableSetOf<View>()
private val viewObstacles = mutableSetOf<View>()
//private val viewObservable by lazy { mutableMapOf<View, ObservableBoolean>() }

private val resumedListenerMap = mutableMapOf<View, OnWindowLifecycleCallbacks>()

open class YouthWindowManagerImpl : YouthWindowManager {

    init {
        BaseApplication.application.registerActivityLifecycleCallbacks(object :
            FullActivityLifecycleCallbacks() {
            override fun onActivityPreResumed(activity: Activity) {
                floatViewSet.toList().forEach { view ->
                    resumedListenerMap[view]?.onResumedBefore(this@YouthWindowManagerImpl, activity)
                }

                floatViewSet.reversed().forEach { view ->
                    resumedListenerMap[view]?.let {
                        if (it.onResumed(this@YouthWindowManagerImpl, activity)) {
                            return@forEach
                        }
                    }
                    if (YouthWindowManager.instance.skipRestoreToStackTop) return@forEach
                    when {
                        DEFAULT_IGNORE_ACTIVITY.contains(activity::class.java) -> {
                            // 临时移除
                            removeFloatWindowView(activity, view, true)
                        }
//                        activity.isNotYouthActivity() -> {
//                            // 临时移除
//                            removeFloatWindowView(activity, view, true)
//                        }
                        else -> {
                            addFloatWindowView(activity, view)
                        }
                    }
                }
            }
        })
    }

    override var skipRestoreToStackTop = false


    override fun contains(view: View): Boolean {
        return floatViewSet.contains(view)
    }

    override fun getTargetActivity(view: View): Activity? {
        return (view.parent as? FloatWindowGroup)?.activityReference
    }

//    override fun getObservableShowStatus(view: View): ObservableBoolean {
//        return viewObservable.getOrAssign(view) { ObservableBoolean() }
//    }

    private val notifyCheckRangeRunnable = Choreographer.FrameCallback { notifyLayoutChange() }
    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        // 过滤,再次请求,取消上次请求
        val choreographer = Choreographer.getInstance()
        choreographer.removeFrameCallback(notifyCheckRangeRunnable)
        choreographer.postFrameCallbackDelayed(notifyCheckRangeRunnable, 150)
    }

    override fun addFloatWindowView(
        activity: Activity,
        view: View,
        skipAddWindow: Boolean,
        callback: OnWindowLifecycleCallbacks?,
        marginBottom: Int,
    ) {
        if (callback != null) resumedListenerMap[view] = callback
        val floatWindowGroup = (view.parent as? FloatWindowGroup) ?: FloatWindowGroup(view.context)
        if (view.parent == null) floatWindowGroup.addView(view)
        floatViewSet.add(view)
//        viewObservable[view]?.set(true)

        val layoutParams = (floatWindowGroup.layoutParams as? LayoutParams) ?: run {
            val type = LayoutParams.LAST_APPLICATION_WINDOW //属于Activity层级的Window中最高的99；
            var flag = LayoutParams.FLAG_NOT_FOCUSABLE or //窗口不能获取焦点，意味着它不会接收输入事件
                    LayoutParams.FLAG_NOT_TOUCH_MODAL or //允许触摸事件传递给窗口之外的其他窗口，即不影响底层Activity上的其他view的触摸
                    LayoutParams.FLAG_LAYOUT_NO_LIMITS or //窗口可以布局到屏幕之外
                    LayoutParams.FLAG_LAYOUT_IN_SCREEN // 可全屏布局，包括状态栏位置
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                //> 5.0生效：表示窗口会绘制系统状态栏和导航栏的背景
                flag = flag or LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            }

            //window像素格式为透明格式，通常用于简单的全透明窗口，不涉及复杂的颜色计算，性能开销较小。
            //PixelFormat.RGBA_8888：适合需要高质量图像显示和透明度控制的场景。
            val format = PixelFormat.TRANSPARENT
            val layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                type,
                flag,
                format
            )
            layoutParams.gravity = Gravity.START or Gravity.TOP
            if (VERSION.SDK_INT >= VERSION_CODES.P) {
                //允许窗口内容扩展到显示屏的凹口区域（如刘海屏）
                layoutParams.layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }

            val safeArea = activity.validArea()
            view.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            )
            layoutParams.x = safeArea.right - UiUtil.dp2px(5) - view.measuredWidth
            layoutParams.y = safeArea.bottom - UiUtil.dp2px(50) - view.measuredHeight - marginBottom
            return@run layoutParams
        }
        layoutParams.token = null
        if (!skipAddWindow && hideWindowViewTagSet.isEmpty()) {
            if (floatWindowGroup.parent != null && floatWindowGroup.activityReference == activity) {
                activity.windowManager.updateViewLayout(floatWindowGroup, layoutParams)
            } else {
                floatWindowGroup.activityReference = activity
                if (floatWindowGroup.parent != null) {
                    activity.windowManager.removeViewImmediate(floatWindowGroup)
                }
                try {
                    activity.windowManager.addView(floatWindowGroup, layoutParams)
                } catch (exception: Exception) {
                    Log.e(TAG, "addFloatWindowView: ", exception)
                }
            }
            notifyLayoutChange(false)
        } else {
            floatWindowGroup.layoutParams = layoutParams
            floatWindowGroup.activityReference = activity
        }
    }

    override fun removeFloatWindowView(activity: Activity, view: View, onlyRemoveWindow: Boolean) {
        if (!onlyRemoveWindow) {
            resumedListenerMap.remove(view)
            floatViewSet.remove(view)
//            viewObservable[view]?.set(false)
        }
        (view.parent as? FloatWindowGroup)?.let { floatWindowGroup ->
            floatWindowGroup.activityReference = null
            if (floatWindowGroup.parent != null) {
                activity.windowManager.removeView(floatWindowGroup)
            }
        }
    }

    private val hideWindowViewTagSet = CopyOnWriteArraySet<Any>()

    override fun hideAllWindowView(tag: Any) {
        RunUtils.runByMainThread {
            if (hideWindowViewTagSet.isEmpty() && hideWindowViewTagSet.add(tag)) {
                floatViewSet.forEach { view ->
                    val floatWindowGroup = view.parent as? FloatWindowGroup ?: return@forEach
                    if (floatWindowGroup.parent != null) {
                        floatWindowGroup.windowManager?.removeView(floatWindowGroup)
                    }
                }
            }
        }
    }

    override fun restoreAllWindowView(tag: Any) {
        RunUtils.runByMainThread {
            if (hideWindowViewTagSet.remove(tag) && hideWindowViewTagSet.isEmpty()) {
                floatViewSet.forEach { view ->
                    val floatWindowGroup = view.parent as? FloatWindowGroup ?: return@forEach
                    if (floatWindowGroup.parent == null) {
                        val layoutParams = floatWindowGroup.layoutParams as LayoutParams
                        floatWindowGroup.windowManager?.addView(floatWindowGroup, layoutParams)
                    }
                }
            }
        }
    }

    override fun fetchChildren(): List<View> {
        return floatViewSet.toList()
    }

    override fun hideWindowView(child: View) {
        floatViewSet.find { it === child }?.also { view ->
            val floatWindowGroup = view.parent as? FloatWindowGroup ?: return@also
            if (floatWindowGroup.parent != null) {
                floatWindowGroup.windowManager?.removeView(floatWindowGroup)
            }
        }
    }

    override fun restoreWindowView(child: View) {
        floatViewSet.find { it === child }?.also { view ->
            val floatWindowGroup = view.parent as? FloatWindowGroup ?: return@also
            if (floatWindowGroup.parent == null) {
                val layoutParams = floatWindowGroup.layoutParams as LayoutParams
                floatWindowGroup.windowManager?.addView(floatWindowGroup, layoutParams)
            }
        }
    }

    override fun addObstaclesView(lifecycle: Lifecycle, list: List<View>) {
        fun addObstaclesView(view: View) {
            view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
            viewObstacles.add(view)
        }

        list.forEach { view -> addObstaclesView(view) }
        notifyLayoutChange()
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Event) {
                if (event == Event.ON_RESUME) {
                    list.forEach { view -> addObstaclesView(view) }
                    notifyLayoutChange()
                } else if (event == Event.ON_PAUSE) {
                    list.forEach { view ->
                        view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
                        viewObstacles.remove(view)
                    }
                }
            }
        })
    }

    override fun notifyLayoutChange(needAnimation: Boolean) {
        val animatorList = mutableListOf<Animator>()
        floatViewSet.reversed().forEach {
            val floatWindowGroup = it.parent as? FloatWindowGroup
            if (floatWindowGroup?.isInTouching == true) return@forEach
            val animator = floatWindowGroup?.checkRange()
            animator?.apply { animatorList.add(this) }
        }
        if (needAnimation) animatorList.forEach { it.start() }
    }

}


// --------------------------------------------------------------------------------------------------------------------

private open class FullActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}

private class FloatWindowGroup(context: Context) : FrameLayout(context) {

    private val specialNotchSize by lazy {
        YouthNotchUtils.getSpecialNotchSize(BaseApplication.application).toInt()
    }

    init {
        this.layoutTransition = null
    }

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    var activityReference: Activity? = null
    val windowManager: WindowManager? get() = activityReference?.windowManager

    private var touchScreenX = 0f
    private var touchScreenY = 0f
    private var touchParamsX = 0
    private var touchParamsY = 0

    var isInTouching = false
        private set

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isInTouching = true
                touchScreenX = event.rawX
                touchScreenY = event.rawY
                val windowLayoutParams = layoutParams as WindowManager.LayoutParams
                touchParamsX = windowLayoutParams.x
                touchParamsY = windowLayoutParams.y
            }

            MotionEvent.ACTION_MOVE -> {
                isInTouching = true
                if (abs(event.rawX - touchScreenX) > touchSlop || abs(event.rawY - touchScreenY) > touchSlop) {
                    return true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isInTouching = false
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isInTouching = true
                touchScreenX = event.rawX
                touchScreenY = event.rawY
                val windowLayoutParams = layoutParams as WindowManager.LayoutParams
                touchParamsX = windowLayoutParams.x
                touchParamsY = windowLayoutParams.y
            }

            MotionEvent.ACTION_MOVE -> {
                isInTouching = true
                val windowLayoutParams = layoutParams as WindowManager.LayoutParams
                windowLayoutParams.x = touchParamsX + (event.rawX - touchScreenX).toInt()
                windowLayoutParams.y = touchParamsY + (event.rawY - touchScreenY).toInt()
                windowManager?.updateViewLayout(this, windowLayoutParams)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isInTouching = false
                checkRange()?.start()
                if (abs(event.rawX - touchScreenX) <= touchSlop && abs(event.rawY - touchScreenY) <= touchSlop) {
                    performClick()
                }
            }
        }
        return true
    }

    fun checkRange(): Animator? {
        val activity = activityReference
        if (parent == null || activity == null) return null
        val windowLayoutParams = layoutParams as? WindowManager.LayoutParams ?: return null
        val selfWidth: Int
        val selfHeight: Int
        if (width == 0 && height == 0) {
            this.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            selfWidth = this.measuredWidth
            selfHeight = this.measuredHeight
        } else {
            selfWidth = this.width
            selfHeight = this.height
        }

        val safeArea = activity.validArea()
        val startX = windowLayoutParams.x
        val startY = windowLayoutParams.y

        val windowWidthHalf = (safeArea.width() - selfWidth) / 2
        val dx5 = UiUtil.dp2px(5)
        val endX = when {
            selfWidth == 0 -> startX
            startX > windowWidthHalf -> safeArea.width() - selfWidth - dx5
            else -> dx5
        }
        val minTopY = safeArea.top + UiUtil.dp2px(50)
        val maxBottomY = safeArea.bottom - UiUtil.dp2px(50)

        val endY = startY.let { endY ->

            val obstacles = hashSetOf<View>()
            viewObstacles.forEach {
                if (it.isVisibleWithParent()) {
                    obstacles.add(it)
                }
            }
            obstacles.addAll(floatViewSet)

            if (obstacles.isEmpty()) return@let endY
            // 不可用列表
            val occupyList = LinkedList<Pair<Int, Int>>()
            obstacles.forEach { targetView ->
                // 当前视图自己,跳过
                if (targetView == this || targetView.parent == this) return@forEach
                // 视图不显示,跳过
                if (!targetView.isVisibleWithParent() || targetView.width == 0 || targetView.height == 0) return@forEach
                // 跟自己平行的兄弟组件,通过 layoutParams 判断 (下方只修改了位置参数,需要特殊处理)
                (targetView.parent as? FloatWindowGroup)?.let { floatWindowGroup ->
                    (floatWindowGroup.layoutParams as? WindowManager.LayoutParams)?.let { itLayoutParams ->
                        // 不是同一侧,跳过
                        val selfDirection = (endX + selfWidth / 2) > windowWidthHalf
                        val targetDirection =
                            (itLayoutParams.x + targetView.width / 2) > windowWidthHalf
                        if (selfDirection != targetDirection) return@forEach
                        val targetStartY = itLayoutParams.y
                        val targetEndY = targetStartY + targetView.height
                        occupyList.add(Pair(targetStartY, targetEndY))
                        return@forEach
                    }
                }
                val location = IntArray(2).apply { targetView.getLocationOnScreen(this) }
                val targetStartX = location[0]
                // 不是同一侧,跳过
                val selfDirection = (endX + selfWidth / 2) > windowWidthHalf
                val targetDirection = (targetStartX + targetView.width / 2) > windowWidthHalf
                if (selfDirection != targetDirection) return@forEach
                // 每个视图的纵轴区间
                val targetStartY = location[1] - specialNotchSize
                val targetEndY = targetStartY + targetView.height
                occupyList.add(Pair(targetStartY, targetEndY))
            }
            // 处理数据
            occupyList.add(Pair(0, minTopY))
            occupyList.add(Pair(maxBottomY, Int.MAX_VALUE))
            occupyList.sortBy { it.second }
            occupyList.sortBy { it.first }
            // 相交合并
            for (index in occupyList.size - 1 downTo 1) {
                val currentItem = occupyList[index]
                val previousItem = occupyList[index - 1]
                if ((currentItem.first - previousItem.second) < selfHeight) {
                    occupyList[index - 1] = Pair(previousItem.first, currentItem.second)
                    occupyList.removeAt(index)
                }
            }
            // 没有位置了
            if (occupyList.size <= 1) return@let endY

            // 可用列表
            val availableList = mutableListOf<Pair<Int, Int>>()
            for (index in 0 until occupyList.size - 1) {
                val currentItem = occupyList[index]
                val nextItem = occupyList[index + 1]
                val first = currentItem.second
                val second = nextItem.first
                if (first < endY && endY < (second - selfHeight)) {
                    // 可以放下自己,不用改变位置
                    return@let endY
                }
                availableList.add(Pair(first, second))
            }

            // 需要改变自己的位置,查找最近的下标
            var lastDistance = Int.MAX_VALUE
            // 方向,1 接近上 2 接近下
            var lastOrientation = 0
            val index = availableList.foldIndexed(-1) { index, result, pair ->
                // 自己的中心位置
                val selfCenterY = endY + selfHeight / 2
                val topDistance = abs(selfCenterY - pair.first)
                val bottomDistance = abs(pair.second - selfCenterY)
                val minDistance = min(topDistance, bottomDistance)
                if (minDistance < lastDistance) {
                    lastDistance = minDistance
                    lastOrientation = if (topDistance < bottomDistance) 1 else 2
                    return@foldIndexed index
                }
                return@foldIndexed result
            }

            availableList.getOrNull(index)?.also {
                if (lastOrientation == 1) {
                    return@let it.first
                } else {
                    return@let it.second - selfHeight
                }
            }
            return@let endY
        }.coerceAtLeast(minTopY).coerceAtMost(maxBottomY - selfHeight)

        // 寻找到位置,进行移动
        val startPoint = Point(startX, startY)
        val endPoint = Point(endX, endY)
        if (startPoint == endPoint) return null

        // 这个地方只修改了位置参数,需要上方特殊处理
        windowLayoutParams.x = endPoint.x
        windowLayoutParams.y = endPoint.y
        this.layoutParams = windowLayoutParams
        if (this.parent != null) {
            windowManager?.updateViewLayout(this, windowLayoutParams)
        }
        val animate = ValueAnimator.ofObject(PointEvaluator(), startPoint, endPoint)
        animate.interpolator = DecelerateInterpolator()
        animate.duration = 200
        animate.addUpdateListener { animation ->
            val point = animation.animatedValue as Point
            windowLayoutParams.x = point.x
            windowLayoutParams.y = point.y
            if (this.parent == null) return@addUpdateListener
            windowManager?.updateViewLayout(this, windowLayoutParams)
        }
        return animate
    }
}

// --------------------------------------------------------------------------------------------------------------------

/**
 * window 的有效区域
 */
fun Activity.validArea(): Rect {
    val rect = Rect()
    when {
        VERSION.SDK_INT >= VERSION_CODES.P -> {
            window.decorView.rootWindowInsets?.let { windowInsets ->
                val cutout = windowInsets.displayCutout
                this.windowManager.defaultDisplay.getRectSize(rect)
                rect.bottom =
                    rect.bottom + (cutout?.safeInsetTop ?: 0) + (cutout?.safeInsetBottom ?: 0)
            } ?: let {
                // 兜底操作,预期来到这个 case 会使用上一次的位置
                val point = Point()
                this.windowManager.defaultDisplay.getRealSize(point)
                rect.set(0, 0, point.x, point.y)
            }
        }

        VERSION.SDK_INT >= VERSION_CODES.O -> {
            this.windowManager.defaultDisplay.getRectSize(rect)
            if (YouthRomUtils.isVivo() && YouthNotchUtils.hasNotchAtVIVO(this)) {
                // vivo 8.0 特殊处理
                rect.offset(0, -CommonUtil.getStatusBarHeight(this))
            }
        }

        else -> {
            this.windowManager.defaultDisplay.getRectSize(rect)
        }
    }
    return rect
}

