package com.wyt.demo.main.fragment.custom

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wyt.demo.R
import com.wyt.demo.view.ColorPickerView
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.eventbus.EventCenter
import kotlinx.android.synthetic.main.color_picker_fragment.*

class ColorPickerFragment : BaseFragment() {

    companion object {
        fun newInstance() = ColorPickerFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.color_picker_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("颜色选择器")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {
        picker1.setOnColorPickerChangeListener(object :
            ColorPickerView.OnColorPickerChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onColorChanged(picker: ColorPickerView, color: Int) {

                if (picker !== picker1) {
                    picker1.setColors(Color.TRANSPARENT, color)
                }
                show.setBackgroundColor(color)

                val s =
                    "a: " + Color.alpha(color) + "  r: " + Color.red(color) + "  g: " + Color.green(
                        color
                    ) + "  b: " + Color.blue(
                        color
                    )
                textView.text = s

            }

            override fun onStartTrackingTouch(picker: ColorPickerView) {

            }

            override fun onStopTrackingTouch(picker: ColorPickerView) {

            }
        })
    }

    override fun initViewModel() = Unit

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
