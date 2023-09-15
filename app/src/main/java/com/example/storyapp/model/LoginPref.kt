package com.example.storyapp.model

import android.content.Context

class LoginPref(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setToken(token: String) {
        val edit = preferences.edit()
        edit.putString(TOKEN, token)
        edit.apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun clearToken() {
        val edit = preferences.edit().clear()
        edit.apply()
    }

    companion object {
        const val PREF_NAME = "login_pref"
        const val TOKEN = "token"
    }
}