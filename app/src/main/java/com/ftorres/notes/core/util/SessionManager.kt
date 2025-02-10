package com.ftorres.notes.core.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveUserSession(username: String) {
        sharedPreferences.edit().putString("USER_SESSION", username).apply()
    }

    fun getUserSession(): String? {
        return sharedPreferences.getString("USER_SESSION", null)
    }

    fun clearSession() {
        sharedPreferences.edit().remove("USER_SESSION").apply()
    }
}
