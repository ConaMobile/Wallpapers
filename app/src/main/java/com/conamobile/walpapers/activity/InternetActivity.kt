package com.conamobile.walpapers.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.conamobile.walpapers.R
import com.conamobile.walpapers.databinding.ActivityInternetBinding

class InternetActivity : BaseActivity() {
    private lateinit var binding: ActivityInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.main_theme)
        binding = ActivityInternetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        retryCardManager()
        backgroundCheckInternetAlways()
    }

    private fun retryCardManager() {
        binding.retryCardView.setOnClickListener {
            if (hasInternet()) {
                finish()
            } else {
                toast(getString(R.string.no_internet))
            }
        }
    }

    override fun onBackPressed() {
        if (hasInternet()) {
            super.onBackPressed()
        } else {
            finishAffinity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        thread.interrupt()
    }

    private fun backgroundCheckInternetAlways() {
        thread.start()
    }

    private val thread: Thread = object : Thread() {
        override fun run() {
            try {
                while (true) {
                    if (hasInternet()) {
                        finish()
                    }
                    sleep(1000)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}