package com.conamobile.walpapers.activity

import android.os.Bundle
import com.conamobile.walpapers.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        installAppLang()
    }
}