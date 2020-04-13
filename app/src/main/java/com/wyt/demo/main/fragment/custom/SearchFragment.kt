package com.wyt.demo.main.fragment.custom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wyt.demo.R
import com.wyt.demo.main.viewmodel.SearchViewModel
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.eventbus.EventCenter
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
        search.addTextChangeListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        val list = ArrayList<String>()
        list.add("了啦有噶就是")
        list.add("暗杀计划的")
        list.add("爱丽丝")
        list.add("夫噶市场大幅")
        list.add("啊实打实")
        list.add("自行车自行车")
        list.add("从v不错v")
        list.add("不能明白你")
        list.add("暗杀计划的")
        list.add("爱丽丝")
        list.add("夫噶市场大幅")
        list.add("啊实打实")
        list.add("自行车自行车")
        list.add("从v不错v")
        list.add("不能明白你")
        search.setData(list)
    }

    override fun initViewModel() {

    }

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
