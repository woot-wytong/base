package com.wyt.demo.main.fragment.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyt.demo.R
import com.wyt.demo.main.adapter.CustomAdapter
import com.wyt.demo.main.viewmodel.CustomViewModel
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.custom_fragment.*

class CustomFragment : BaseFragment() {


    companion object {
        fun newInstance() = CustomFragment()
    }

    private lateinit var viewModel: CustomViewModel
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) {

    }

    override fun initLogic() {

    }

    override fun initTitle() {
        titlebar.setTitle("自定义View")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {
        adapter = CustomAdapter()
        rv_custom.adapter = adapter
        rv_custom.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {
                    startFragment(R.id.action_customFragment_to_waveViewFragment)
                }
                1 -> {
                    startFragment(R.id.action_customFragment_to_ringDialFragment)
                }
                2 -> {
                    startFragment(R.id.action_customFragment_to_colorPickerFragment)
                }
                3 -> {
                    startFragment(R.id.action_customFragment_to_bubbleFragment)
                }
                4 -> {
                    startFragment(R.id.action_customFragment_to_searchFragment)
                }
                5 -> {
                    startFragment(R.id.action_customFragment_to_numberKeyboardFragment)
                }
                6 -> {
                    startFragment(R.id.action_customFragment_to_frameFragment)
                }
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CustomViewModel::class.java)
        viewModel.customsLiveData.observe(this, Observer {
            adapter.setNewData(it)
        })
        viewModel.initData()
    }

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit


}
