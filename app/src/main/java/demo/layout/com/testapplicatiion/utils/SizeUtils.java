package demo.layout.com.testapplicatiion.utils;

import android.view.View;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : utils about size
 * </pre>
 *
 * @deprecated see @{@link YouthResUtils}
 */
public final class SizeUtils {

    private SizeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * {@link YouthResUtils#dp2px(Number)}
     */
    public static int dp2px(final float dpValue) {
        return YouthResUtils.INSTANCE.dp2px(dpValue);
    }

    /**
     * {@link YouthResUtils#sp2px(Number)}
     */
    public static int px2dp(final float pxValue) {
        return YouthResUtils.INSTANCE.px2dp(pxValue);
    }

    /**
     * {@link YouthResUtils#sp2px(Number)}
     */
    public static int sp2px(final float spValue) {
        return YouthResUtils.INSTANCE.sp2px(spValue);
    }

    /**
     * {@link YouthResUtils#px2sp(Number)}
     */
    public static int px2sp(final float pxValue) {
        return YouthResUtils.INSTANCE.px2sp(pxValue);
    }

    /**
     * {@link YouthResUtils#getMeasuredWidth(View)}
     */
    public static int getMeasuredWidth(final View view) {
        return measureView(view)[0];
    }

    /**
     * {@link YouthResUtils#getMeasuredHeight(View)}
     */
    public static int getMeasuredHeight(final View view) {
        return measureView(view)[1];
    }

    /**
     * {@link YouthResUtils#measureView(View)}
     */
    public static int[] measureView(final View view) {
        return YouthResUtils.INSTANCE.measureView(view);
    }
}
