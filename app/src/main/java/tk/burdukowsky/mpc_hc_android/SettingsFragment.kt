package tk.burdukowsky.mpc_hc_android

import android.content.Context
import android.os.Bundle
import androidx.preference.*

class SettingsFragment : PreferenceFragmentCompat() {

    private val addHostPreferenceOrder = 1000
    private val currentHostPreferenceOrder = 1001
    private val layoutPreferenceOrder = 1002

    private val hostPlaceholder = "192.168.0.1:13579"

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
            title = resources.getString(R.string.settings_category_hosts)
        }
        screen.addPreference(hostsCategory)

        val settingsCategory = PreferenceCategory(context).apply {
            isPersistent = false
            key = "settings_category"
            title = resources.getString(R.string.settings_category_settings)
        }
        screen.addPreference(settingsCategory)

        setupHostsPreferences(hostsCategory)
        setupAddHostPreference(hostsCategory)
        currentHostPreference = getCurrentHostPreference()
        settingsCategory.addPreference(currentHostPreference)
        settingsCategory.addPreference(getLayoutPreference())

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
            dialogTitle = resources.getString(R.string.settings_preference_host_dialog_title)
            order = number
            setOnBindEditTextListener { it.hint = hostPlaceholder }
        }

        preference.setOnPreferenceChangeListener { _, newValue ->
            val newHost = newValue.toString().trim()
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
            title = resources.getString(R.string.settings_preference_add_host_title)
            dialogTitle = resources.getString(R.string.settings_preference_add_host_dialog_title)
            order = addHostPreferenceOrder
            setOnBindEditTextListener { it.hint = hostPlaceholder }
        }
        addHostPreference.setOnPreferenceChangeListener { _, newValue ->
            val newHost = newValue.toString().trim()
            val newId = IdGenerator.next()

            hosts[newId] = newHost
            AppPreferences.setHosts(hosts)

            category.addPreference(getHostPreference(hosts.size, newHost, newId, category))

            updateCurrentHostPreferenceState(currentHostPreference)

            if (
                currentHostPreference.value.isNullOrEmpty() ||
                !hosts.containsKey(currentHostPreference.value)
            ) {
                currentHostPreference.value = newId
                this.onCurrentHostPreferenceChange(newId)
            }

            false
        }
        category.addPreference(addHostPreference)
    }

    private fun getCurrentHostPreference(): ListPreference {
        val preference = ListPreference(context).apply {
            isPersistent = false
            key = "current_host"
            title = resources.getString(R.string.settings_preference_active_host_title)
            dialogTitle = resources.getString(R.string.settings_preference_active_host_dialog_title)
            order = currentHostPreferenceOrder
            value = AppPreferences.getCurrentHostId()
            summaryProvider =
                Preference.SummaryProvider { preference: ListPreference -> hosts[preference.value] }
        }
        updateCurrentHostPreferenceState(preference)

        preference.setOnPreferenceChangeListener { _, newValue ->
            this.onCurrentHostPreferenceChange(newValue.toString())
            true
        }

        return preference
    }

    private fun onCurrentHostPreferenceChange(newValue: String) {
        AppPreferences.setCurrentHostId(newValue)
        updateFab()
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

    private fun getLayoutPreference(): ListPreference {
        val preference = ListPreference(context).apply {
            isPersistent = false
            key = "layout"
            title = resources.getString(R.string.settings_preference_layout_title)
            dialogTitle = resources.getString(R.string.settings_preference_layout_dialog_title)
            order = layoutPreferenceOrder
            entries = resources.getStringArray(R.array.layouts)
            entryValues = Layout.values().map { layout -> layout.name }.toTypedArray()
            value = AppPreferences.getLayout().name
            summaryProvider =
                Preference.SummaryProvider { preference: ListPreference ->
                    Layout.toLayout(preference.value).getResource()
                }
        }

        preference.setOnPreferenceChangeListener { _, newValue ->
            AppPreferences.setLayout(Layout.toLayout(newValue.toString()))
            true
        }

        return preference
    }

}