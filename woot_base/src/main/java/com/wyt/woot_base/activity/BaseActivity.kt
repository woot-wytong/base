package com.wyt.woot_base.activity

import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AdaptScreenUtils
import com.wyt.woot_base.fragment.BackHandleFragment
import com.wyt.woot_base.fragment.BackHandleInterface

/**
 * BaseActivity,处理屏幕适配，物理回退拦截
 * 2019.11.28 -> wyt
 */
abstract class BaseActivity : AppCompatActivity(), BackHandleInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 屏幕适配
     */
    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptHeight(super.getResources(), 1280)

    }

    // 物理回退拦截
    private lateinit var backHandleFragment: BackHandleFragment

    override fun onSelectedFragment(backHandleFragment: BackHandleFragment) {
        this.backHandleFragment = backHandleFragment;
    }

    override fun onBackPressed() {
        if (!backHandleFragment.onBackPressed()) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                super.onBackPressed()
            } else {
                supportFragmentManager.popBackStack();
            }
        }
    }


}