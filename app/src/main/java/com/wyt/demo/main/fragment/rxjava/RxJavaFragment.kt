package com.wyt.demo.main.fragment.rxjava

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.wyt.demo.R
import com.wyt.demo.main.viewmodel.RxJavaViewModel
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.rx_java_fragment.*

class RxJavaFragment : BaseFragment() {

    companion object {
        fun newInstance() = RxJavaFragment()
    }

    private lateinit var viewModel: RxJavaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rx_java_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titleBar.setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
            .setTitle("RxJava应用")
    }

    override fun initView() {
        start.setOnClickListener {
            viewModel.initData()
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(RxJavaViewModel::class.java)
        viewModel.msgLiveData.observe(this, Observer {
            value.text= it
        })
    }

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
