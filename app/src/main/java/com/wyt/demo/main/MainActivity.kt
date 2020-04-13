package com.wyt.demo.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.hairun.net_info.SimInfoBean
import com.wyt.demo.R
import com.wyt.woot_base.activity.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                100
            )
        } else {
            SimInfoBean.startListener(true)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        SimInfoBean.stopListener()
    }




}
