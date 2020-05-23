package dev.edsoncamargo.views

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import dev.edsoncamargo.R
import dev.edsoncamargo.models.Cart
import dev.edsoncamargo.models.Ordered
import dev.edsoncamargo.utils.alert
import kotlinx.android.synthetic.main.card_item_cart.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_products.*
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        getMyProductsToBuy()
        handleButtonFinishShop()
        handleButtonCancelShop()
    }

    private fun getMyProductsToBuy() {
        cartContainer.removeAllViews()
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        if (Cart.on.isEmpty().not()) {
            for (product in Cart.on) {
                val cardView = layoutInflater.inflate(R.layout.card_item_cart, cartContainer, false)
                val productName = product.name
                cardView.tvProductNameCart.text = "${productName!!.substring(0, 12)}..."
                cardView.etQtdProductCart.setText(product.qtd!!.toString())
                cardView.etQtdProductCart.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (cardView.etQtdProductCart.text.isNullOrBlank().not()) {
                            product.qtd = cardView.etQtdProductCart.text.toString().toInt()
                            product.totalPrice =
                                product.qtd?.times((product.unitPrice!!.minus(product.specialPrice!!)))
                            getMyProductsToBuy()
                            handleButtonFinishShop()
                            hideSoftKeyboard(activity!!)
                            Log.e("INFO", product.toString())
                        }
                    }
                })
                cardView.tvUnitPriceCart.text = "Unidade ${formatter.format(product.unitPrice)}"
                cardView.tvTotalPriceCart.text = "Total ${formatter.format(product.totalPrice)}"
                Log.e("INFO", product.toString())
                cartContainer.addView(cardView)
            }
        } else {
            callContainerBag()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleButtonFinishShop() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        if (Cart.on.isEmpty().not()) {
            var total: Double = 0.0
            for (product in Cart.on) {
                total = total.plus(product.totalPrice!!)
            }
            btnFinishShop.setText("FINALIZAR POR ${formatter.format(total)}")
            btnFinishShop.setOnClickListener {
                val database =
                    FirebaseDatabase.getInstance().reference.child("ordered")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                val ordered = Ordered(
                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    total = total,
                    products = Cart.on
                )
                layoutInflater.inflate(R.layout.bag, shopContainer, true)
                val new = database.push()
                ordered.id = new.key
                new.setValue(ordered)
                cartContainer.removeAllViews()
                callContainerBag()
                Cart.on.clear()
                alert("Compra efetuada", "Obrigado por comprar conosco.", shopContainer.context)
            }
        }
    }

    private fun handleButtonCancelShop() {
        if (Cart.on.isEmpty().not()) {
            btnCancelShop.setOnClickListener {
                Cart.on.clear()
                cartContainer.removeAllViews()
                shopContainer.removeAllViews()
                layoutInflater.inflate(R.layout.bag, shopContainer, true)
                alert("Compra cancelada", "Sua sacola foi esvaziado.", shopContainer.context)
            }
        }
    }

    private fun callContainerBag() {
        shopContainer.removeAllViews()
        layoutInflater.inflate(R.layout.bag, shopContainer, true)
    }

    fun hideSoftKeyboard(mActivity: Activity) {
        // Check if no view has focus:
        val view = mActivity.currentFocus
        if (view != null) {
            val inputManager = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
