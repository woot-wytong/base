package com.wyt.demo.util

import android.graphics.PointF
import com.wyt.demo.view.CropWindowEdgeSelector

/***
 * 捕获手指在裁剪框的哪一条边
 */
object CatchEdgeUtil {
    /**
     * 判断手指是否的位置是否在有效的缩放区域：缩放区域的半径为targetRadius
     * 缩放区域使指：裁剪框的四个角度或者四条边，当手指位置处在某个角
     * 或者某条边的时候，则随着手指的移动对裁剪框进行缩放操作。
     * 如果手指位于裁剪框的内部，则裁剪框随着手指的移动而只进行移动操作。
     * 否则可以判定手指距离裁剪框较远而什么都不做
     */
    @JvmStatic
    fun getPressedHandle(
        x: Float,
        y: Float,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        targetRadius: Float
    ): CropWindowEdgeSelector? {
        var nearestCropWindowEdgeSelector: CropWindowEdgeSelector? = null
        //判断手指距离裁剪框哪一个角最近
//最近距离默认正无穷大
        var nearestDistance = Float.POSITIVE_INFINITY
        //////////判断手指是否在图二种的A位置：四个角之一/////////////////
//计算手指距离左上角的距离
        val distanceToTopLeft = calculateDistance(x, y, left, top)
        if (distanceToTopLeft < nearestDistance) {
            nearestDistance = distanceToTopLeft
            nearestCropWindowEdgeSelector = CropWindowEdgeSelector.TOP_LEFT
        }
        //计算手指距离右上角的距离
        val distanceToTopRight = calculateDistance(x, y, right, top)
        if (distanceToTopRight < nearestDistance) {
            nearestDistance = distanceToTopRight
            nearestCropWindowEdgeSelector = CropWindowEdgeSelector.TOP_RIGHT
        }
        //计算手指距离左下角的距离
        val distanceToBottomLeft = calculateDistance(x, y, left, bottom)
        if (distanceToBottomLeft < nearestDistance) {
            nearestDistance = distanceToBottomLeft
            nearestCropWindowEdgeSelector = CropWindowEdgeSelector.BOTTOM_LEFT
        }
        //计算手指距离右下角的距离
        val distanceToBottomRight =
            calculateDistance(x, y, right, bottom)
        if (distanceToBottomRight < nearestDistance) {
            nearestDistance = distanceToBottomRight
            nearestCropWindowEdgeSelector = CropWindowEdgeSelector.BOTTOM_RIGHT
        }
        //如果手指选中了一个最近的角，并且在缩放范围内则返回这个角
        if (nearestDistance <= targetRadius) {
            return nearestCropWindowEdgeSelector
        }
        //////////判断手指是否在图二种的C位置：四个边的某条边/////////////////
        if (isInHorizontalTargetZone(x, y, left, right, top, targetRadius)) {
            return CropWindowEdgeSelector.TOP //说明手指在裁剪框top区域
        } else if (isInHorizontalTargetZone(
                x,
                y,
                left,
                right,
                bottom,
                targetRadius
            )
        ) {
            return CropWindowEdgeSelector.BOTTOM //说明手指在裁剪框bottom区域
        } else if (isInVerticalTargetZone(x, y, left, top, bottom, targetRadius)) {
            return CropWindowEdgeSelector.LEFT //说明手指在裁剪框left区域
        } else if (isInVerticalTargetZone(x, y, right, top, bottom, targetRadius)) {
            return CropWindowEdgeSelector.RIGHT //说明手指在裁剪框right区域
        }
        //////////判断手指是否在图二种的B位置：裁剪框的中间/////////////////
        return if (isWithinBounds(x, y, left, top, right, bottom)) {
            CropWindowEdgeSelector.CENTER
        } else null
        ////////手指位于裁剪框的D位置，此时移动手指什么都不做/////////////
    }

    @JvmStatic
    fun getOffset(
        cropWindowEdgeSelector: CropWindowEdgeSelector,
        x: Float,
        y: Float,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        touchOffsetOutput: PointF
    ) {
        var touchOffsetX = 0f
        var touchOffsetY = 0f
        when (cropWindowEdgeSelector) {
            CropWindowEdgeSelector.TOP_LEFT -> {
                touchOffsetX = left - x
                touchOffsetY = top - y
            }
            CropWindowEdgeSelector.TOP_RIGHT -> {
                touchOffsetX = right - x
                touchOffsetY = top - y
            }
            CropWindowEdgeSelector.BOTTOM_LEFT -> {
                touchOffsetX = left - x
                touchOffsetY = bottom - y
            }
            CropWindowEdgeSelector.BOTTOM_RIGHT -> {
                touchOffsetX = right - x
                touchOffsetY = bottom - y
            }
            CropWindowEdgeSelector.LEFT -> {
                touchOffsetX = left - x
                touchOffsetY = 0f
            }
            CropWindowEdgeSelector.TOP -> {
                touchOffsetX = 0f
                touchOffsetY = top - y
            }
            CropWindowEdgeSelector.RIGHT -> {
                touchOffsetX = right - x
                touchOffsetY = 0f
            }
            CropWindowEdgeSelector.BOTTOM -> {
                touchOffsetX = 0f
                touchOffsetY = bottom - y
            }
            CropWindowEdgeSelector.CENTER -> {
                val centerX = (right + left) / 2
                val centerY = (top + bottom) / 2
                touchOffsetX = centerX - x
                touchOffsetY = centerY - y
            }
        }
        touchOffsetOutput.x = touchOffsetX
        touchOffsetOutput.y = touchOffsetY
    }

    private fun isInHorizontalTargetZone(
        x: Float,
        y: Float,
        handleXStart: Float,
        handleXEnd: Float,
        handleY: Float,
        targetRadius: Float
    ): Boolean {
        return x > handleXStart && x < handleXEnd && Math.abs(y - handleY) <= targetRadius
    }

    private fun isInVerticalTargetZone(
        x: Float,
        y: Float,
        handleX: Float,
        handleYStart: Float,
        handleYEnd: Float,
        targetRadius: Float
    ): Boolean {
        return Math.abs(x - handleX) <= targetRadius && y > handleYStart && y < handleYEnd
    }

    private fun isWithinBounds(
        x: Float,
        y: Float,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ): Boolean {
        return x >= left && x <= right && y >= top && y <= bottom
    }

    /**
     * 计算 (x1, y1) 和 (x2, y2)两个点的距离
     */
    private fun calculateDistance(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ): Float {
        val side1 = x2 - x1
        val side2 = y2 - y1
        return Math.sqrt(side1 * side1 + side2 * side2.toDouble()).toFloat()
    }
}