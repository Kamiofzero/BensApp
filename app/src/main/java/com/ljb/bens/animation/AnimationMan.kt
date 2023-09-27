package com.ljb.bens.animation

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import com.ljb.base.utils.LogUtil
import com.ljb.bens.BensApp

class AnimationMan {

    companion object {
        fun myAppOperationBarAnim(view: View, isShow: Boolean) {
            var translate: TranslateAnimation = if (isShow) {
                TranslateAnimation(
                    TranslateAnimation.ABSOLUTE,//以父layout为基准，指定具体位移像素值
                    0.0f,
                    TranslateAnimation.ABSOLUTE,
                    0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF,//以自己为基准，指定百分比
                    0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF,
                    -1.0f
                )
            } else {
                TranslateAnimation(
                    TranslateAnimation.ABSOLUTE,
                    0.0f,
                    TranslateAnimation.ABSOLUTE,
                    0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF,
                    -1.0f,
                    TranslateAnimation.RELATIVE_TO_SELF,
                    0.0f
                )
            }.apply {
                duration = 100
                fillAfter = true//设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
            }
            view.startAnimation(translate)
        }

        @SuppressLint("NewApi")
        fun getScreenHeight(): Float {
            var windowManager =
                BensApp.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            var rect = windowManager.currentWindowMetrics.bounds
            LogUtil.i("AnimationMan", "screen height: " + rect.height())
            return rect.height().toFloat()
        }
    }


}