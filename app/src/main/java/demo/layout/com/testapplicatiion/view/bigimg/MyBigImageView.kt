package demo.layout.com.testapplicatiion.view.bigimg

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.OnDoubleTapListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.Scroller
import java.io.InputStream

/**
 * 有道领世
 * TestApplicatiion
 * Description: 处理大图
 * Created by wangzheng on 2024/8/29 14:16
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MyBigImageView : View {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {
    }

    //用于绘制时使用，同时用于inBitmap复用内存空间
    private var mBitmap: Bitmap? = null
    private val mOptions: BitmapFactory.Options by lazy { BitmapFactory.Options() }

    //用于分区加载图片的解码器
    private var mBitmapRegionDecoder: BitmapRegionDecoder? = null

    //区分加载图片的范围
    private val mRect by lazy { Rect() }

    //图片的真实大小
    private var mImgWidth = 0
    private var mImgHeight = 0

    //当前view的真实大小
    private var mViewWidth = 0
    private var mViewHeight = 0

    //图片的缩放比例
    private var mScale = 1f
    private var mCurrentScale = 1f

    //图片最大缩放几倍
    private var mMaxMultiple = 5

    //Canvas绘制时，用于缩放的矩阵
    private val mMatrix: Matrix by lazy { Matrix() }


    fun setImage(inputStream: InputStream) {
        //1、预测量图片的真实大小
        mOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, mOptions)
        mImgWidth = mOptions.outWidth
        mImgHeight = mOptions.outHeight;

        //2、初始化分区加载的解码器
        mOptions.inJustDecodeBounds = false //取消只测量大小
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565 //设置颜色内存
        mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false)

        //3、重绘view，目的是回调onSizeChange()获取view的真实大小，以便获取缩放比例
        requestLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //记录view的宽高
        mViewWidth = w
        mViewHeight = h

        //设置分区加载区域，即当前view的大小
        mRect.top = 0
        mRect.left = 0
        mRect.right = mViewWidth
        mRect.bottom = mViewHeight

        //计算缩放比例：view宽度 / 图片宽度
        mScale = mViewWidth.toFloat() / mImgWidth.toFloat()
        mCurrentScale = mScale
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.parseColor("#6600ff00"))
        mBitmapRegionDecoder?.let { decoder ->
            //复用bitmap内存空间
            mOptions.inBitmap = mBitmap
            //用分段解析器加载图片
            mBitmap = decoder.decodeRegion(mRect, mOptions)
            //绘制
            mBitmap?.let {
                //设置缩放比例
                mMatrix.setScale(mCurrentScale, mCurrentScale)
                canvas.drawBitmap(it, mMatrix, null)
            }
        }

        canvas.drawCircle(fligerX.toFloat(), fligerY.toFloat(),5f,mPaint)
    }

    var mPaint = Paint()
    ///////////////////////////////////////////////////////////////////////////
    // 至此，以下部分都是处理滑动手势的
    ///////////////////////////////////////////////////////////////////////////

    //手势工具：处理滑动、fling事件
    private val mGestureDetector by lazy {
        GestureDetector(context, mGestureListener).apply {
            //设置双击监听，用于缩放
            setOnDoubleTapListener(mOnDoubleTapListener)
        }
    }

    //滚动工具：用于配合GestureDetector的fling，继续滑动图片
    private val mScroller by lazy { Scroller(context) }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //将触摸事件传递给手势工具
        mGestureDetector.onTouchEvent(event)
        //缩放手势处理工具
        mScaleGestureDetector.onTouchEvent(event)
        //返回true，消费事件
        return true
    }

    //被mScroller.fling()方法触发，
    override fun computeScroll() {
        super.computeScroll()
        //computeScrollOffset()返回true，表示：没有结束滚动
        //isFinished这个用来配合onDown()，滚动时触摸强制停止滚动
        if (!mScroller.isFinished && mScroller.computeScrollOffset()) {
            //处理mRect，然后重绘
            if (mRect.top + mViewHeight / mCurrentScale < mImgHeight) {
                mRect.top = mScroller.currY
                mRect.bottom = (mRect.top + mViewHeight / mCurrentScale).toInt()
            }
            if (mRect.bottom > mImgHeight) {
                mRect.top = (mImgHeight - mViewHeight / mCurrentScale).toInt()
                mRect.bottom = mImgHeight.toInt()
            }
            invalidate()
        }
    }

    private val mGestureListener = object : GestureDetector.OnGestureListener {
        //用户在触摸屏上轻击并抬起，手指离开触摸屏时触发(而长按、滚动、滑动时，不会触发这个手势)
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return false
        }

        //重压，触摸屏按下后片刻后抬起，会触发这个手势，如果迅速抬起则不会
        override fun onShowPress(e: MotionEvent) {}

        //长按，触摸屏按下后既不抬起也不移动，过一段时间后触发
        override fun onLongPress(e: MotionEvent) {}

        //按下，触摸屏按下时立刻触发
        override fun onDown(e: MotionEvent): Boolean {
            //触摸时，让fling的scroller停下来
            if (!mScroller.isFinished) {
                mScroller.forceFinished(true)
            }
            return false
        }

        //滚动，触摸屏按下后移动
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float)
                : Boolean {
            //滑动的时候，改变mRect显示区域的位置
            mRect.offset(distanceX.toInt(), distanceY.toInt())
            //处理上下左右的边界
            handleBorder()
            invalidate()
            return false
        }

        //滑动，触摸屏按下后快速移动并抬起，会先触发滚动手势，跟着触发一个滑动手势
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float)
                : Boolean {
            //设置之后，会调用自动调用view的computeScroll()方法
            mScroller.fling(
                mRect.left,
                mRect.top,
                -velocityX.toInt(),
                -velocityY.toInt(),
                0,
                mImgWidth,
                0,
                mImgHeight
            )
            return false
        }
    }

    private fun handleBorder() {
        //左边界
        if (mRect.left < 0) {
            mRect.left = 0
            mRect.right = (mViewWidth / mCurrentScale).toInt()
        }
        //右边界
        if (mRect.right > mImgWidth) {
            mRect.right = mImgWidth
            mRect.left = (mImgWidth - mViewWidth / mCurrentScale).toInt()
        }
        if (mRect.top < 0) {
            mRect.top = 0
            mRect.bottom = (mViewHeight / mCurrentScale).toInt()
        }
        if (mRect.bottom > mImgHeight) {
            mRect.bottom = mImgHeight.toInt()
            mRect.top = (mImgHeight - mViewHeight / mCurrentScale).toInt()
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 至此，以下部分都是处理缩放手势的
    ///////////////////////////////////////////////////////////////////////////

    private var fligerX = 0
    private var fligerY = 0

    private val mOnDoubleTapListener = object : OnDoubleTapListener {
        //单击
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return false
        }

        //双击
        override fun onDoubleTap(e: MotionEvent): Boolean {
            //处理双击事件
            if (mCurrentScale > mScale) {
                mCurrentScale = mScale
            } else {
                mCurrentScale = mScale * mMaxMultiple
            }
            //手指点击位置，作为中心点
            val centerX = e.x
            val centerY = e.y
            fligerX = centerX.toInt()
            fligerY = centerY.toInt()
//            //Rect缩放后的新宽高
//            val newWidth = mViewWidth / mCurrentScale
//            val newHeight = mViewHeight / mCurrentScale
//            //中心点 - (宽度 / 2) 就是新的rect启点
//            mRect.left = centerX.toInt()
//            mRect.top = centerY.toInt()
//            mRect.right = (mRect.left + newWidth).toInt()
//            mRect.bottom = (mRect.top + newHeight).toInt()


            val newWidth = mViewWidth / mCurrentScale
            val newHeight = mViewHeight / mCurrentScale
            //中心点 - (宽度 / 2) 就是新的rect启点
            mRect.left = centerX.toInt()
            mRect.top = centerY.toInt()
            mRect.right = (mRect.left + newWidth).toInt()
            mRect.bottom = (mRect.top + newHeight).toInt()
            //处理上下左右的边界
            handleBorder()
            invalidate()
            return true
        }

        //处理与双击相关的事件序列。包括双击的每一个动作事件，例如双击时的按下、移动和抬起动作
        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return false
        }
    }
    //缩放手势处理器
    private val mScaleGestureDetector by lazy {
        ScaleGestureDetector(context, mScaleGestureListener)
    }

    private val mScaleGestureListener = object :OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //处理手指缩放事件
            //获取与上次事件相比，得到的比例因子
            val scaleFactor = detector.scaleFactor
            mCurrentScale *= scaleFactor
            //如果大于最大放大倍数，就设置成最大倍数
            if (mCurrentScale > mScale * mMaxMultiple) {
                mCurrentScale = mScale * mMaxMultiple
            } //如果小于默认倍数，就设置成默认倍数
            else if (mCurrentScale <= mScale) {
                mCurrentScale = mScale
            }
            mRect.right = mRect.left + (mViewWidth / mCurrentScale).toInt()
            mRect.bottom = mRect.top + (mViewHeight / mCurrentScale).toInt()
            invalidate()
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            //当 >= 2 个手指碰触屏幕时调用，若返回 false 则忽略改事件调用
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
        }

    }

}