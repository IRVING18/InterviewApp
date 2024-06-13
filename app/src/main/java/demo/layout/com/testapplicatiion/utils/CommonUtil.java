package demo.layout.com.testapplicatiion.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.TypedValue;

import java.util.Formatter;
import java.util.Locale;

import androidx.appcompat.widget.TintContextWrapper;

/**
 * 公共类
 * Created by shuyu on 2016/11/11.
 */

public class CommonUtil {

  public static String stringForTime(long timeMs) {
    if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
      return "00:00";
    }
    int totalSeconds = (int) (timeMs / 1000);
    int seconds = totalSeconds % 60;
    int minutes = (totalSeconds / 60) % 60;
    int hours = totalSeconds / 3600;
    StringBuilder stringBuilder = new StringBuilder();
    Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
    if (hours > 0) {
      return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
    } else {
      return mFormatter.format("%02d:%02d", minutes, seconds).toString();
    }
  }

  /**
   * Get activity from context object
   * @param context something
   * @return object of Activity or null if it is not Activity
   */
  public static Activity scanForActivity(Context context) {
    if (context == null) return null;

    if (context instanceof Activity) {
      return (Activity) context;
    } else if (context instanceof TintContextWrapper) {
      return scanForActivity(((TintContextWrapper) context).getBaseContext());
    } else if (context instanceof ContextWrapper) {
      return scanForActivity(((ContextWrapper) context).getBaseContext());
    }

    return null;
  }

  /**
   * 获取状态栏高度
   * @param context 上下文
   * @return 状态栏高度
   */
  public static int getStatusBarHeight(Context context) {
    return YouthResUtils.INSTANCE.getStatusHeight();
  }

  /**
   * 获取ActionBar高度
   * @param activity activity
   * @return ActionBar高度
   */
  public static int getActionBarHeight(Activity activity) {
    TypedValue tv = new TypedValue();
    if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
      return TypedValue.complexToDimensionPixelSize(
          tv.data, activity.getResources().getDisplayMetrics()
      );
    }
    return 0;
  }

}
