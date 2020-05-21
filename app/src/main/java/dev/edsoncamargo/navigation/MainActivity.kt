package dev.edsoncamargo.navigation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dev.edsoncamargo.R
import dev.edsoncamargo.views.LoginActivity
import dev.edsoncamargo.views.ProductsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu.*

class MainActivity : AppCompatActivity() {
    private var toggle: ActionBarDrawerToggle? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initProductsFragment()
        createToggleNavigationViewItems()
        handleNavigationViewItems()
        getCurrentUser()
    }

    private fun initProductsFragment() {
        val productsFragment = ProductsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.productsContainer, productsFragment)
            .commit()
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
                R.id.productsContainer -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }

                R.id.cart -> {
                    val i = Intent(this, CartActivity::class.java)
                    startActivity(i)
                }

                R.id.about -> {
                    val i = Intent(this, AboutActivity::class.java)
                    startActivity(i)
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

    private fun getCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val header = navigationView.getHeaderView(0)
            val tvUserLoggedMenu = header.findViewById<TextView>(R.id.tvUserLoggedMenu)
            tvUserLoggedMenu.text = auth.currentUser!!.displayName
        } else {
            auth.signOut()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}
