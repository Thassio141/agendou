package br.com.agendou.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferences(context: Context) {
    
    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    fun saveLoginCredentials(email: String, password: String) {
        preferences.edit {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putBoolean(KEY_REMEMBER_ME, true)
        }
    }
    
    fun clearLoginCredentials() {
        preferences.edit {
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            putBoolean(KEY_REMEMBER_ME, false)
        }
    }
    
    fun hasCredentials(): Boolean {
        return preferences.getBoolean(KEY_REMEMBER_ME, false) &&
                !preferences.getString(KEY_EMAIL, "").isNullOrEmpty() &&
                !preferences.getString(KEY_PASSWORD, "").isNullOrEmpty()
    }
    
    fun getEmail(): String {
        return preferences.getString(KEY_EMAIL, "") ?: ""
    }
    
    fun getPassword(): String {
        return preferences.getString(KEY_PASSWORD, "") ?: ""
    }
    
    companion object {
        private const val PREF_NAME = "agendou_preferences"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_PASSWORD = "user_password"
        private const val KEY_REMEMBER_ME = "remember_me"
    }
} 