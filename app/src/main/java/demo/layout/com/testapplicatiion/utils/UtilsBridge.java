package demo.layout.com.testapplicatiion.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import java.io.File;
import java.io.InputStream;
import java.util.List;


/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2020/03/19
 *     desc  :
 * </pre>
 */
class UtilsBridge {

    static void init(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.init(app);
    }

    static void unInit(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.unInit(app);
    }

    static void preLoad() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // UtilsActivityLifecycleImpl
    ///////////////////////////////////////////////////////////////////////////
    static Activity getTopActivity() {
        return UtilsActivityLifecycleImpl.INSTANCE.getTopActivity();
    }

    static void addOnAppStatusChangedListener(final Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.addOnAppStatusChangedListener(listener);
    }

    static void removeOnAppStatusChangedListener(final Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.removeOnAppStatusChangedListener(listener);
    }

    static void addActivityLifecycleCallbacks(final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(callbacks);
    }

    static void removeActivityLifecycleCallbacks(final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(callbacks);
    }

    static void addActivityLifecycleCallbacks(final Activity activity,
                                              final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(activity, callbacks);
    }

    static void removeActivityLifecycleCallbacks(final Activity activity) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity);
    }

    static void removeActivityLifecycleCallbacks(final Activity activity,
                                                 final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    static List<Activity> getActivityList() {
        return UtilsActivityLifecycleImpl.INSTANCE.getActivityList();
    }

    static Application getApplicationByReflect() {
        return UtilsActivityLifecycleImpl.INSTANCE.getApplicationByReflect();
    }

    static boolean isAppForeground() {
        return UtilsActivityLifecycleImpl.INSTANCE.isAppForeground();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ActivityUtils
    ///////////////////////////////////////////////////////////////////////////
    static boolean isActivityAlive(final Activity activity) {
        return ActivityUtils.isActivityAlive(activity);
    }

    static boolean isActivityResumed(final Activity activity) {
        return ActivityUtils.isActivityResumed(activity);
    }

    static String getLauncherActivity(final String pkg) {
        return ActivityUtils.getLauncherActivity(pkg);
    }

    static Activity getActivityByContext(Context context) {
        return ActivityUtils.getActivityByContext(context);
    }

    static void finishAllActivities() {
        ActivityUtils.finishAllActivities();
    }


//    static void relaunchApp() {
//        AppUtils.relaunchApp();
//    }

    ///////////////////////////////////////////////////////////////////////////
    // BarUtils
    ///////////////////////////////////////////////////////////////////////////
//    static int getStatusBarHeight() {
//        return BarUtils.getStatusBarHeight();
//    }
//
//    static int getNavBarHeight() {
//        return BarUtils.getNavBarHeight();
//    }
//
//
//    static String byte2FitMemorySize(final long byteSize) {
//        return ConvertUtils.byte2FitMemorySize(byteSize);
//    }
//
//    static byte[] inputStream2Bytes(final InputStream is) {
//        return ConvertUtils.inputStream2Bytes(is);
//    }
//
//
//    static List<String> inputStream2Lines(final InputStream is, final String charsetName) {
//        return ConvertUtils.inputStream2Lines(is, charsetName);
//    }
//
//
//    static boolean writeFileFromIS(final String filePath, final InputStream is) {
//        return FileIOUtils.writeFileFromIS(filePath, is);
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // FileUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static boolean isFileExists(final File file) {
//        return FileUtils.isFileExists(file);
//    }
//
//    static File getFileByPath(final String filePath) {
//        return FileUtils.getFileByPath(filePath);
//    }
//
//    static boolean deleteAllInDir(final File dir) {
//        return FileUtils.deleteAllInDir(dir);
//    }
//
//    static boolean createOrExistsFile(final File file) {
//        return FileUtils.createOrExistsFile(file);
//    }
//
//    static boolean createOrExistsDir(final File file) {
//        return FileUtils.createOrExistsDir(file);
//    }
//
//    static boolean createFileByDeleteOldFile(final File file) {
//        return FileUtils.createFileByDeleteOldFile(file);
//    }
//
//    static long getFsTotalSize(String path) {
//        return FileUtils.getFsTotalSize(path);
//    }
//
//    static long getFsAvailableSize(String path) {
//        return FileUtils.getFsAvailableSize(path);
//    }
//
//    static void notifySystemToScan(File file) {
//        FileUtils.notifySystemToScan(file);
//    }
//
//
//    static Bitmap view2Bitmap(final View view) {
//        return ImageUtils.view2Bitmap(view);
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    // IntentUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static boolean isIntentAvailable(final Intent intent) {
//        return IntentUtils.isIntentAvailable(intent);
//    }
//
//    static Intent getLaunchAppIntent(final String pkgName) {
//        return IntentUtils.getLaunchAppIntent(pkgName);
//    }
//
//    static Intent getInstallAppIntent(final File file) {
//        return IntentUtils.getInstallAppIntent(file);
//    }
//
//    static Intent getInstallAppIntent(final Uri uri) {
//        return IntentUtils.getInstallAppIntent(uri);
//    }
//
//    static Intent getUninstallAppIntent(final String pkgName) {
//        return IntentUtils.getUninstallAppIntent(pkgName);
//    }
//
//    static Intent getLaunchAppDetailsSettingsIntent(final String pkgName, final boolean isNewTask) {
//        return IntentUtils.getLaunchAppDetailsSettingsIntent(pkgName, isNewTask);
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // KeyboardUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static void fixSoftInputLeaks(final Activity activity) {
//        YouthKeyboardUtils.fixSoftInputLeaks(activity);
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    // PermissionUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static boolean isGranted(final String... permissions) {
//        return PermissionUtils.isGranted(permissions);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    static boolean isGrantedDrawOverlays() {
//        return PermissionUtils.isGrantedDrawOverlays();
//    }
//
//
//    static String getForegroundProcessName() {
//        return ProcessUtils.getForegroundProcessName();
//    }
//
//    static String getCurrentProcessName() {
//        return ProcessUtils.getCurrentProcessName();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // RomUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static boolean isSamsung() {
//        return YouthRomUtils.isSamsung();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // ScreenUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static int getAppScreenWidth() {
//        return ScreenUtils.getAppScreenWidth();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // SDCardUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static boolean isSDCardEnableByEnvironment() {
//        return SDCardUtils.isSDCardEnableByEnvironment();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // ShellUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static ShellUtils.CommandResult execCmd(final String command, final boolean isRooted) {
//        return ShellUtils.execCmd(command, isRooted);
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // SizeUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static int dp2px(final float dpValue) {
//        return SizeUtils.dp2px(dpValue);
//    }
//
//    static int px2dp(final float pxValue) {
//        return SizeUtils.px2dp(pxValue);
//    }
//
//    static int sp2px(final float spValue) {
//        return SizeUtils.sp2px(spValue);
//    }
//
//    static int px2sp(final float pxValue) {
//        return SizeUtils.px2sp(pxValue);
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // StringUtils
//    ///////////////////////////////////////////////////////////////////////////
    static boolean isSpace(final String s) {
        return StringUtils.isSpace(s);
    }
//
//    static boolean equals(final CharSequence s1, final CharSequence s2) {
//        return StringUtils.equals(s1, s2);
//    }
//
//    static String getString(@StringRes int id) {
//        return StringUtils.getString(id);
//    }
//
//    static String getString(@StringRes int id, Object... formatArgs) {
//        return StringUtils.getString(id, formatArgs);
//    }
//
//    static String format(@Nullable String str, Object... args) {
//        return StringUtils.format(str, args);
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    // ThreadUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static <T> Utils.Task<T> doAsync(final Utils.Task<T> task) {
//        YouthThreadUtils.getCachedPool().execute(task);
//        return task;
//    }
//
    static void runOnUiThread(final Runnable runnable) {
        YouthThreadUtils.runOnUiThread(runnable);
    }

    static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        YouthThreadUtils.runOnUiThreadDelayed(runnable, delayMillis);
    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // TimeUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static String millis2FitTimeSpan(long millis, int precision) {
//        return TimeUtils.millis2FitTimeSpan(millis, precision);
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    // UriUtils
//    ///////////////////////////////////////////////////////////////////////////
//
//    static File uri2File(final Uri uri) {
//        return UriUtils.uri2File(uri);
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    // ViewUtils
//    ///////////////////////////////////////////////////////////////////////////
//    static View layoutId2View(@LayoutRes final int layoutId) {
//        return ViewUtils.layoutId2View(layoutId);
//    }
//
//    static boolean isLayoutRtl() {
//        return ViewUtils.isLayoutRtl();
//    }
}
