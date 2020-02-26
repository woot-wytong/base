package com.wyt.woot_base.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseFragmentNoBack :Fragment(){

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            getBundleExtras(arguments!!)
        }

        if (openEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        initLogic()
        initTitle()
        initView()
        initViewModel()
    }

    //获取bundle信息
    protected abstract fun getBundleExtras(bundle: Bundle)

    // 初始化逻辑
    abstract fun initLogic()

    //初始化titleBar
    abstract fun initTitle()

    //初始化View
    abstract fun initView()

    //初始化viewModel
    abstract fun initViewModel()

    //是否开启EventBus
    abstract fun openEventBus(): Boolean

    /**
     * EventBus接收信息的方法，开启后才会调用
     *
     * @param event
     */
    abstract fun onEventBus(event: EventCenter<Any>)

    /**
     * EventBus接收消息
     *
     * @param event 消息接收
     */
    @Subscribe
    fun onEventMainThread(event: EventCenter<Any>?) {
        if (event != null) {
            onEventBus(event)
        }
    }

    /**
     * 在UI线程中运行
     */
    fun runOnUiThread(runnable: Runnable) {
        if (activity != null) {
            activity!!.runOnUiThread(runnable)
        }
    }

    fun onBackThis(){
        if (activity != null) {
            activity!!.onBackPressed()
        }
    }

    /**
     * 跳转fragment
     */
    fun startFragment(actionId: Int, bundle: Bundle) {
        Navigation.findNavController(view!!)
            .navigate(actionId, bundle)
    }

    /**
     * 跳转fragment
     */
    fun startFragment(actionId: Int) {
        Navigation.findNavController(view!!)
            .navigate(actionId, null)
    }



    override fun onDestroy() {
        super.onDestroy()
        if (openEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}