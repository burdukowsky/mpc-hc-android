package tk.burdukowsky.mpc_hc_android

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object AppPreferences {

    private const val PREFERENCES_NAME = "appPreferences"
    private const val LAYOUT_KEY = "layout"
    private const val HOSTS_KEY = "hosts"
    private const val CURRENT_HOST_ID_KEY = "currentHostId"

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

    fun getHosts(): Map<String, String> {
        val defValue = "{}"
        val hosts = preferences.getString(HOSTS_KEY, defValue) ?: defValue
        val type: Type = object : TypeToken<Map<String, String>>() {}.type
        return GsonProvider.instance.fromJson(hosts, type)
    }

    fun setHosts(hosts: Map<String, String>) {
        val editor = preferences.edit()
        editor.putString(HOSTS_KEY, GsonProvider.instance.toJson(hosts))
        editor.apply()
    }

    fun getCurrentHostId(): String? {
        return preferences.getString(CURRENT_HOST_ID_KEY, null)
    }

    fun setCurrentHostId(id: String) {
        val editor = preferences.edit()
        editor.putString(CURRENT_HOST_ID_KEY, id)
        editor.apply()
    }

    fun getCurrentHost(): String? {
        val id = getCurrentHostId() ?: return null

        val hosts = getHosts()

        if (!hosts.containsKey(id)) {
            return null
        }

        return hosts[id]
    }

}
