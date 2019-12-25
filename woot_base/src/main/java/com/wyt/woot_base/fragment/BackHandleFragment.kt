package com.wyt.woot_base.fragment

import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

/**
 * 在fragment监听activity回退
 * 2019.11.28 -> wyt
 */

abstract class BackHandleFragment : Fragment(){
    private lateinit var backHandleInterface: BackHandleInterface
    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    abstract fun onBackPressed(): Boolean

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is BackHandleInterface) {
            this.backHandleInterface = activity as BackHandleInterface
        } else {
            Log.e("wyt_error","Hosting Activity must implement BackHandledInterface")
        }
    }

    override fun onStart() {
        super.onStart()
        backHandleInterface.onSelectedFragment(this)
    }

}