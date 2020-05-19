package dev.edsoncamargo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.edsoncamargo.navigationview.MainActivity
import dev.edsoncamargo.navigationview.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        handleButtonLogin()
        handleTextViewCreateAccount()

    }

    private fun getCurrentUser(): FirebaseUser? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    private fun isUserLogged() {
        if (getCurrentUser() != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun signInWithEmailAndPassword() {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(etEmailLogin.text.trim().toString(), etPasswordLogin.text.trim().toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(baseContext, "Autenticação falhou, tente novamente.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun createUserWithEmailAndPassword() {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(etEmailLogin.text.trim().toString(), etPasswordLogin.text.trim().toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(baseContext, "Criação de conta falhou, tente novamente.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleTextViewCreateAccount() {
        val tvCreateAccount: TextView = findViewById(R.id.tvCreateAccount)
        tvCreateAccount.setOnClickListener {
            if (tvCreateAccount.text == "CRIAR CONTA") {
                tvCreateAccount.text = "ENTRAR"
                btnLogin.text = "CRIAR CONTA"
            } else {
                tvCreateAccount.text = "CRIAR CONTA"
                btnLogin.text = "ENTRAR"
            }
        }
    }

    private fun handleButtonLogin() {
        btnLogin.setOnClickListener {
            if (etEmailLogin.text.isNullOrBlank() || etPasswordLogin.text.isNullOrBlank()) {
                Toast.makeText(this, "Todos os campos são obrigatórios.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (btnLogin.text == "ENTRAR") {
                signInWithEmailAndPassword()
            } else {
                createUserWithEmailAndPassword()
            }
        }
    }
}
