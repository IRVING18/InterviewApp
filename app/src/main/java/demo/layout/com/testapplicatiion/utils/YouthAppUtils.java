package demo.layout.com.testapplicatiion.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import demo.layout.com.testapplicatiion.base.BaseApplication;

public class YouthAppUtils {

    public static Application getApplication() {
        return BaseApplication.getApplication();
    }

    // -----------------------------------------------------------------------------------------------------------------


    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 获取系统的语言类型
     */
    public static String getLocaleLanguage() {
        Locale l = Locale.getDefault();
        return String.format("%s-%s", l.getLanguage(), l.getCountry());
    }

    /**
     * 检测通知栏权限
     */
    public static boolean isNotificationPermissionOpen() {
        return NotificationManagerCompat.from(getApplication()).areNotificationsEnabled();
    }

    /**
     * 打开通知栏设置页面
     */
    public static void openNotificationSetting(Context context) {
        openNotificationSetting(context, -1);
    }

    /**
     * 打开通知栏设置页面
     */
    public static void openNotificationSetting(@NonNull Context context, int requestCode) {
        try {
            Intent localIntent = new Intent();
            if (!(context instanceof Activity)) {
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //直接跳转到应用通知设置的代码：
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(localIntent, requestCode);
                } else {
                    context.startActivity(localIntent);
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                localIntent.putExtra("app_package", context.getPackageName());
                localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(localIntent, requestCode);
                } else {
                    context.startActivity(localIntent);
                }
                return;
            }
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.setData(Uri.parse("package:" + context.getPackageName()));
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(localIntent, requestCode);
                } else {
                    context.startActivity(localIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean hasSIMCard() {
        Application context = getApplication();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyManager.SIM_STATE_READY == tm.getSimState();
    }

    public static void killAllProcess() {
        ActivityManager manager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @NonNull
    public static String getAppPackageName() {
        return getApplication().getPackageName();
    }
}