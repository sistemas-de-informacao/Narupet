package dev.edsoncamargo.navigationview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.edsoncamargo.views.SendEmailActivity
import kotlinx.android.synthetic.main.fragment_about.view.*

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val i = inflater.inflate(R.layout.fragment_about, container, false)
        handleButtonGoToSenac(i)
        handleButtonSendEmailToDevelopers(i)
        return i;
    }

    private fun handleButtonGoToSenac(view: View) {
        view.btnGoToSenac.setOnClickListener {
            val url = "https://www.sp.senac.br/jsp/default.jsp?newsID=0"
            val i = Intent(Intent.ACTION_VIEW);
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun handleButtonSendEmailToDevelopers(view: View) {
        view.btnSendEmailToDevelopers.setOnClickListener {
            val i = Intent(view.context, SendEmailActivity::class.java)
            startActivity(i)
        }
    }

}
