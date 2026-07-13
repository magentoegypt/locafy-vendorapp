package magentoegypt.locafy.addons.advance_Report.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(var activity: Context) {

    companion object {
        const val FILTER_KEY_FROM_DATE = "fromDate"
        const val FILTER_KEY_TO_DATE = "toDate"
        const val FILTER_KEY_VENDOR_NAME = "vendorName"
    }

    private val sharedPrefFile = "userPrefs"
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)


    @SuppressLint("CommitPrefEdits")
    fun addStringItem(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    @SuppressLint("CommitPrefEdits")
    fun addIntItem(key: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
        editor.commit()
    }

    fun getStringItem(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getIntItem(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }
}