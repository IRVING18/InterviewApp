package demo.layout.com.testapplicatiion.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Looper
import android.util.Log

abstract class BaseApplication : Application(), Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "BasicApplication"

        @JvmStatic
        private var innerApplication: BaseApplication? = null

        @JvmStatic
        val application: BaseApplication
            get() = innerApplication!!
    }

    var START_TIME: Long = 0
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)

        START_TIME = System.currentTimeMillis()
        if (innerApplication == null) {
            innerApplication = this
//            MultiDex.install(this)
//            YouthAppLifecycleUtils.init(this)
        }
    }

    /**
     * 需要子类手动调用
     * 基类Application 需处理的事
     */
    protected fun doCommonService() {
        Thread.setDefaultUncaughtExceptionHandler(this)
//        FixReportSizeConfigurationCrashManager.getInstance().init()
//        initRxProperties()
//        disableAPIDialog()
    }

    protected open fun doUncaughtException(thread: Thread, exception: Throwable?) {
//        YouthLogger.e(TAG, "子类未实现 doUncaughtException 方法")
    }

//    /**
//     * 解决androidP 第一次打开程序出现莫名弹窗 (反射)
//     * 弹窗内容“detected problems with api ”
//     */
//    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
//    private fun disableAPIDialog() {
//        if (VERSION.SDK_INT < VERSION_CODES.P) return
//        try {
//            val clazz = Class.forName("android.app.ActivityThread")
//            val currentActivityThread = clazz.getDeclaredMethod("currentActivityThread")
//            currentActivityThread.isAccessible = true
//            val activityThread = currentActivityThread.invoke(null)
//            val hiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown")
//            hiddenApiWarningShown.isAccessible = true
//            hiddenApiWarningShown.setBoolean(activityThread, true)
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//    }
//
//    /**
//     * @description 解决Rx2.0问题
//     */
//    private fun initRxProperties() {
//        //解决 2.0 Error直接发给虚拟机 发生 crash
//        RxJavaPlugins.setErrorHandler { YouthLogger.e("RxJavaErrorHandler", it) }
//        //RxJava开启一个循环线程在后台默默回收Publisher，默认是1秒循环一次，但是这样比较耗费cpu，纯净后台检测时，cpu唤醒率无法达到标准。
//        System.setProperty("rx2.purge-enabled", "true")
//        System.setProperty("rx2.purge-period-seconds", "600")
//        //RXJava与app本身共用一套线程调度器
//        RxJavaPlugins.setIoSchedulerHandler { Schedulers.from(YouthThreadUtils.getIoPool()) }
//        //禁止使用YouthThreadUtils.getSinglePool()替换单线程池，会导致delay操作符栈溢出，ExecutorScheduler->scheduleDirect 方法栈溢出
//        RxJavaPlugins.setSingleSchedulerHandler { Schedulers.from(Executors.newScheduledThreadPool(1)) }
//        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.from(YouthThreadUtils.getCpuPool()) }
//        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.from(YouthThreadUtils.getCachedPool()) }
//    }

    /**
     * @description 崩溃处理
     */
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        try {
            Log.d(TAG, "doUncaughtException(${thread}): ", exception)
            doUncaughtException(thread, exception)
            Log.d(TAG, "doUncaughtException(${thread}): end")
        } catch (exception: Throwable) {
            Log.d(TAG, "doUncaughtException(${thread}): ", exception)
        } finally {
            if (thread == Looper.getMainLooper().thread) {
//                YouthAppUtils.killAllProcess()
            }
        }
    }

    /**
     * 设置字体为默认大小，不随系统字体大小改而改变
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale != 1f) {
            tryResetResources(super.getResources())
        }
    }

    override fun getResources(): Resources {
        return tryResetResources(super.getResources())
    }

    fun tryResetResources(res: Resources): Resources {
        if (res.configuration.fontScale != 1f) {
            val newConfig = res.configuration
            newConfig.fontScale = 1f //设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }
}