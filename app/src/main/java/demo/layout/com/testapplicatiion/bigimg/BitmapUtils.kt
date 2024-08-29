package demo.layout.com.testapplicatiion.bigimg

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/28 17:10
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
object BitmapUtils {
    /**
     * 只获取bitmap宽高
     */
    fun getBitmapSizeOnly(resource: File): Array<Int> {
        val options = BitmapFactory.Options()
        //设置只获取宽高
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(resource.absolutePath, options)
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight
        return arrayOf(bitmapWidth, bitmapHeight)
    }

    /**
     * 压缩图片，压缩到2M以下
     *
     * @return
     */
    fun compressTo2M(originImgFile: File?, outFile: File): File? {
        //输出流
        var os: FileOutputStream? = null
        var baos: ByteArrayOutputStream? = null
        //输入流
        var fis: FileInputStream? = null
        try {
            //先将所选图片转化为流的形式，path所得到的图片路径
            fis = FileInputStream(originImgFile)
            //size = 1不压缩长宽
            val size = 1
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inSampleSize = size
            options.inPreferredConfig = Bitmap.Config.RGB_565
            //将图片缩小为原来的  1/size ,不然图片很大时会报内存溢出错误
            val bitmap: Bitmap = BitmapFactory.decodeStream(fis, null, options) ?: return null
            baos = ByteArrayOutputStream()
            baos.toByteArray().size
            //这里100表示不压缩，将不压缩的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var per = 100
            Log.e("compressTo2M   ", "$per  ${baos.toByteArray().size}")
            // 循环判断如果压缩后图片是否大于2000kb,大于继续压缩
            while (baos.toByteArray().size >= 2 * 1024 * 1024) {
                // 每次都减少10
                per -= 10
                // 重置baos即清空baos
                baos.reset()
                // 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos)
                Log.e("compressTo2M   ", "$per  ${baos.toByteArray().size}")
            }

            //回收图片，清理内存
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            //输出file
            os = FileOutputStream(outFile)
            baos.writeTo(os)
            os.flush()
            return outFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (baos != null) {
                try {
                    baos.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}