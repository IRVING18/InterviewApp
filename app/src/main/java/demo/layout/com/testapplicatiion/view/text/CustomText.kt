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

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 48f
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }
    private var textList: List<String> = emptyList()
    private var currentScale: Float = 1f // 当前的全局缩放比例
    private var currentShift: Float = 0f // 当前的字符间距调整量

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) // 确保硬件加速
    }

    fun setTextList(list: List<String>) {
        textList = list
        invalidate()
    }

    fun animateMagnifierEffect() {
        val maxScale = 1.5f
        val maxShift = dp2px(10f) // 最大间距调整

        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 350
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                currentScale = 1f + (maxScale - 1f) * progress
//                currentShift = maxShift * progress
                invalidate()
            }
            start()
        }
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 350
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                currentShift = maxShift * progress
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (textList.isEmpty()) return

        val totalWidth = calculateTotalWidth()
        val startX = (width - totalWidth) / 2

        var currentX = startX
        val middleIndex = (textList.size.toFloat() - 1) / 2f

        textList.forEachIndexed { index, text ->
            val scaleFactor = calculateScaleFactor(index.toFloat(), middleIndex)
            canvas.save()
            canvas.translate(currentX, height / 2f)

            Log.e("wzzzzzzzzzz","$currentX $text")


            // 放大文字
            canvas.scale(scaleFactor, scaleFactor, 0f, 0f)
            canvas.drawText(text, 0f, 0f, paint)
            canvas.drawLine(0f, -(height.toFloat()),0f, height.toFloat(), Paint().apply { color = Color.RED })
            canvas.restore()

            // 更新下一个字符的起始位置
            currentX += paint.measureText(text) + currentShift
        }
    }

    private fun calculateScaleFactor(index: Float, middleIndex: Float): Float {
        // 根据字符距离中心的距离动态调整缩放比例
        val distanceFromCenter = abs(index.toFloat() - middleIndex).toFloat()
        val maxDistance = max(1f, (textList.size - 1) / 2f)
//        Log.e("wzzzzzzzzzz","$index $middleIndex  $distanceFromCenter   $maxDistance")
        return currentScale - (currentScale - 1f) * (1 - (distanceFromCenter / maxDistance))
    }

    private fun calculateTotalWidth(): Float {
        // 计算字符总宽度和动态间距的总和
        var totalWidth = 0f
        textList.forEachIndexed { index, s ->
            val w = paint.measureText(s)
            totalWidth += w
            Log.e("wzzzzzzzzzz","$index $s $w")
        }
        return totalWidth + currentShift * (textList.size - 1)
    }

    private fun dp2px(value: Float): Float {
        return value * context.resources.displayMetrics.density
    }
}