package com.wyt.demo.main

import android.os.Bundle
import com.wyt.demo.R
import com.wyt.woot_base.activity.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
