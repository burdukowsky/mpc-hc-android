package tk.burdukowsky.mpc_hc_android

import android.os.Bundle
import androidx.preference.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val hostsCategory = PreferenceCategory(context).apply {
            isPersistent = false
            key = "hosts_category"
            title = "Hosts"
        }
        screen.addPreference(hostsCategory)

        val settingsCategory = PreferenceCategory(context).apply {
            isPersistent = false
            key = "settings_category"
            title = "Settings"
        }
        screen.addPreference(settingsCategory)

        setupHostsPreferences(hostsCategory)
        setupAddHostPreference(hostsCategory)
        setupCurrentHostPreference(settingsCategory)

        preferenceScreen = screen
    }

    private fun setupHostsPreferences(category: PreferenceCategory) {
        val testHost = "192.168.0.74:42000"
        val editTextPreference = EditTextPreference(context).apply {
            isPersistent = false
            key = "host_1"
            title = testHost
            text = testHost
            order = 1
        }
        category.addPreference(editTextPreference)
    }

    private fun setupAddHostPreference(category: PreferenceCategory) {
        val addHostPreference = EditTextPreference(context).apply {
            isPersistent = false
            key = "add_host"
            title = "+ New host"
            order = 100
        }
        addHostPreference.setOnPreferenceChangeListener { _, newValue ->
            val newValueText = newValue.toString()
            category.addPreference(EditTextPreference(context).apply {
                isPersistent = false
                key = "host_2"
                title = newValueText
                text = newValueText
                order = 2
            })
            false
        }
        category.addPreference(addHostPreference)
    }

    private fun setupCurrentHostPreference(category: PreferenceCategory) {
        val testEntries = arrayOf("192.168.0.74:42000", "192.168.0.74:42001", "192.168.0.74:42002")
        val listPreference = ListPreference(context).apply {
            isPersistent = false
            key = "current_host"
            title = "Active host"
            order = 101
            entries = testEntries
            entryValues = testEntries
            value = testEntries[1]
            summary = "%s"
        }
        category.addPreference(listPreference)
    }

}