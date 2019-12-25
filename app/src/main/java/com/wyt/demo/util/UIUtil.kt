package com.wyt.demo.util

import android.content.Context

object UIUtil {
    @JvmStatic
    fun dip2px(context: Context?, dpValue: Float): Int {
        assert(context != null)
        val scale = context!!.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}