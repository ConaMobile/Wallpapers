package com.conamobile.walpapers.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.conamobile.walpapers.R
import com.conamobile.walpapers.activity.InternetActivity
import com.conamobile.walpapers.util.NetworkHelper

class CheckInternetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!hasInternet(context)) {
            Toast.makeText(context, context!!.getString(R.string.check_internet), Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, InternetActivity::class.java))
        }
    }

    private fun hasInternet(context: Context?): Boolean {
        val networkHelper = NetworkHelper(context!!)
        return networkHelper.isNetworkConnected()
    }
}