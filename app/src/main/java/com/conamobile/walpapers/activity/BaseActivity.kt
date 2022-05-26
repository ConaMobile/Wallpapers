package com.conamobile.walpapers.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.conamobile.walpapers.memory.MySharedPreferences
import com.conamobile.walpapers.util.NetworkHelper
import java.util.*

open class BaseActivity : AppCompatActivity() {
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySharedPreferences = MySharedPreferences(this)
        networkHelper = NetworkHelper(this)
    }


    fun installAppLang() {
        when (mySharedPreferences.getSavedAppLang()) {
            "en" -> {
                setLocale("en")
            }
            "ru" -> {
                setLocale("ru")
            }
            "uz" -> {
                setLocale("uz")
            }
            else -> {
                getDefaultLanguage()
            }
        }
    }

    private fun getDefaultLanguage() {
        Locale.getDefault().displayLanguage
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext?.resources?.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    fun installRealTheme() {
        if (mySharedPreferences.getRealThemeManager())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun hasInternet(): Boolean {
        return networkHelper.isNetworkConnected()
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}