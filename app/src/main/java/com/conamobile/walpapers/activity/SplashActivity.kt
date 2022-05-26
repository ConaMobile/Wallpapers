package com.conamobile.walpapers.activity

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.conamobile.walpapers.broadcast.CheckInternetReceiver
import com.google.android.gms.auth.api.signin.GoogleSignIn

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    lateinit var myCheckInternetReceiver: CheckInternetReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installRealTheme()
        installAppLang()
        myCheckInternetReceiver = CheckInternetReceiver()
        if (GoogleSignIn.getLastSignedInAccount(this) != null || mySharedPreferences.getLoginPageBoolean()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startMainActivity()
                installReceiver()
            }, 800)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startLoginActivity()
                installReceiver()
            }, 800)
        }
    }

    private fun installReceiver() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(myCheckInternetReceiver, intent)
    }
}