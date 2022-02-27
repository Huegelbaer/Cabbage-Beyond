package com.cabbagebeyond

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import com.cabbagebeyond.databinding.ActivityMainBinding
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.auth.AuthenticationActivity
import com.cabbagebeyond.util.AuthenticationService
import com.cabbagebeyond.util.startRefreshWorker

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            logoutUserAndNavigateToStartScreen()
        }

        updateNavigationHeader()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_worlds,
                R.id.nav_characters,
                R.id.nav_talents,
                R.id.nav_handicaps,
                R.id.nav_abilities,
                R.id.nav_forces,
                R.id.nav_equipments,
                R.id.nav_races,
                R.id.nav_stories,
                R.id.nav_sessions,
                R.id.nav_account,
                R.id.nav_admin_panel
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                logoutUserAndNavigateToStartScreen()
                return true
            }
            R.id.action_synchronize -> {
                refreshData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logoutUserAndNavigateToStartScreen() {
        AuthenticationService.logout(this) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent)
            finish()
        }
    }

    private fun refreshData() {
        startRefreshWorker(applicationContext)
    }

    private fun updateNavigationHeader() {
        val usernameTextView = binding.navView.getHeaderView(0)
            .findViewById<TextView>(R.id.nav_header_user_name)
        usernameTextView?.text = UserService.currentUser.email
    }
}