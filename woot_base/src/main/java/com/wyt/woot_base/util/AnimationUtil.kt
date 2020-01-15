package com.wyt.woot_base.util

import android.view.animation.Animation
import android.view.animation.TranslateAnimation

object AnimationUtil {
    fun showAnimation(duration:Long): TranslateAnimation {
        val showAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        )
        showAnim.duration = duration
        return showAnim
    }

    fun hideAnimation(duration:Long): TranslateAnimation {
        val hideAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        )
        hideAnim.duration = duration
        return hideAnim
    }
}