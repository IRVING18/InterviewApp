package demo.layout.com.testapplicatiion.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import demo.layout.com.testapplicatiion.base.BaseApplication


/**
 * 在宿主/插件中根据不同的[Application]获取[Resources]
 *
 * @see YouthResUtils 宿主中使用
 */
open class YouthResourcesWrap(res: Resources?) {

    val resources: Resources
    val displayMetrics: DisplayMetrics

    init {
        resources = res ?: Resources.getSystem()
        displayMetrics = resources.displayMetrics
    }

    // -----------------------------------------------------------------------------------------------------------------

    val density get() = displayMetrics.density

    // 物理设备宽高
    val screenWidth get() = displayMetrics.widthPixels
    val screenHeight get() = displayMetrics.heightPixels

    val statusHeight get() = statusHeight()
    fun statusHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) return resources.getDimensionPixelSize(resourceId)
        return 0
    }

    val navigationHeight get() = navigationHeight()
    fun navigationHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) return resources.getDimensionPixelSize(resourceId)
        return 0
    }

    val isLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val isPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // -----------------------------------------------------------------------------------------------------------------

    fun getColor(@ColorRes id: Int): Int {
        return ResourcesCompat.getColor(resources, id, null)
    }

    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return ResourcesCompat.getColorStateList(resources, id, null)
    }

    fun getDimen(@DimenRes id: Int): Int {
        return resources.getDimensionPixelSize(id)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        val drawable = ResourcesCompat.getDrawable(resources, id, null) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicWidth)
        return drawable
    }

    fun getDrawable(@DrawableRes id: Int, width: Int, height: Int): Drawable? {
        val drawable = ResourcesCompat.getDrawable(resources, id, null) ?: return null
        drawable.setBounds(0, 0, width, height)
        return drawable
    }

    fun getString(@StringRes id: Int): String {
        return resources.getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return resources.getString(id, *formatArgs)
    }

    fun getStringArray(@ArrayRes id: Int): Array<String?> {
        return resources.getStringArray(id)
    }

    // dp/sp转换辅助方法  ------------------------------------------------------------------------------------------------

    fun transformPixels(value: Number): Float {
        return (value.toFloat() / displayMetrics.density + 0.5f)
    }

    fun dp2px(value: Number): Int {
        return (value.toFloat() * displayMetrics.density + 0.5f).toInt()
    }

    fun sp2px(value: Number): Int {
        return (value.toFloat() * displayMetrics.scaledDensity + 0.5f).toInt()
    }

    fun px2dp(value: Number): Int {
        return (value.toFloat() / displayMetrics.density + 0.5f).toInt()
    }

    fun px2sp(value: Number): Int {
        return (value.toFloat() / displayMetrics.scaledDensity + 0.5f).toInt()
    }

    // View测量辅助方法  -------------------------------------------------------------------------------------------------

    fun getMeasuredWidth(view: View): Int {
        return measureView(view)[0]
    }

    fun getMeasuredHeight(view: View): Int {
        return measureView(view)[1]
    }

    fun measureView(view: View): IntArray {
        val lp = view.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width)
        val lpHeight = lp.height

        val heightSpec = if (lpHeight > 0) {
            View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY)
        } else {
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        }
        view.measure(widthSpec, heightSpec)
        return intArrayOf(view.measuredWidth, view.measuredHeight)
    }

}

val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
val View?.windowManager: WindowManager? get() = this?.context?.windowManager

val WindowManager.defaultWidth: Int get() = defaultSize.x
val WindowManager.defaultHeight: Int get() = defaultSize.y
val WindowManager.defaultSize: Point
    get() {
//            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                Point(
//                    windowManager.currentWindowMetrics.bounds.width(),
//                    windowManager.currentWindowMetrics.bounds.height()
//                )
//            } else {
//                Point(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
//            }
        return Point(defaultDisplay.width, defaultDisplay.height)
    }

object YouthResUtils : YouthResourcesWrap(BaseApplication.application.resources) {

    // 使用预览模式没有
    private val applicationWM: WindowManager?

    init {
        applicationWM = BaseApplication.application.windowManager
    }

    /**
     * 对应的当前应用默认[Window]的宽高
     *
     * 如果是[Activity],对应的默认屏幕可能会发生变化(比如分屏、小窗)
     * 则应该使用[Activity]对应的[Activity.windowManager]
     *
     * @see WindowManager.defaultWidth
     * @see WindowManager.defaultHeight
     * @see WindowManager.defaultSize
     */
    val windowWidth get() = applicationWM?.defaultWidth ?: YouthResUtils.screenWidth
    val windowHeight get() = applicationWM?.defaultHeight ?: YouthResUtils.screenWidth
}

val Number.transformPixels: Float get() = YouthResUtils.transformPixels(this)
val Number.dp2px: Int get() = YouthResUtils.dp2px(this)
val Number.sp2px: Int get() = YouthResUtils.sp2px(this)
val Number.px2dp: Int get() = YouthResUtils.px2dp(this)
val Number.px2sp: Int get() = YouthResUtils.px2sp(this)
