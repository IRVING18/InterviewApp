package demo.layout.com.testapplicatiion.bigimg

import android.graphics.BitmapFactory
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import demo.layout.com.testapplicatiion.base.BaseActivity
import demo.layout.com.testapplicatiion.databinding.ActivitySubsamplingImgBinding
import demo.layout.com.testapplicatiion.utils.UIUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

/**
 * 有道领世
 * TestApplicatiion
 * Description: 大图框架
 * Created by wangzheng on 2024/8/28 10:23
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class SubsamplingActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySubsamplingImgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySubsamplingImgBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        lifecycleScope.launch {
            //设置图片存储路径，私有目录下
            val file = File("$filesDir/tmp.png")
            val imgUrl = "https://nos.netease.com/ydschool-online/QHHguk7VkT5dCLtgj-hWCg.png"
            //1、先将图片下载到file文件中
            downloadImageToFile(imgUrl, file)
            //2、通过SubsamplingImg加载本地file中大图
            loadBigImg(file)
        }
    }

    /**
     * 使用SubsamplingImg加载本地file中大图
     */
    private fun loadBigImg(resource: File) {
        val options = BitmapFactory.Options()
        //设置只获取宽高
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(resource.absolutePath, options)
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight
        val screenHeight = UIUtils.getHeight(this)
        //计算bitmap高度是否大于屏幕高度，并且高、宽比大于3
        if (bitmapHeight >= screenHeight && bitmapHeight / bitmapWidth >= 3) {
            mBinding.ssiv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
            mBinding.ssiv.setImage(
                ImageSource.uri(Uri.fromFile(resource)),
                ImageViewState(0.5f, PointF(0f, 0f), 0)
            )
        } else {
            mBinding.ssiv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)
            mBinding.ssiv.setImage(ImageSource.uri(Uri.fromFile(resource)))
            mBinding.ssiv.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE)
        }
    }

    /**
     * 通过Okhttp将图片先下载到本地
     */
    suspend fun downloadImageToFile(imageUrl: String, outputFile: File) {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(imageUrl).build()
            val response = client.newCall(request).execute()
            val inputStream = response.body?.byteStream()

            inputStream?.use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}