package dev.edsoncamargo.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dev.edsoncamargo.R
import dev.edsoncamargo.models.OrderedEntity
import dev.edsoncamargo.models.ProductCartOrdered
import dev.edsoncamargo.utils.progress
import kotlinx.android.synthetic.main.card_item_ordered.view.*
import kotlinx.android.synthetic.main.fragment_ordered.*
import java.text.NumberFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class OrderedFragment : Fragment() {

    var loading: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ordered, container, false)
    }

    override fun onResume() {
        super.onResume()

        loading = progress(activity!!, layoutInflater)
        getOrdered()
    }

    private fun getOrdered() {
        clearProductCartOrdered()
        val ref = FirebaseDatabase.getInstance().reference.child("ordered")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val activity: Activity? = activity
                if (activity != null) {
                    val cart = dataSnapshot.getValue(OrderedEntity::class.java)
                    val cardView =
                        layoutInflater.inflate(R.layout.card_item_ordered, containerOrdered, false)
                    cardView.tvHashOrdered.text = "PEDIDO ${cart!!.id}"
                    cardView.tvDateOrdered.text = cart.date.toString()
                    cardView.tvTotalOrdered.text = "TOTAL ${formatter.format(cart.total)}"
                    cardView.setOnClickListener {
                        val intent = Intent(activity, OrderedProductsActivity::class.java)
                        intent.putExtra("ordered", "PEDIDO ${cart.id}")
                        cart.products?.let { p -> ProductCartOrdered.on.addAll(p) }
                        startActivity(intent)
                    }
                    containerOrdered.addView(cardView)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        }
        ref.addChildEventListener(listener)
        loading!!.dismiss()
    }

    private fun clearProductCartOrdered() {
        ProductCartOrdered.on.clear()
        containerOrdered.removeAllViews()
    }

}
