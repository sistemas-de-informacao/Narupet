package dev.edsoncamargo.navigation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.edsoncamargo.R
import dev.edsoncamargo.views.SendEmailActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_main.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        handleButtonGoToSenac()
        handleButtonSendEmailToDevelopers()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("SOBRE")
    }

    private fun handleButtonGoToSenac() {
        btnGoToSenac.setOnClickListener {
            val url = "https://www.sp.senac.br/jsp/default.jsp?newsID=0"
            val i = Intent(Intent.ACTION_VIEW);
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun handleButtonSendEmailToDevelopers() {
        btnSendEmailToDevelopers.setOnClickListener {
            val i = Intent(this, SendEmailActivity::class.java)
            startActivity(i)
        }
    }
}
