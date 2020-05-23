package dev.edsoncamargo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import dev.edsoncamargo.R
import dev.edsoncamargo.models.User
import dev.edsoncamargo.navigation.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profille.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        handleButtonLogin()
        handleTextViewCreateAccount()
    }

    private fun signInWithEmailAndPassword() {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(
            etEmailLogin.text.trim().toString(),
            etPasswordLogin.text.trim().toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(
                        baseContext, "Autenticação falhou, tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun createUserWithEmailAndPassword() {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(
            etEmailLogin.text.trim().toString(),
            etPasswordLogin.text.trim().toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(user!!.email!!.split("@")[0])
                        .build()
                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener {
                            if (task.isSuccessful) {
                                val ref = FirebaseDatabase.getInstance().reference.child("users")
                                    .child(user.uid)
                                ref.setValue(
                                    User(
                                        uid = user.uid,
                                        name = "",
                                        displayName = user.email!!.split("@")[0],
                                        email = etEmailLogin.text.toString(),
                                        cpf = "",
                                        phoneNumber = ""
                                    )
                                )
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Criação de conta falhou, tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
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
