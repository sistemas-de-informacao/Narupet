package dev.edsoncamargo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.edsoncamargo.R
import dev.edsoncamargo.navigation.MainActivity
import java.lang.Exception

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(1200)
                    isUserLogged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

    fun getCurrentUser(): FirebaseUser? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    fun isUserLogged() {
        if (getCurrentUser() != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}
