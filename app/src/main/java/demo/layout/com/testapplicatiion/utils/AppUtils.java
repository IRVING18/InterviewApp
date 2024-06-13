package demo.layout.com.testapplicatiion.utils;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

public class AppUtils {

    /**
     * Return whether application is foreground.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground() {
        return UtilsBridge.isAppForeground();
    }

    /**
     * Register the status of application changed listener.
     *
     * @param listener The status of application changed listener
     */
    public static void registerAppStatusChangedListener(@NonNull final Utils.OnAppStatusChangedListener listener) {
        UtilsBridge.addOnAppStatusChangedListener(listener);
    }

    /**
     * Unregister the status of application changed listener.
     *
     * @param listener The status of application changed listener
     */
    public static void unregisterAppStatusChangedListener(@NonNull final Utils.OnAppStatusChangedListener listener) {
        UtilsBridge.removeOnAppStatusChangedListener(listener);
    }

    /**
     * Relaunch the application.
     */
//    public static void relaunchApp() {
//        relaunchApp(false);
//    }
//
//    /**
//     * Relaunch the application.
//     *
//     * @param isKillProcess True to kill the process, false otherwise.
//     */
//    public static void relaunchApp(final boolean isKillProcess) {
//        Intent intent = UtilsBridge.getLaunchAppIntent(Utils.getApp().getPackageName());
//        if (intent == null) {
//            Log.e("AppUtils", "Didn't exist launcher activity.");
//            return;
//        }
//        intent.addFlags(
//            Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
//        );
//        Utils.getApp().startActivity(intent);
//        if (!isKillProcess) return;
//        YouthAppUtils.killAllProcess();
//        System.exit(0);
//    }
}
