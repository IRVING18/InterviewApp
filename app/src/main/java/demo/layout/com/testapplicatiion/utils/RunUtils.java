package demo.layout.com.testapplicatiion.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;


/**
 * @author lm
 * @description
 * @date 2020/9/30 下午3:42
 * 公共线程管理类
 * TODO
 * 1、任务退出机制
 * 2、SDK线程HOOK统一管理
 */
public class RunUtils {
    public static final String TAG = "RunUtils";

    /*****************************************  线程池系列  ***************************************/

    /**
     * @description DB和SP专用线程
     * 数量 ->  1
     * 优先级 1
     */
    public static void runByDbThread(Runnable runnable) {
        try {
            YouthThreadUtils.getSinglePool(Thread.MIN_PRIORITY).execute(runnable);
        } catch (Exception e) {
            Log.e(TAG, "runByDbThread error -->" + e.getMessage());
        }
    }

    /**
     * @description 其他业务线程
     * 数量max -> 2*cpu + 1
     * 优先级 1
     */
    public static void runByIOThread(Runnable runnable) {
        runByIOThread(runnable, null);
    }

    public static void runByIOThread(Runnable runnable, Runnable failRunnable) {
        try {
            YouthThreadUtils.getIoPool(Thread.MIN_PRIORITY).execute(runnable);
        } catch (Exception e) {
            Log.e(TAG, "runByIOThread error -->" + e.getMessage());
            if (null != failRunnable) {
                failRunnable.run();
            }
        }
    }

    /**
     * @description 埋点专用线程
     * 数量 ->  1
     * 优先级 10
     */
    public static void runByPointThread(Runnable runnable) {
        try {
            YouthThreadUtils.getSinglePool(Thread.MAX_PRIORITY).execute(runnable);
        } catch (Exception e) {
            Log.e(TAG, "runByPointThread error -->" + e.getMessage());
        }
    }

    /**
     * @description 预加载数据专门线程
     * 数量 ->  1
     * 优先级 1
     */
    public static void runByPreDataThread(Runnable runnable) {
        try {
            YouthThreadUtils.getSinglePool(Thread.MIN_PRIORITY).execute(runnable);
        } catch (Exception e) {
            Log.e(TAG, "runByPreDataThread error -->" + e.getMessage());
        }
    }

    /**
     * 主线程
     */
    public static void runByMainThread(Runnable runnable) {
        runByMainThread(runnable, null);
    }

    /**
     * 主线程 支持失败回调
     */
    public static void runByMainThread(Runnable runnable, Runnable failRunnable) {
        try {
            YouthThreadUtils.runOnUiThread(runnable);
        } catch (Exception e) {
            Log.e(TAG, "runByMainThread error -->" + e.getMessage());
            if (null != failRunnable) {
                failRunnable.run();
            }
        }
    }

    /**
     * 主线程 延迟处理
     */
    public static void runByMainThreadDelayed(Runnable runnable, long delayMillis) {
        try {
            YouthThreadUtils.runOnUiThreadDelayed(runnable, delayMillis);
        } catch (Exception e) {
            Log.e(TAG, "runByMainThreadDelayed error -->" + e.getMessage());
        }
    }

    /**
     * 移除主线程runnable
     */
    public static void removeByMainThread(Runnable runnable) {
        try {
            YouthThreadUtils.getMainHandler().removeCallbacks(runnable);
        } catch (Exception e) {
            Log.e(TAG, "removeByMainThread error -->" + e.getMessage());
        }
    }

    public interface RtnTask<T> {
        T run();
    }

    /**************************************  解析使用old （待优化） ************************************/

    public static <T> T run(RtnTask<T> task) {
        return run(task, null);
    }

    public static <T> T run(RtnTask<T> task, Runnable action) {
        T t = null;
        try {
            if (null != task) {
                t = task.run();
            }
        } catch (Exception e) {
            Log.e(TAG, "runT error -->" + e.getMessage());
            if (null != action) {
                action.run();
            }
        }
        return t;
    }

    /**
     * 在catch块中执行代码,确保程序不崩溃,无安全隐患
     *
     * @param runnable 执行代码块
     * todo 老代码调用的 后期会慢慢去除
     */
    @Deprecated
    public static void run(Runnable runnable) {
        try {
            if (null != runnable) {
                runnable.run();
            }
        } catch (Exception e) {
            Log.e(TAG, "run error -->" + e.getMessage());
        }
    }

}
