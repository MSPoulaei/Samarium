package ir.tehranshomal.samarium.Services

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SamariumPreferences", Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Add more methods for other data types as needed
}