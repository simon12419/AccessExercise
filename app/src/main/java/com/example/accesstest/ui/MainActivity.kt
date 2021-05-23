package com.example.accesstest.ui

import android.os.Bundle
import com.example.accesstest.R
import com.example.swipertest.ui.base.BaseActivity

class MainActivity: BaseActivity<MainViewModel>(MainViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}