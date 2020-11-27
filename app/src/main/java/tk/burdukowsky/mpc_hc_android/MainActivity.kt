package tk.burdukowsky.mpc_hc_android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var fab: FloatingActionButton
    private var optionsMenuEnabled = true
    private var notificationServiceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            val id = when (AppPreferences.getLayout()) {
                Layout.SIMPLE -> R.id.action_settings_to_simple
                Layout.STRETCH -> R.id.action_settings_to_stretch
            }
            navController.navigate(id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (optionsMenuEnabled) {
            menuInflater.inflate(R.menu.menu_main, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.SettingsFragment -> {
                NavigationUI.onNavDestinationSelected(item, navController)
                super.onOptionsItemSelected(item)
            }
            R.id.notification_on -> {
                setNotificationStatus(true)
                true
            }
            R.id.notification_off -> {
                setNotificationStatus(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun setOptionsMenuVisibility(visibility: Boolean) {
        optionsMenuEnabled = visibility
        invalidateOptionsMenu()
    }

    fun setFabVisibility(visibility: Boolean) {
        if (visibility) fab.show() else fab.hide()
    }

    private fun setNotificationStatus(status: Boolean) {
        if (notificationServiceIntent == null) {
            notificationServiceIntent = Intent(this, CommandNotificationService::class.java)
        }
        if (status) startService(notificationServiceIntent)
        else stopService(notificationServiceIntent)
    }

}