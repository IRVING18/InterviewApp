package demo.layout.com.testapplicatiion;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import demo.layout.com.testapplicatiion.base.BaseApplication;

/**
 * @author lm
 * @description 看点Application
 * *** 只放与初始化服务相关的方法和属性 ***
 * *** 全局常量不要放在里面放到常量配置文件 ***
 * 1、所有的初始化服务操作放入相关的Utils包里
 * 2、应用 userinfo与userID 合并
 * 3、引入 判断冷热启动框架
 * 4、引入 应用防carsh框架
 * 5、整理 数据清理机制
 * @date 2020-01-21 11:20
 */
@Keep
public class MyApp extends BaseApplication {
  public final static String TAG = "MyApp";
  public static boolean isCheckSplashAd = true;
  public static boolean mIsStartSplashAd = false;

  /**
   * 当前是否为debug状态
   */
  public static boolean isDebug() {
    return BuildConfig.DEBUG;
  }

  /**
   * @deprecated use BaseApplication.getApplication();
   */
  public static Application getAppContext() {
    return BaseApplication.getApplication();
  }

  /****************************************** Base Service *****************************************/

  @Override
  protected void attachBaseContext(@NonNull Context base) {
    super.attachBaseContext(base);
//    RePluginConfig rePluginConfig = new RePluginConfig();
//    rePluginConfig.setUseHostClassIfNotFound(true);
//    RePlugin.App.attachBaseContext(this, rePluginConfig);
//    AndroidBugSpWorkaround.tryHockQueuedWork();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    try {
//      RePlugin.App.onCreate();
      baseInit();
    } catch (Exception exception) {
      Log.e(TAG, "baseInit exception", exception);
      throw exception;
    }

    try {
//      String processName = ProcessUtils.getCurrentProcessName();
//      Log.e(TAG, "processName:" + processName);
//      if (ProcessUtils.isMainProcess()) {
//        YouthMob.INSTANCE.insertApplication(this);
//        fixHuaweiBroadcastReceiverIssue();
//        ReplaceExecutorHelperKt.replaceLottieTask();
//        doCommonService();
//        LauncherHelper.preInitUmengService();
//        // todo 主线程 提供统一的 App单日使用时长、单次使用时长、 前后台切换事件  目前后台切回 处理逻辑太多 有明显卡顿现象
//        registerActivityLifecycleCallbacks(new YouthActivityLifecycleImpl());
//        RunUtils.runByIOThread(this::doBaseServiceThread);
//        LauncherHelper.delayInit(true);
//      }
    } catch (Exception exception) {
      Log.e(TAG, "otherInit exception", exception);
      throw exception;
    }
  }
//
//  private void fixHuaweiBroadcastReceiverIssue() {
//    if (YouthRomUtils.isHuawei() && Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
//      // 线上都是华为+api22的崩溃信息,不开放其他机型
//      LoadedApkHuaWei.hookHuaWeiVerifier(this);
//    }
//  }

  private void baseInit() {
    initDebugValue();
    iniUtils();
    //setActivityEnabled(this, false, JPushStatisticsActivity.class);
  }

  private void iniUtils() {
//    Utils.init(getAppContext());
//    if (isDebug()) {
//      LogUtils.setLevelAll();
//    }
  }

  public static void initDebugValue() {
//      try {
//          BaseAppConfig.INSTANCE.setDebug(isDebug() ||DebugSpUtils.INSTANCE.getDEBUG_SWITCH().getValue());
//      } catch (Exception exception) {
//          YouthLogger.e("MyApp", "initialDebugValue: " + exception.getMessage());
//      }
  }


  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    onTrimMemoryClearGlide(level);
  }

  private void onTrimMemoryClearGlide(int level) {
    Log.e(TAG, "onTrimMemory:" + level);
    try {
      if (level == TRIM_MEMORY_UI_HIDDEN) { //所有UI不可见 即用户点击了Home键或者Back键导致应用的UI界面不可见
        Glide.get(this).onLowMemory();
      }
    } catch (Exception e) {
      Log.e(TAG, "onTrimMemoryClearGlide(" + level + ")", e);
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    onLowMemoryClearGlide();
  }

  private void onLowMemoryClearGlide() {
    Log.e(TAG, "onLowMemory");
    try {
      Glide.get(this).clearMemory();
    } catch (Exception e) {
      Log.e(TAG, "onLowMemoryClearGlide", e);
    }
  }



  /**
   * Crash
   * TODO 优化
   */
  protected void doUncaughtException(@NonNull Thread thread, @Nullable Throwable exception) {
//    long currentCrashTime = System.currentTimeMillis();
//    if (thread == Looper.getMainLooper().getThread()) {
//      int crashCount = SP2Util.getInt(SPKey.APP_CRASH_COUNT, 0);
//
//      if (crashCount == 0) {
//        SP2Util.putLong(SPKey.APP_CRASH_TIME, currentCrashTime);
//      }
//      ++crashCount;
//      int crtCountConfig = AppConfigHelper.getConfig().getCrt_count();
//
//      // 崩溃2次 ，5分钟内崩溃2次，清空所有
//      if (crashCount >= crtCountConfig) {
//        if (SPKey.isInTimeShow(5 * 60)) {
//          UserUtil.clearAppData();
//          ActivityUtils.finishAllActivities();
//          return;  //5分钟内多次启动不重启
//        } else { //重新算
//          crashCount = 1;
//          SP2Util.putInt(SPKey.APP_CRASH_COUNT, crashCount);
//          SP2Util.putLong(SPKey.APP_CRASH_TIME, currentCrashTime);
//        }
//      } else {
//        SP2Util.putInt(SPKey.APP_CRASH_COUNT, crashCount);
//      }
//    }
//    MobclickAgent.onKillProcess(getAppContext());
//    if (ProcessUtils.isMainProcess() ) {
//      LauncherHelper.restart();
//    }
  }

}

