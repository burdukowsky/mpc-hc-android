package tk.burdukowsky.mpc_hc_android

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var fab: FloatingActionButton
    private var optionsMenuEnabled = true

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
        NavigationUI.onNavDestinationSelected(item, navController)
        return super.onOptionsItemSelected(item)
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

}