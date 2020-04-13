package com.wyt.demo.main.fragment.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.wyt.demo.R
import com.wyt.demo.view.ScaleDialView
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.eventbus.EventCenter
import kotlinx.android.synthetic.main.ring_dial_fragment.*

class RingDialFragment : BaseFragment() {

    private val onAngleColorListener =
        ScaleDialView.OnAngleColorListener { red, green -> val c = Color.argb(150, red, green, 0) }

    companion object {
        fun newInstance() = RingDialFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ring_dial_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("环形刻度表盘")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {

        scaleDialView.setInitConfig(ScaleDialView.PRECISION_MODE_INTEGER, seek_bar.max.toFloat(), "温度", "℃")
        scaleDialView.setOnAngleColorListener(onAngleColorListener)//颜色监听器 可自行设置
        scaleDialView.setChange(seek_bar.progress*300/seek_bar.max.toFloat())

        ring_dial.value = seek_bar.progress.toFloat()
        seek_bar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ring_dial.value = progress.toFloat()
                scaleDialView.setChange(progress*300/seek_bar.max.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seek_bar.thumbOffset = 15
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seek_bar.thumbOffset = 0
                seek_bar.thumb = getDrawable(context!!,R.drawable.slider_thumb_normal)
            }

        })
    }

    override fun initViewModel() = Unit

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit
}
