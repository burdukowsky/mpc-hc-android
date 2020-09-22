package tk.burdukowsky.mpc_hc_android

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object AppPreferences {

    private const val PREFERENCES_NAME = "appPreferences"
    private const val LAYOUT_KEY = "layout"

    private val preferences: SharedPreferences =
        App.instance.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)


    fun getLayout(): Layout {
        val layoutString = preferences.getString(LAYOUT_KEY, Layout.default.name)
        return if (layoutString != null) Layout.toLayout(layoutString) else Layout.default
    }

    fun setLayout(layout: Layout) {
        val editor = preferences.edit()
        editor.putString(LAYOUT_KEY, layout.name)
        editor.apply()
    }

}
