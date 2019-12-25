package com.wyt.demo.main.fragment.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources.getDrawable

import com.wyt.demo.R
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.wave_view_fragment.*

class WaveViewFragment : BaseFragment() {


    companion object {
        fun newInstance() = WaveViewFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wave_view_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("进度球")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {

        flowerView.setScore(seek_bar.progress)
        flowerView.setTextDesc("我的进度")

        vw_add_f.setOnClickListener {
            if (flowerView.f > 1024) {
                flowerView.f /= 2
            }
        }

        vw_less_f.setOnClickListener {
            if (flowerView.f < 8192) {
                flowerView.f *= 2
            }
        }

        waveView.setProgress(seek_bar.progress)//设置进度
            .setAboveWaveColor(Color.parseColor("#2be8c8")) //设置1波纹颜色
            .setBlowWaveColor(Color.parseColor("#d5faf4")) //设置2波纹颜色
            .setTextAttr(24) //设置字体大小


        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                waveView.setProgress(progress)
                flowerView.setScore(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seek_bar.thumbOffset = 15

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seek_bar.thumbOffset = 0
                seek_bar.thumb = getDrawable(context!!, R.drawable.slider_thumb_normal)
            }

        })
    }

    override fun initViewModel() = Unit

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
