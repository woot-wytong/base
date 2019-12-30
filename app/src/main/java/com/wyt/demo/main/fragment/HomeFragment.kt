package com.wyt.demo.main.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.wyt.demo.R
import com.wyt.demo.main.adapter.HomeAdapter
import com.wyt.demo.main.viewmodel.HomeViewModel
import com.wyt.demo.other.MyDividerItem
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private var isExit = false
    private lateinit var adapter: HomeAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titleBar.setTitle("Demo")
    }

    override fun initView() {
        val width = ScreenUtils.getScreenWidth()
        val spanCount = 4  //列数
        val leftRight = 30 //左右间隔
        val topBottom = 5  //上下间隔
        adapter = HomeAdapter(width, leftRight, spanCount)
        rv_content.adapter = adapter
        rv_content.layoutManager = GridLayoutManager(
            context, spanCount,
            LinearLayoutManager.VERTICAL, false
        )
        rv_content.addItemDecoration(MyDividerItem(leftRight, topBottom))


        adapter.setOnItemClickListener { _, _, position ->
            when(position){
                0->{
                    startFragment(R.id.action_homeFragment_to_customFragment)
                }
                1->{
                    startFragment(R.id.action_homeFragment_to_byteFragment)
                }
                2->{
                    startFragment(R.id.action_homeFragment_to_rxJavaFragment)
                }
            }
        }

    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.homeItemsLiveData.observe(this, Observer {
            adapter.setNewData(it)
        })
        viewModel.initData()
    }

    override fun openEventBus() = false

    override fun onBack(): Boolean {
        if (isExit) {
            AppUtils.exitApp()
        } else {
            ToastUtils.showLong("再按一次退出")
            isExit = true
            Handler().postDelayed({ isExit = false }, 2000)
        }
        return true
    }

    override fun onEventBus(event: EventCenter<Any>) = Unit


}
