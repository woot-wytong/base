package com.wyt.demo.main.fragment.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wyt.demo.R
import com.wyt.demo.main.viewmodel.SearchViewModel
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : BaseFragment() {


    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titlebar.setTitle("搜索栏")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {

    }

    override fun initViewModel() {

    }

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
