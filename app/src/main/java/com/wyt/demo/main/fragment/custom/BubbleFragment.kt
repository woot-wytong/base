package com.wyt.demo.main.fragment.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wyt.demo.R
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.eventbus.EventCenter
import kotlinx.android.synthetic.main.bubble_fragment.*

class BubbleFragment : BaseFragment() {


    companion object {
        fun newInstance() = BubbleFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bubble_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("气泡扩散/收缩")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {
        initAirFoamView(0, 500)
        button.setOnClickListener {
            initAirFoamView(0, 500)
        }
        button2.setOnClickListener {
            initAirFoamView(1, 800)
        }
        button3.setOnClickListener {
            initAirFoamView(2, 1200)
        }

    }



    override fun initViewModel() = Unit

    override fun openEventBus(): Boolean = false

    override fun onBack(): Boolean = false

    override fun onEventBus(event: EventCenter<Any>)  = Unit

    private fun initAirFoamView(int: Int, pm25: Int) {
        when (int) {
            0 -> {
                airFoamView.setCity("北京")
                    .setGrade("优")
                    .setPm25(pm25)
                    .setRingColor(Color.parseColor("#B5E1FF"))
                    .setFilterLife("80%")
                    .addBubbleObject(30, 181, 225, 255, true)
            }
            1 -> {
                airFoamView.setCity("太原")
                    .setGrade("中")
                    .setPm25(pm25)
                    .setRingColor(Color.parseColor("#F7E47B"))
                    .setFilterLife("70%")
                    .addBubbleObject(30, 247, 228, 123, false)
            }
            2 -> {
                airFoamView.setCity("沈阳")
                    .setGrade("差")
                    .setPm25(pm25)
                    .setRingColor(Color.parseColor("#FF8686"))
                    .setFilterLife("50%")
                    .addBubbleObject(30, 255, 134, 134, false)
            }
        }
    }
}
