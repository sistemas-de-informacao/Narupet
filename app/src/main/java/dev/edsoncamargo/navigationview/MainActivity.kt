package dev.edsoncamargo.navigationview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var toggle: ActionBarDrawerToggle? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.open_menu, R.string.close_menu
        )
        drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()
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
                    this.finishAffinity();
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
