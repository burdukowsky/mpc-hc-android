package tk.burdukowsky.mpc_hc_android

import android.content.Context
import android.os.Bundle
import androidx.preference.*

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mainActivity: MainActivity

    private var hosts: MutableMap<String, String> = mutableMapOf()
    private lateinit var currentHostPreference: ListPreference

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
        mainActivity.setOptionsMenuVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.setFabVisibility(false)
        mainActivity.setOptionsMenuVisibility(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        hosts.putAll(AppPreferences.getHosts())
        updateFab()

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
        currentHostPreference = getCurrentHostPreference()
        settingsCategory.addPreference(currentHostPreference)

        preferenceScreen = screen
    }

    private fun setupHostsPreferences(category: PreferenceCategory) {
        var number = 1
        for ((id, host) in hosts) {
            category.addPreference(getHostPreference(number, host, id, category))
            number++
        }
    }

    private fun getHostPreference(
        number: Int,
        host: String,
        id: String,
        category: PreferenceCategory
    ): EditTextPreference {
        val preference = EditTextWithButtonPreference(context).apply {
            isPersistent = false
            key = "host_$number"
            title = host
            text = host
            order = number
        }

        preference.setOnPreferenceChangeListener { _, newValue ->
            val newHost = newValue.toString()
            preference.title = newHost

            hosts[id] = newHost
            AppPreferences.setHosts(hosts)

            updateCurrentHostPreferenceState(currentHostPreference)

            true
        }

        preference.setOnButtonClickListener {
            ConfirmationDialogFragment(
                positiveOnClickListener = {
                    hosts.remove(id)
                    AppPreferences.setHosts(hosts)

                    category.removePreference(preference)

                    updateCurrentHostPreferenceState(currentHostPreference)
                    updateFab()
                }
            ).show(childFragmentManager, "deleteHostConfirmationDialog")
        }

        return preference
    }

    private fun setupAddHostPreference(category: PreferenceCategory) {
        val addHostPreference = EditTextPreference(context).apply {
            isPersistent = false
            key = "add_host"
            title = "+ New host"
            order = 100
        }
        addHostPreference.setOnPreferenceChangeListener { _, newValue ->
            val newHost = newValue.toString()
            val newId = IdGenerator.next()

            hosts[newId] = newHost
            AppPreferences.setHosts(hosts)

            category.addPreference(getHostPreference(hosts.size, newHost, newId, category))

            updateCurrentHostPreferenceState(currentHostPreference)

            false
        }
        category.addPreference(addHostPreference)
    }

    private fun getCurrentHostPreference(): ListPreference {
        val preference = ListPreference(context).apply {
            isPersistent = false
            key = "current_host"
            title = "Active host"
            order = 101
            value = AppPreferences.getCurrentHostId()
            summaryProvider =
                Preference.SummaryProvider { preference: ListPreference -> hosts[preference.value] }
        }
        updateCurrentHostPreferenceState(preference)

        preference.setOnPreferenceChangeListener { _, newValue ->
            AppPreferences.setCurrentHostId(newValue.toString())
            updateFab()
            true
        }

        return preference
    }

    private fun updateCurrentHostPreferenceState(preference: ListPreference) {
        preference.entries = hosts.values.toTypedArray()
        preference.entryValues = hosts.keys.toTypedArray()
        preference.isEnabled = hosts.isNotEmpty()
        preference.summaryProvider = preference.summaryProvider // force update summary
    }

    private fun updateFab() {
        val currentHostId = AppPreferences.getCurrentHostId()
        mainActivity.setFabVisibility(currentHostId != null && hosts.containsKey(currentHostId))
    }

}