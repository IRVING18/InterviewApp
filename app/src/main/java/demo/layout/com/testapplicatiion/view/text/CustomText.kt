package demo.layout.com.testapplicatiion.view.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs
import kotlin.math.max

class CustomText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //默认字符的列表
    private var mDefaultTextList: List<SpanBean> = emptyList()

    //分段字符的列表
    private var mSpiltTextList: List<String> = emptyList()

    // 当前的全局缩放比例
    private var mCurrentScale: Float = 1f

    // 当前的字符间距调整量，分段间距
    private var mCurrentShift: Float = 0f

    // 最大间距调整，最大分段间距
    private val mMaxShift = dp2px(20f)

    // 最大缩放比例
    private val mMaxScale = 1.5f

    // 点点的半径
    private var mCircleRadius = 4f

    // 文字大小
    private val mTextSize = sp2px(24f)

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = mTextSize
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }

    private val mDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) // 确保硬件加速
    }

    fun setTextList(spanList: MutableList<SpanBean>, list: List<String>) {
        this.mDefaultTextList = spanList
        mSpiltTextList = list
        invalidate()
    }

    fun animateMagnifierEffect() {
        if (mCurrentShift == 0f) {
            //缩放动画，设置了repeatCount = 1，会反复执行一次
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 350
                repeatMode = ValueAnimator.REVERSE
                repeatCount = 1
                addUpdateListener { animator ->
                    val progress = animator.animatedValue as Float
                    mCurrentScale = 1f + (mMaxScale - 1f) * progress
                    invalidate()
                }
                start()
            }
            //分段间距动画
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 350
                addUpdateListener { animator ->
                    val progress = animator.animatedValue as Float
                    mCurrentShift = mMaxShift * progress
                    invalidate()
                }
                start()
            }
        } else {
            reset()
        }
    }

    fun reset() {
        mCurrentShift = 0f
        mCurrentScale = 1f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mCurrentShift == 0f) {
            //绘制默认状态的字符串
            drawDefaultText(canvas, mDefaultTextList)
        } else {
            //绘制分段后动态变化的字符串
            drawSplitText(canvas)
        }
    }

    /**
     * 绘制分段后动态变化的字符串
     */
    private fun drawSplitText(canvas: Canvas) {
        //绘制分段之后的字符串
        if (mSpiltTextList.isEmpty()) return

        //计算总长度
        val totalWidth = calculateTotalWidthSpilt()
        //文字起始位置
        val startX = (width - totalWidth) / 2

        //当前字符位置
        var currentX = startX
        //字符总数
        var totalCount = 0
        mSpiltTextList.forEachIndexed { index, text -> totalCount += text.length }
        //字符总数中间值，用于计算距离中间字符的锚点，两端缩放比例越大
        val middleIndex = (totalCount - 1) / 2f

        //当前字符位置
        var curIndex = 0
        mSpiltTextList.forEachIndexed { index, text ->
            text.toCharArray().forEachIndexed { cI, c ->
                //根据字符角标，计算缩放比例
                val scaleFactor = calculateScaleFactor(curIndex.toFloat(), middleIndex, totalCount)
                curIndex++

                canvas.save()
                //移动画布，比起移动画笔方便多
                canvas.translate(currentX, height / 2f)

                // 计算基线位置，用于垂直居中的。
                val fontMetrics = mTextPaint.fontMetrics
                val baseLineY = -(fontMetrics.bottom + fontMetrics.top) / 2

                Log.e("wzzzzzzzzzz", "$currentX $c")
                // 缩放文字
                canvas.scale(scaleFactor, scaleFactor, 0f, 0f)
                mTextPaint.textSize = mTextSize
                mTextPaint.color = Color.parseColor(if (index % 2 == 0) "#FA6400" else "#0099FF")
                canvas.drawText(c.toString(), 0f, baseLineY, mTextPaint)
//                canvas.drawLine(0f, -(height.toFloat()),0f, height.toFloat(), Paint().apply { color = Color.RED })
                canvas.restore()

                // 更新下一个字符的起始位置
                currentX += mTextPaint.measureText(c.toString())
            }

            currentX += mCurrentShift / 2

            //画小点点
            if (index != mSpiltTextList.size - 1) {
                canvas.save()
                canvas.translate(currentX, height / 2f)
                mDotPaint.alpha = ((mCurrentShift / mMaxShift) * 255).toInt()
                canvas.drawCircle(mCircleRadius / 2, 0f, mCircleRadius, mDotPaint)
                canvas.restore()
            }

            currentX += mCurrentShift / 2
        }
    }

    /**
     * 绘制默认状态字符串
     */
    private fun drawDefaultText(canvas: Canvas, spanList: List<SpanBean>) {
        val totalWidth = calculateTotalWidthDefault()
        val startX = (width - totalWidth) / 2

        var currentX = startX

        spanList.forEach { spanBean ->
            canvas.save()
            canvas.translate(currentX, height / 2f)

            // 计算基线位置
            val fontMetrics = mTextPaint.fontMetrics
            val baseLineY = -(fontMetrics.bottom + fontMetrics.top) / 2
            // 设置颜色
            mTextPaint.color = Color.parseColor(spanBean.color)
            mTextPaint.textSize = sp2px(spanBean.textSize)
            canvas.drawText(spanBean.text, 0f, baseLineY, mTextPaint)
            canvas.restore()

            // 更新下一个字符的起始位置
            currentX += mTextPaint.measureText(spanBean.text)
        }

    }

    private fun calculateScaleFactor(index: Float, middleIndex: Float, totalCount: Int): Float {
        // 根据字符距离中心的距离动态调整缩放比例
        val distanceFromCenter = abs(index.toFloat() - middleIndex).toFloat()
        val maxDistance = max(1f, (totalCount - 1) / 2f)
//        Log.e("wzzzzzzzzzz","$index $middleIndex  $distanceFromCenter   $maxDistance")
        return mCurrentScale - (mCurrentScale - 1f) * (1 - (distanceFromCenter / maxDistance))
    }

    /**
     * 获取分段时字符总长度
     */
    private fun calculateTotalWidthSpilt(): Float {
        // 计算字符总宽度和动态间距的总和
        var totalWidth = 0f
        mSpiltTextList.forEachIndexed { index, s ->
            val w = mTextPaint.measureText(s)
            totalWidth += w
            Log.e("wzzzzzzzzzz", "$index $s $w")
        }
        return totalWidth + mCurrentShift * (mSpiltTextList.size - 1)
    }

    /**
     * 获取默认时字符总长度
     */
    private fun calculateTotalWidthDefault(): Float {
        // 计算字符总宽度和动态间距的总和
        var totalWidth = 0f
        mDefaultTextList.forEachIndexed { index, s ->
            val w = mTextPaint.measureText(s.text)
            totalWidth += w
            Log.e("wzzzzzzzzzz", "$index ${s.text} $w")
        }
        return totalWidth
    }

    private fun dp2px(value: Float): Float {
        return value * context.resources.displayMetrics.density
    }

    private fun sp2px(value: Float): Float {
        return value * context.resources.displayMetrics.scaledDensity + 0.5f
    }

}