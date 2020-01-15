package com.wyt.demo.inter

import android.graphics.RectF

interface IFrameAction {
    fun onActionStart()
    fun onActionStop(rectF: RectF?)
}