package tk.burdukowsky.mpc_hc_android

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object AppPreferencesProvider {

    private const val preferencesName = "appPreferences"
    private val preferences: SharedPreferences =
        App.instance.getSharedPreferences(preferencesName, MODE_PRIVATE)

}
