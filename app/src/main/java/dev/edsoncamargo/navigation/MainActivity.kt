package dev.edsoncamargo.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import dev.edsoncamargo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu.*

class MainActivity : AppCompatActivity() {
    private var toggle: ActionBarDrawerToggle? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createToggleNavigationViewItems()
        handleNavigationViewItems()
        getCurrentUser()
    }

    private fun getCurrentUser() {
        val email = intent.getStringExtra("value")
        if (email != null) {
            tvUserLoggedMenu.text = intent.getStringExtra("value")
        }
    }

    private fun createToggleNavigationViewItems() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.open_menu, R.string.close_menu
        )
        drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()
    }

    private fun handleNavigationViewItems() {
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when (it.itemId) {
                R.id.about -> {
                    val fragmentViewOne = AboutFragment();
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragmentViewOne)
                        .commit()
                }

                R.id.logout -> {
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    finishAffinity()
                }
            }
            return@setNavigationItemSelectedListener false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle != null && toggle!!.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
