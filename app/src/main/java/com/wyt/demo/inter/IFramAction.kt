package com.wyt.demo.inter

import android.graphics.RectF

interface IFramAction {
    fun onActionStart()
    fun onActionStop(rectF: RectF?)
}