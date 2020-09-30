package tk.burdukowsky.mpc_hc_android

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object AppPreferences {

    private const val PREFERENCES_NAME = "appPreferences"
    private const val LAYOUT_KEY = "layout"
    private const val HOSTS_KEY = "hosts"
    private const val HOSTS_DELIMITER = ","
    private const val CURRENT_HOST_INDEX_KEY = "currentHostIndex"
    private const val CURRENT_HOST_UNDEFINED = -1

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

    fun getHosts(): List<String> {
        val hosts = preferences.getString(HOSTS_KEY, "") ?: ""
        return hosts.split(HOSTS_DELIMITER)
    }

    fun setHosts(hosts: List<String>) {
        val editor = preferences.edit()
        editor.putString(HOSTS_KEY, hosts.joinToString(separator = HOSTS_DELIMITER))
        editor.apply()
    }

    fun getCurrentHostIndex(): Int {
        return preferences.getInt(CURRENT_HOST_INDEX_KEY, CURRENT_HOST_UNDEFINED)
    }

    fun setCurrentHostIndex(index: Int) {
        val editor = preferences.edit()
        editor.putInt(CURRENT_HOST_INDEX_KEY, index)
        editor.apply()
    }

    fun getCurrentHost(): String? {
        val index = getCurrentHostIndex()

        if (index == CURRENT_HOST_UNDEFINED) {
            return null
        }

        val hosts = getHosts()

        if (index >= hosts.size) {
            return null
        }

        return hosts[index]
    }

}
