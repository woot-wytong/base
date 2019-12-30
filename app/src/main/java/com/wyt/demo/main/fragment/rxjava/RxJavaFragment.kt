package com.wyt.demo.main.fragment.rxjava

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson

import com.wyt.demo.R
import com.wyt.demo.main.viewmodel.RxJavaViewModel
import com.wyt.demo.room.AppDataBase
import com.wyt.demo.room.bean.User
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.rx_java_fragment.*

class RxJavaFragment : BaseFragment() {

    companion object {
        fun newInstance() = RxJavaFragment()
    }

    private lateinit var viewModel: RxJavaViewModel

    val adapter = object : BaseQuickAdapter<User, BaseViewHolder>(R.layout.item_user) {
        override fun convert(helper: BaseViewHolder, item: User) {
            helper.setText(R.id.first_name, item.firstName)
            helper.setText(R.id.last_name, item.lastName)
            helper.setText(R.id.age, item.age.toString())
            helper.setText(R.id.sex, item.sex)
        }
    }

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

    @SuppressLint("CheckResult")
    override fun initView() {
        start.setOnClickListener {
            //            viewModel.initData()
            AppDataBase.default.rxDao().addUser(
                User(7, "lajkshd", "lkasdha", "男", 19)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        }
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter

        AppDataBase.default.rxDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.setNewData(it)
            }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(RxJavaViewModel::class.java)
        viewModel.msgLiveData.observe(this, Observer {
            value.text = it
        })
    }

    override fun openEventBus() = false

    override fun onBack() = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
