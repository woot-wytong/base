package com.wyt.demo.main.fragment.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wyt.demo.R
import com.wyt.demo.view.NumberKeyboardView
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.number_keyboard_fragment.*

class NumberKeyboardFragment : BaseFragment() {


    companion object {
        fun newInstance() = NumberKeyboardFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.number_keyboard_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("数字输入框")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {
        numberKeyboardView.onNumberClickListener =
            object : NumberKeyboardView.OnNumberClickListener {
                override fun onNumberReturn(number: String) {
                    if (number == "C") {
                        txt_number.text = ""
                    } else {
                        txt_number.append(number)
                    }
                }

                override fun onNumberDelete() {
                    val str = txt_number.text.toString()
                    if (str.isNotEmpty()) {
                        txt_number.text = str.substring(0, txt_number.text.lastIndex)
                    }
                }
            }

    }

    override fun initViewModel() = Unit

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
