package demo.layout.com.testapplicatiion.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Created by anfs on 2019-11-05.
 */

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.isVisible(): Boolean {
    return this?.visibility == View.VISIBLE
}

fun View.isVisibleWithParent(): Boolean {
    if (this.parent == null) return false
    if (this.visibility != View.VISIBLE) return false
    if (this.layoutParams.height == 0 && this.measuredHeight == 0) return false
    // ViewRootImpl 返回 true
    return (this.parent as? View)?.isVisibleWithParent() ?: return true
}

fun View?.isVisible(visible: Boolean) {
    this?.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View?.isGone(): Boolean {
    return this?.visibility == View.GONE
}

fun View?.isGone(gone: Boolean) {
    this?.visibility = if (gone) View.GONE else View.VISIBLE
}

//fun Any.toJson(): String {
////    return JsonUtils.toJson(this)
//}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun String?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * 显示时分
 */
fun Long.showTimeStr(): String {
    val time = this
    val min = time / 60
    val second = time % 60

    val minStr = if (min < 10) "0$min" else min.toString()
    val secondStr = if (second < 10) "0$second" else second.toString()
    return "$minStr:$secondStr"
}

///**
// * 修改TouchSlop
// * 由于ViewPager2 嵌套RecyclerView 上下滑动时容易触发左右滑动，所以需要修改TouchSlop
// */
//fun ViewPager2.setTouchSlop() {
//    runCatching {
//        val touchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop * 4
//        YouthReflectUtils.reflect(this)
//            .field("mRecyclerView")
//            .field("mTouchSlop", touchSlop)
//    }
//}

fun ViewGroup.inflateView(
    @LayoutRes layoutResId: Int,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(this.context).inflate(layoutResId, this, attachToRoot)
}

//inline fun View.findLifecycleOwnerOnAttach(
//    crossinline action: (lifecycleOwner: LifecycleOwner) -> Unit
//) {
//    doOnAttach { findViewTreeLifecycleOwner()?.let(action) }
//}

//inline fun ViewGroup.doOnVisibleChange(
//    crossinline changeAction: VisibleChangeAction.() -> Unit
//) {
//    val visibleChange = VisibleChangeAction().apply { changeAction() }
//    findLifecycleOwnerOnAttach { lifecycleOwner ->
//        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
//            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//                val isResumed = lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
//                visibleChange.resumeChange?.invoke(hasWindowFocus(), isResumed)
//            }
//        })
//        val observer = object : View(context) {
//            override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
//                super.onWindowFocusChanged(hasWindowFocus)
//                val isResumed = lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
//                visibleChange.windowFocusChange?.invoke(hasWindowFocus, isResumed)
//            }
//        }
//        this.addView(observer)
//    }
//}

class VisibleChangeAction {

    var windowFocusChange: ((hasWindowFocus: Boolean, isResumed: Boolean) -> Unit)? = null
    var resumeChange: ((hasWindowFocus: Boolean, isResumed: Boolean) -> Unit)? = null

    fun onWindowFocusChange(action: (hasWindowFocus: Boolean, isResumed: Boolean) -> Unit) {
        windowFocusChange = action
    }

    fun onResumeChange(action: (hasWindowFocus: Boolean, isResumed: Boolean) -> Unit) {
        resumeChange = action
    }

}

var lastClickTime = 0L

fun singleClick(interval: Long = 500, action: () -> Unit) {
    val intervalTime = System.currentTimeMillis() - lastClickTime
    if (intervalTime > interval) {
        lastClickTime = System.currentTimeMillis()
        action.invoke()
    }
}

fun View.setViewWidth(width: Int) {
    val params = this.layoutParams
    params.width = width
    this.layoutParams = params
}

fun View.setViewHeight(width: Int) {
    val params = this.layoutParams
    params.height = width
    this.layoutParams = params
}


//fun View.setOnNotFastClickListener(onClickListener: View.OnClickListener) {
//    setOnClickListener {
//        if (NClick.isFastClick(it)) return@setOnClickListener
//        onClickListener.onClick(it)
//    }
//}
