package demo.layout.com.testapplicatiion;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2022/11/22 10:35 上午
 * Copyright @ 2022 网易有道. All rights reserved.
 **/
public class TestActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_test)
        val ssiv = findViewById<SubsamplingScaleImageView>(R.id.ssiv)
        loadLargeImage(this,"https://nos.netease.com/ydschool-online/QHHguk7VkT5dCLtgj-hWCg.png",ssiv)
    }

    @SuppressLint("CheckResult")
    fun loadLargeImage(context: Context, res: String, imageView: SubsamplingScaleImageView) {
        imageView.isQuickScaleEnabled = true
        imageView.maxScale = 15F;
        imageView.isZoomEnabled = true;
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)

        Glide.with(context).load(res).downloadOnly(object : SimpleTarget<File?>() {
            override fun onResourceReady(resource: File, glideAnimation: Transition<in File?>?) {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(resource.absolutePath, options)
                val sWidth = options.outWidth
                val sHeight = options.outHeight
                options.inJustDecodeBounds = false
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                val width = wm?.defaultDisplay?.width ?: 0
                val height = wm?.defaultDisplay?.height ?: 0
                if (sHeight >= height
                    && sHeight / sWidth >= 3
                ) {
                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                    imageView.setImage(
                        ImageSource.uri(Uri.fromFile(resource)),
                        ImageViewState(0.5f, PointF(0f, 0f), 0)
                    )
                } else {
                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)
                    imageView.setImage(ImageSource.uri(Uri.fromFile(resource)))
                    imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE)
                }
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
            }
        })

    }
}