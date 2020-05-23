package dev.edsoncamargo.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import dev.edsoncamargo.R
import dev.edsoncamargo.models.User
import dev.edsoncamargo.models.UserEntity
import kotlinx.android.synthetic.main.fragment_profille.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profille, container, false)
    }

    override fun onResume() {
        super.onResume()
        handleButtonUpdateProfile()
        getProfile()
    }

    private fun getProfile() {
        val ref = FirebaseDatabase.getInstance().reference.child("users")
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("ERROR", "onCancelled: (${p0.message})")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val user = dataSnapshot.getValue(UserEntity::class.java)
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                if (uid == user!!.uid) {
                    Log.e("INFO", user.toString())
                    tvEmail.text = user.email
                    etName.setText(user.name)
                    etDisplayName.setText(user.displayName)
                    etCPF.setText(user.cpf)
                    etPhoneNumber.setText(user.phoneNumber)
                    Log.e(
                        "INFO",
                        "${user.uid}, ${user.email}, ${user.name}, ${user.displayName}, ${user.cpf}, ${user.phoneNumber}"
                    )
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.e("INFO", "onChildRemoved: (${p0})")
            }
        }
        ref.addChildEventListener(listener)
    }

    private fun handleButtonUpdateProfile() {
        btnEditUserProfile.setOnClickListener {
            if (etDisplayName?.text.isNullOrBlank().not()) {
                updateProfile()
                Toast.makeText(
                    activity,
                    "Informações do usuário atualizadas com sucesso.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                Toast.makeText(activity, "O campo apelido é obrigatório.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun updateProfile() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(etDisplayName.text.toString())
            .build()
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener {
                val ref =
                    FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
                ref.setValue(
                    User(
                        user.uid,
                        if (etName.text.toString().isBlank().not()) etName.text.toString() else "",
                        etDisplayName.text.toString().,
                        tvEmail.text.toString(),
                        if (etCPF.text.toString().isBlank().not()) etCPF.text.toString() else "",
                        if (etPhoneNumber.text.toString().isBlank()
                                .not()
                        ) etPhoneNumber.text.toString() else ""
                    )
                )
            }
    }

}