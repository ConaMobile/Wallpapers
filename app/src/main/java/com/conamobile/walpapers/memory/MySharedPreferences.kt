package com.conamobile.walpapers.memory

import android.content.Context

class MySharedPreferences(context: Context) {
    private val pref = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

    fun isLoginPageBoolean(isLoginPageBoolean: Boolean) {
        val editor = pref.edit()
        editor.putBoolean("isLoginPageBoolean", isLoginPageBoolean)
        editor.apply()
    }

    fun getLoginPageBoolean(): Boolean {
        return pref.getBoolean("isLoginPageBoolean", false)
    }

    fun isSwitchSaverBoolean(isSwitchSaverBoolean: Boolean) {
        val editor = pref.edit()
        editor.putBoolean("isSwitchSaverBoolean", isSwitchSaverBoolean)
        editor.apply()
    }

    fun getSwitchSaverBoolean(): Boolean {
        return pref.getBoolean("isSwitchSaverBoolean", true)
    }

    fun isRealThemeManager(isRealThemeManager: Boolean) {
        val editor = pref.edit()
        editor.putBoolean("isRealThemeManager", isRealThemeManager)
        editor.apply()
    }

    fun getRealThemeManager(): Boolean {
        //false -> light mode
        return pref.getBoolean("isRealThemeManager", false)
    }

    fun isSavedAppLang(isSavedAppLang: String) {
        val editor = pref.edit()
        editor.putString("isSavedAppLang", isSavedAppLang)
        editor.apply()
    }

    fun getSavedAppLang(): String? {
        return pref.getString("isSavedAppLang", "other")
    }

    fun isSavedUserEmail(isSavedUserEmail: String) {
        val editor = pref.edit()
        editor.putString("isSavedUserEmail", isSavedUserEmail)
        editor.apply()
    }

    fun getSavedUserEmail(): String? {
        return pref.getString("isSavedUserEmail", "")
    }

    fun isSavedUserName(isSavedUserName: String) {
        val editor = pref.edit()
        editor.putString("isSavedUserName", isSavedUserName)
        editor.apply()
    }

    fun getSavedUserName(): String? {
        return pref.getString("isSavedUserName", "")
    }
}