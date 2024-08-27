package cn.youth.flowervideo.view.timer

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import demo.layout.com.testapplicatiion.R

/**
 * @Description:
 * @author: ivring
 * @date: 2022/4/13
 **/
class FlyView constructor(context: Context) : RelativeLayout(context) {
    companion object {
        const val TYPE_RED = 1
        const val TYPE_COIN = 2
    }

    var iv: ImageView? = null
    var tv: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_fly_view, this, true)
        iv = findViewById(R.id.iv)
        tv = findViewById(R.id.tv)
    }

    fun bindData(type: Int, str: String) {
        var resId = R.drawable.icon_reward_toast_red
        var colorId = R.color.color_ff5269
        when(type){
            TYPE_RED->{
                resId = R.drawable.icon_reward_toast_red
                colorId = R.color.color_ff5269
            }
            TYPE_COIN->{
                resId = R.drawable.icon_reward_toast_coin
                colorId = R.color.color_ffdb73
            }
        }
        iv?.setImageResource(resId)
        tv?.text = "+$str"
        tv?.setTextColor(resources.getColor(colorId))
    }
}