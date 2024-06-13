package demo.layout.com.testapplicatiion.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;


import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import demo.layout.com.testapplicatiion.MyApp;

/**
 * fangzhi create 2019/12/10
 *
 * UI功能类，去掉context获取
 */
public class UiUtil {

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public static int dp2px(int dpValue) {
    return SizeUtils.dp2px(dpValue);
  }

  /**
   * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
   */
  public static int px2dp(int pxValue) {
    final float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int getScreenWidth() {
    Resources resources = Resources.getSystem();
    DisplayMetrics dm = resources.getDisplayMetrics();
    return dm.widthPixels;
  }

  /**
   * 获取view的区域高度
   */
  public static int getScreenHeight() {
    Resources resources = Resources.getSystem();
    DisplayMetrics dm = resources.getDisplayMetrics();
    return dm.heightPixels;
  }

  /**
   * 获取window宽度
   */
  public static int getWindowWidth() {
    WindowManager wm =
        (WindowManager) MyApp.getAppContext().getSystemService(Context.WINDOW_SERVICE);
    Point point = new Point();
    wm.getDefaultDisplay().getSize(point);
    return point.x;
  }

  public static int getColor(@ColorRes int id) {
    return ContextCompat.getColor(MyApp.getAppContext(), id);
  }

  public static ColorStateList getColorStateList(@ColorRes int id) {
    return ContextCompat.getColorStateList(MyApp.getAppContext(), id);
  }

  public static int getDimen(@DimenRes int resId) {
    return Utils.getApp().getResources().getDimensionPixelSize(resId);
  }

  public static Drawable getDrawable(@DrawableRes int id) {
    Drawable drawable = ContextCompat.getDrawable(MyApp.getAppContext(), id);
    if (drawable == null) return null;
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    return drawable;
  }

  public static String[] getArrarys(@ArrayRes int resId) {
    return Utils.getApp().getResources().getStringArray(resId);
  }

  public static String getString(@StringRes int resId) {
    return Utils.getApp().getResources().getString(resId);
  }

  private static final Rect rect = new Rect();

//  public static boolean isViewVisible(View view) {
//    if (view.getVisibility() != View.VISIBLE) {
//      return false;
//    }
//
//    boolean result = view.getGlobalVisibleRect(rect);
//
//    if (result) {
//      if (rect.top >= UIUtilKt.getHeightPixels()) {
//        return false;
//      }
//
//      int height = view.getHeight();
//      int visibleHeight = rect.height();
//
//      return visibleHeight >= height / 2;
//    }
//    return false;
//  }
}
