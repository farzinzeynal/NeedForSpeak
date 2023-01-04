package az.needforspeak.utils.helpers

import android.content.Context
import android.content.SharedPreferences
import az.needforspeak.utils.SharedTypes


class MainSharedPrefs(context: Context?, @SharedTypes.SharedTypesDef sharedType: String) {

    val prefs: SharedPreferences? = context?.getSharedPreferences(sharedType, Context.MODE_PRIVATE)

    fun <T> set(item: String, value: T) {
        val editor = prefs?.edit()

        when (value) {
            is Int -> {
                editor?.putInt(item, value)
            }
            is String -> {
                editor?.putString(item, value)
            }
            is Boolean -> {
                editor?.putBoolean(item, value)
            }
            else -> {
                editor?.putString(item, value as String)
            }
        }
        editor?.apply()
    }


    inline fun <reified T> get(item: String, default: T): T? {
        return when (T::class) {
            Int::class -> {
                prefs?.getInt(item, default as Int) as T?
            }
            String::class -> {
                prefs?.getString(item, default as String) as T?
            }
            Boolean::class -> {
                prefs?.getBoolean(item, default as Boolean) as T?
            }
            else -> {
                prefs?.getString(item, default as String) as T?
            }
        }
    }

    fun contains(prName: String) = prefs?.contains(prName)

    fun clearSharedPrefs() {
        val editor = prefs?.edit()
        editor?.clear()
        editor?.apply()
    }

    fun removeSharedPrefs(prName: String) {
        val editor = prefs?.edit()
        editor?.remove(prName)
        editor?.apply()
    }
}
