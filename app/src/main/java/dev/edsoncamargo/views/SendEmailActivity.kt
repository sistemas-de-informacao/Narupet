package dev.edsoncamargo.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dev.edsoncamargo.R
import kotlinx.android.synthetic.main.activity_send_email.*
import java.lang.Exception

class SendEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)
        handleButtonSendEmail()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("ENVIAR E-MAIL")
    }

    private fun handleButtonSendEmail() {
        btnSendEmail.setOnClickListener {
            if (etSubjectEmail.text.isNullOrBlank() || etMessage.text.isNullOrBlank()) {
                Toast.makeText(this, "Todos os campos são obrigatórios.", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            val i = Intent(Intent.ACTION_SEND)
            i.data = Uri.parse("mailto:")
            i.type = "message/rfc822"
            i.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("DaniielVieira@outlook.com", "dinhocmenezes@hotmail.com", "dan_css@hotmail.com")
            )
            i.putExtra(Intent.EXTRA_SUBJECT, etSubjectEmail.text.trim())
            i.putExtra(Intent.EXTRA_TEXT, etMessage.text.trim())
            try {
                startActivity(Intent.createChooser(i, "Escolha o cliente de e-mail"));
                etSubjectEmail.setText("");
                etMessage.setText("");
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
