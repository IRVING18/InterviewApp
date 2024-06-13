package demo.layout.com.testapplicatiion.view.window

import android.app.Activity
import android.view.View
import androidx.lifecycle.Lifecycle
import demo.layout.com.testapplicatiion.Activity2
import demo.layout.com.testapplicatiion.MainActivity
import demo.layout.com.testapplicatiion.view.window.impl.YouthWindowManagerImpl

/**
 * 默认忽略的 Activity ,当这个列表的界面覆盖时,
 * 不将浮窗移动到界面最上方
 */
val DEFAULT_IGNORE_ACTIVITY = listOf(
    Activity2::class.java,
//    SplashAdActivity::class.java,
//    SplashEmptyActivity::class.java,
//    SearchActivity::class.java,
//    UserInfoActivity::class.java,
)

/**
 * 主动暂停歌曲的 Activity ,当这个列表的界面覆盖时,
 * 不将浮窗移动到界面最上方并且暂停歌曲
 * (需要判断广告类型的 activity,由于依赖外部,所以改为判断非中青 activity)
 */
fun Activity.isNotYouthActivity(): Boolean {
    return !this::class.java.name.startsWith("cn.youth.news") && !this::class.java.name.startsWith("com.youth.market")
}

fun Activity.isYouthActivity(): Boolean {
    return this::class.java.name.startsWith("cn.youth.news") || this::class.java.name.startsWith("com.youth.market")
}

interface YouthWindowManager {

    companion object {
        @JvmStatic
        val instance: YouthWindowManager by lazy { YouthWindowManagerImpl() }
    }

    /**
     * 拦截检测事件
     * 当弹出 dialog 等顶级容器时禁止,隐藏后恢复
     */
    var skipRestoreToStackTop: Boolean

    /**
     * 当前视图是否在显示的集合中
     */
    fun contains(view: View): Boolean

    /**
     * 获取浮窗视图当前所属的 Activity (从哪个 Activity 添加到 window 的)
     */
    fun getTargetActivity(view: View): Activity?

    /**
     * 添加浮窗视图
     * @param callback 默认保留上一次的回调 (传 null 不会清空)
     */
    fun addFloatWindowView(
        activity: Activity,
        view: View,
        skipAddWindow: Boolean = false,
        callback: OnWindowLifecycleCallbacks? = null,
        marginBottom: Int = 0,
    )

    /**
     * 移除浮窗视图
     *
     * @param onlyRemoveWindow 只删除窗口,保留数据
     */
    fun removeFloatWindowView(activity: Activity, view: View, onlyRemoveWindow: Boolean = false)

    /**
     * 隐藏/恢复 视图状态,需成对调用,内部维护有计数器
     * 并且恢复前需先调用隐藏
     */
    fun hideAllWindowView(tag: Any)
    fun restoreAllWindowView(tag: Any)

    /**
     * 隐藏/恢复 单个View
     */
    fun hideWindowView(child: View)
    fun restoreWindowView(child: View)

    /**
     * 获取所有浮窗View
     */
    fun fetchChildren(): List<View>

    /**
     * 添加障碍视图,对浮窗视图排挤
     */
    fun addObstaclesView(lifecycle: Lifecycle, view: View) {
        addObstaclesView(lifecycle, listOf(view))
    }

    /**
     * 移除障碍视图
     */
    fun addObstaclesView(lifecycle: Lifecycle, list: List<View>)

    /**
     * 布局位置改变,需要重新检测边界
     */
    fun notifyLayoutChange(needAnimation: Boolean = true)
}

interface OnWindowLifecycleCallbacks {
    fun onResumedBefore(manager: YouthWindowManager, activity: Activity) {}
    fun onResumed(manager: YouthWindowManager, activity: Activity): Boolean
}
