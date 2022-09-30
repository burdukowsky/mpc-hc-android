package tk.burdukowsky.mpc_hc_android

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var backPressedOnce = false
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
            R.id.notification_switch -> {
                showOrHideNotification()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedOnce) {
            finish()
        }
        this.backPressedOnce = true
        Toast.makeText(this, R.string.press_twice_to_exit, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ backPressedOnce = false }, 2000)
    }

    fun setOptionsMenuVisibility(visibility: Boolean) {
        optionsMenuEnabled = visibility
        invalidateOptionsMenu()
    }

    fun setFabVisibility(visibility: Boolean) {
        if (visibility) fab.show() else fab.hide()
    }

    private fun showOrHideNotification() {
        val status = isMyServiceRunning(CommandNotificationService::class.java)
        setNotificationStatus(!status)
    }

    private fun setNotificationStatus(status: Boolean) {
        if (notificationServiceIntent == null) {
            notificationServiceIntent = Intent(this, CommandNotificationService::class.java)
        }
        if (status) startService(notificationServiceIntent)
        else stopService(notificationServiceIntent)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}