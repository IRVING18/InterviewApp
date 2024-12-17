package demo.layout.com.testapplicatiion.view.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import demo.layout.com.testapplicatiion.R
import kotlin.math.abs
import kotlin.math.max

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/12/17 10:25
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class CustomTextFL @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mTv: TextView? = null
    private var mLl: LinearLayout? = null

    init {
        clipToPadding = false
        clipChildren = false
        LayoutInflater.from(context).inflate(R.layout.view_custom_text, this, true)

        mTv = findViewById(R.id.tv_text)
        mLl = findViewById(R.id.ll)

        setOnClickListener {
            if (mTv?.visibility == View.GONE) {
                mTv?.visibility = View.VISIBLE
                mLl?.visibility = View.GONE
                mLl?.let { reset(it) }
            } else {
                mTv?.visibility = View.GONE
                mLl?.visibility = View.VISIBLE
                mLl?.post {
                    mLl?.let { applyMagnifierEffect(it) }
                }
            }
        }
    }

    fun bindData(spanList: MutableList<SpanBean>, list: MutableList<String>) {
        mTv?.text = getSpannableString(spanList)
        list.forEachIndexed { i, s ->
            s.toCharArray().forEach { c ->
                mLl?.addView(
                    TextView(context).apply {
                        textSize = 24f
                        setTextColor(Color.parseColor(if (i % 2 == 0) "#FA6400" else "#0099FF"))
                        text = c.toString()
                    },
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
            }
            if (i != list.size - 1) {
                mLl?.addView(
                    View(context).apply {
                        setBackgroundColor(Color.BLACK)
                        tag = "dot"
                    },
                    LinearLayout.LayoutParams(dp2px(3), dp2px(3))
                )
            }
        }
    }

    private val scaleAnimator by lazy {
        ValueAnimator().apply {
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
            duration = 350
        }
    }
    private val marginAnimator by lazy {
        ValueAnimator().apply {
            duration = 350
        }
    }

    private fun applyMagnifierEffect(linearLayout: LinearLayout) {
        val childCount = linearLayout.childCount
        val middleIndex = childCount / 2

        linearLayout.children.forEachIndexed { i, child ->
            if (child.tag == "dot") {
                child.visibility = View.VISIBLE
                val animator = marginAnimator.clone().apply {
                    setFloatValues(0f, dp2px(10f).toFloat())
                    addUpdateListener { animation ->
                        val margin = (animation.animatedValue as Float).toInt()
                        (child.layoutParams as MarginLayoutParams).apply {
                            leftMargin = margin
                            rightMargin = margin
                            child.layoutParams = this
                        }
                    }
                }
                animator.start()
            } else {
                val scale = calculateScale(i, middleIndex, childCount)
                val animator = scaleAnimator.clone().apply {
                    setFloatValues(1f, scale)
                    addUpdateListener { animation ->
                        val animatedScale = animation.animatedValue as Float
                        child.scaleX = animatedScale
                        child.scaleY = animatedScale
                    }
                }
                animator.start()
            }
        }
    }

    private fun calculateScale(index: Int, middleIndex: Int, childCount: Int): Float {
        val initialScale = 1f
        val maxScale = 1.5f
        return max(
            initialScale + (abs(index - middleIndex) / middleIndex.toFloat()) * (maxScale - initialScale),
            1.0f
        )
    }

    private fun reset(linearLayout: LinearLayout) {
        // 遍历 LinearLayout 的所有子视图
        linearLayout.children.forEachIndexed { i, child ->
            if (child.tag == "dot") {
                // 为子视图设置动画
                (child.layoutParams as MarginLayoutParams).apply {
                    leftMargin = 0
                    rightMargin = 0
                    child.layoutParams = this
                }
                child.visibility = View.GONE
            }
        }
    }

    fun getSpannableString(spanList: MutableList<SpanBean>): SpannableString {
        var content = ""
        spanList.forEach { spanBean ->
            content += spanBean.text
        }
        val spannableString = SpannableString(content)
        var tmp = ""
        spanList.forEach { spanBean ->
            val sizeSpan1 = AbsoluteSizeSpan(spanBean.textSize.toInt(), true)
            val colorSpan1 = ForegroundColorSpan(Color.parseColor(spanBean.color))
            spannableString.setSpan(
                sizeSpan1,
                tmp.length,
                tmp.length + spanBean.text.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                colorSpan1,
                tmp.length,
                tmp.length + spanBean.text.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            tmp += spanBean.text
        }
        return spannableString
    }

    private fun dp2px(value: Number): Int {
        return (value.toFloat() * resources.displayMetrics.density + 0.5f).toInt()
    }


}