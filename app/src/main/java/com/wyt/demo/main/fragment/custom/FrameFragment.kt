package com.wyt.demo.main.fragment.custom

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wyt.demo.R
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.frame_fragment.*
import kotlin.math.abs

class FrameFragment : BaseFragment() {

    companion object {
        fun newInstance() = FrameFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frame_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("框选工具")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {


        screenshot.setOnClickListener {
            fram.visibility = View.VISIBLE
            confirm.visibility = View.VISIBLE

        }
        confirm.setOnClickListener {
            fram.visibility = View.GONE
            confirm.visibility = View.GONE
            screenshot_ima.setImageBitmap(
                getCroppedImage(origin_ima.drawable as BitmapDrawable)
            )
        }

    }

    private fun getCroppedImage(drawable: Drawable): Bitmap? {
        if (drawable !is BitmapDrawable) {
            return null
        }
        val rectF = framSelectionView.screenShotRectF
        val matrixValues = FloatArray(9)
        origin_ima.imageMatrix.getValues(matrixValues)
        val scaleX = matrixValues[Matrix.MSCALE_X]
        val scaleY = matrixValues[Matrix.MSCALE_Y]
        val transX = matrixValues[Matrix.MTRANS_X]
        val transY = matrixValues[Matrix.MTRANS_Y]
        val bitmapLeft: Float = if (transX < 0) abs(transX) else 0f
        val bitmapTop: Float = if (transY < 0) abs(transY) else 0f
        val originalBitmap = drawable.bitmap

        val cropX = (bitmapLeft + rectF.left) / scaleX
        val cropY = (bitmapTop + rectF.top) / scaleY

        val cropWidth = Math.min((rectF.right - rectF.left) / scaleX, originalBitmap.width - cropX)
        val cropHeight =
            Math.min((rectF.bottom - rectF.top) / scaleY, originalBitmap.height - cropY)

        return Bitmap.createBitmap(
            originalBitmap,
            cropX.toInt(),
            cropY.toInt(),
            cropWidth.toInt(),
            cropHeight.toInt()
        )
    }


    override fun initViewModel() = Unit

    override fun openEventBus() = false
    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
