package com.conamobile.walpapers.activity

import android.os.Bundle
import com.conamobile.walpapers.R

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (!myCheckInternetReceiver.hasInternet(this)) {
//            startInternetActivity()
//        }
        setContentView(R.layout.activity_login)
        installAppLang()
    }
}