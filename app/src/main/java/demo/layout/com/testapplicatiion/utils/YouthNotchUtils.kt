package demo.layout.com.testapplicatiion.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import java.lang.reflect.Method
import java.util.Locale

/**
 * https://blog.csdn.net/iamZ2z/article/details/84063943
 */
object YouthNotchUtils {

    // 调用该方法，可以获取刘海屏的px值，没刘海屏则返回0
    fun getSpecialNotchSize(context: Context): Float {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O && Build.VERSION.SDK_INT != Build.VERSION_CODES.O_MR1) {
            return 0f
        }

        var notchLengthFloat = 0.0f
        //判断手机厂商，目前8.0只有华为、小米、oppo、vivo适配了刘海屏
        val phoneManufacturer = Build.BRAND.toLowerCase(Locale.ROOT)
        if ("huawei" == phoneManufacturer) {
            //huawei,长度为length,单位px
            val haveInScreenEMUI = hasNotchInScreenEMUI(context)
            if (haveInScreenEMUI) {
                val screenSize = getNotchSizeEMUI(context)
                notchLengthFloat = screenSize[1].toFloat()
            }
        } else if ("xiaomi" == phoneManufacturer) {
            //xiaomi,单位px
            val haveInScreenMIUI = getNotchMIUI() == 1
            if (haveInScreenMIUI) {
                val resourceId: Int = context.resources.getIdentifier("notch_height", "dimen", "android")
                var result = 0
                if (resourceId > 0) {
                    result = context.resources.getDimensionPixelSize(resourceId)
                }
                notchLengthFloat = result.toFloat()
            }
        } else if ("vivo" == phoneManufacturer) {
            //vivo,单位dp，高度27dp
            val haveInScreenVIVO = hasNotchAtVIVO(context)
            if (haveInScreenVIVO) {
                notchLengthFloat = YouthResUtils.dp2px(27f).toFloat()
            }
        } else if ("oppo" == phoneManufacturer) {
            // //oppo
            // if (hasNotchAtOPPO(context)) {
            //     notchLengthFloat = getNotchSizeOppo().toFloat()
            // }
        }
        return notchLengthFloat
    }

    //huawei
    private fun hasNotchInScreenEMUI(context: Context): Boolean {
        var ret = false
        try {
            val cl: ClassLoader = context.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get: Method = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(hwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("test", "hasNotchInScreen Exception")
        }
        return ret
    }

    private fun getNotchSizeEMUI(context: Context): IntArray {
        var ret = intArrayOf(0, 0)
        try {
            val cl: ClassLoader = context.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get: Method = hwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(hwNotchSizeUtil) as IntArray
        } catch (e: ClassNotFoundException) {
            Log.e("test", "getNotchSize ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "getNotchSize NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("test", "getNotchSize Exception")
        }
        return ret
    }

    private fun hasNotchAtOPPO(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    @SuppressLint("PrivateApi")
    private fun getNotchSizeOppo(): Int {
        return try {
            val key = "ro.oppo.screen.heteromorphism"
            val cls = Class.forName("android.os.SystemProperties")
            val hideMethod = cls.getMethod("get", String::class.java)
            // 229,0:492,53
            val str = hideMethod.invoke(null, key) as String
            return str.split(":")[1].split(",")[1].toInt()
        } catch (exception: Exception) {
            Log.e("test", "getNotchSize Exception")
        }
    }

    @SuppressLint("PrivateApi")
    public fun getNotchMIUI(): Int {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getIntMethod = clazz.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            return (getIntMethod.invoke(null, "ro.miui.notch", 0) as Int).toInt()
        } catch (e: Exception) {
            Log.e("MainActivity", "Platform error: $e")
        }
        return 0
    }

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun hasNotchAtVIVO(context: Context): Boolean {
        var ret = false
        try {
            val classLoader: ClassLoader = context.classLoader
            val clazz = classLoader.loadClass("android.util.FtFeature")
            val method: Method = clazz.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            ret = method.invoke(clazz, 0x00000020) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("Notch", "hasNotchAtVivo Exception")
        }
        return ret
    }

}
