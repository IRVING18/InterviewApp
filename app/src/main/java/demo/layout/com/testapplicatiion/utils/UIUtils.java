package demo.layout.com.testapplicatiion.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by anfs on 2019-12-10.
 */
public class UIUtils {


  /**
   * 计算方法：获取到屏幕的分辨率:point.x和point.y，再取出屏幕的DPI（每英寸的像素数量），
   * 计算长和宽有多少英寸，即：point.x / dm.xdpi，point.y / dm.ydpi，屏幕的长和宽算出来了，
   * 再用勾股定理，计算出斜角边的长度，即屏幕尺寸。
   * @return
   */
  public static double getPhysicsScreenSize(Context context){
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Point point = new Point();
    manager.getDefaultDisplay().getRealSize(point);
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    double x = Math.pow(point.x / dm.xdpi, 2);//dm.xdpi是屏幕x方向的真实密度值，比上面的densityDpi真实。
    double y = Math.pow(point.y / dm.ydpi, 2);//dm.xdpi是屏幕y方向的真实密度值，比上面的densityDpi真实。
    double screenInches = Math.sqrt(x + y);
    return screenInches;
  }

  public static float getScreenWidthDp(Context context){
    final float scale = context.getResources().getDisplayMetrics().density;
    float width = context.getResources().getDisplayMetrics().widthPixels;
    return width / (scale <= 0 ? 1 : scale) + 0.5f;
  }

  //全面屏、刘海屏适配
  public static float getHeight(Activity activity) {
    if (activity == null) {
      return 0f;
    }
    //hideBottomUIMenu(activity);
    float height;
    int realHeight = getRealHeight(activity);
    if (UIUtils.hasNotchScreen(activity)) {
      height = px2dip(activity, realHeight - getStatusBarHeight(activity));
    }else {
      height = px2dip(activity, realHeight);
    }
    return height;
  }

  public static void hideBottomUIMenu(Activity activity) {
    if (activity == null) {
      return;
    }
    try {
      //隐藏虚拟按键，并且全屏
      if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
        View v = activity.getWindow().getDecorView();
        v.setSystemUiVisibility(View.GONE);
      } else if (Build.VERSION.SDK_INT >= 19) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
          //                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
          | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //获取屏幕真实高度，不包含下方虚拟导航栏
  public static int getRealHeight(Context context) {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    DisplayMetrics dm = new DisplayMetrics();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      display.getRealMetrics(dm);
    } else {
      display.getMetrics(dm);
    }
    int realHeight = dm.heightPixels;
    return realHeight;
  }

  //获取状态栏高度
  public static float getStatusBarHeight(Context context) {
    float height = 0;
    int resourceId = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      height = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
    }
    return height;
  }

  public static float getNavigationBarHeight(Context context) {
    Resources resources = context.getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    if (resourceId > 0) {
      return resources.getDimensionPixelSize(resourceId);
    }
    return 0;
  }

  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / (scale <= 0 ? 1 : scale) + 0.5f);
  }
  /**
   * 判断是否是刘海屏
   * @return
   */
  public static boolean hasNotchScreen(Activity activity){
    if (getInt("ro.miui.notch",activity) == 1 || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity)
      || hasNotchAtVivo(activity) || isAndroidPHasNotch(activity)){ //TODO 各种品牌
      return true;
    }

    return false;
  }

  /**
   * Android P 刘海屏判断
   * @param activity
   * @return
   */
  public static boolean isAndroidPHasNotch(Activity activity){
    boolean ret = false;
    if (Build.VERSION.SDK_INT >= 28){
      try {
        Class windowInsets = Class.forName("android.view.WindowInsets");
        Method method = windowInsets.getMethod("getDisplayCutout");
        Object displayCutout = method.invoke(windowInsets);
        if (displayCutout != null) {
          ret = true;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ret;
  }

  /**
   * 小米刘海屏判断.
   * @return 0 if it is not notch ; return 1 means notch
   * @throws IllegalArgumentException if the key exceeds 32 characters
   */
  public static int getInt(String key,Activity activity) {
    int result = 0;
    if (isMiui()){
      try {
        ClassLoader classLoader = activity.getClassLoader();
        @SuppressWarnings("rawtypes")
        Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
        //参数类型
        @SuppressWarnings("rawtypes")
        Class[] paramTypes = new Class[2];
        paramTypes[0] = String.class;
        paramTypes[1] = int.class;
        Method getInt = SystemProperties.getMethod("getInt", paramTypes);
        //参数
        Object[] params = new Object[2];
        params[0] = new String(key);
        params[1] = new Integer(0);
        result = (Integer) getInt.invoke(SystemProperties, params);

      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 华为刘海屏判断
   * @return
   */
  public static boolean hasNotchAtHuawei(Context context) {
    boolean ret = false;
    try {
      ClassLoader classLoader = context.getClassLoader();
      Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
      Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
      ret = (boolean) get.invoke(HwNotchSizeUtil);
    } catch (ClassNotFoundException e) {
    } catch (NoSuchMethodException e) {
    } catch (Exception e) {
    } finally {
      return ret;
    }
  }

  public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
  public static final int VIVO_FILLET = 0x00000008;//是否有圆角

  /**
   * VIVO刘海屏判断
   * @return
   */
  public static boolean hasNotchAtVivo(Context context) {
    boolean ret = false;
    try {
      ClassLoader classLoader = context.getClassLoader();
      Class FtFeature = classLoader.loadClass("android.util.FtFeature");
      Method method = FtFeature.getMethod("isFeatureSupport", int.class);
      ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
    } catch (ClassNotFoundException e) {
    } catch (NoSuchMethodException e) {
    } catch (Exception e) {
    } finally {
      return ret;
    }
  }

  public static int dip2px(Context context, float dpValue) {
    float density = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * density + 0.5);
  }

  /**
   * OPPO刘海屏判断
   * @return
   */
  public static boolean hasNotchAtOPPO(Context context) {
    return  context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
  }

  public static boolean isMiui() {
    boolean sIsMiui = false;
    try {
      Class<?> clz = Class.forName("miui.os.Build");
      if (clz != null) {
        sIsMiui = true;
        //noinspection ConstantConditions
        return sIsMiui;
      }
    } catch (Exception e) {
      // ignore
    }
    return sIsMiui;
  }

  /**
   * 获取view的中心点
   * @param view
   * @return
   */
  public static int[] getCenterFrame(View view) {
    int[] location = new int[2];
    view.getLocationOnScreen(location);
    int x = location[0] + view.getWidth() / 2; // view距离 屏幕左边的距离（即x轴方向）
    int y = location[1] + view.getHeight() / 2; // view距离 屏幕顶边的距离（即y轴方向）
    location[0] = x;
    location[1] = y;
    return location;
  }


  //设置状态栏字体颜色
  public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
    View decor = activity.getWindow().getDecorView();
    if (dark) {
      decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    } else {
      decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
  }

  /**
   * 获取view的位置
   *
   * @param view
   * @return
   */
  public static RectF getRect(View view) {
    RectF rectF = new RectF();
    int[] location = new int[2];
    view.getLocationOnScreen(location);
    rectF.left = location[0];
    rectF.top = location[1];
    rectF.right = location[0] + view.getWidth();
    rectF.bottom = location[1] + view.getHeight();
    return rectF;
  }
}
