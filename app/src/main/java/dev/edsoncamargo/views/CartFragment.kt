package dev.edsoncamargo.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import dev.edsoncamargo.R
import dev.edsoncamargo.models.Cart
import dev.edsoncamargo.utils.alert
import kotlinx.android.synthetic.main.card_item_cart.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.NumberFormat
import java.util.*
import java.util.zip.Inflater

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

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (cardView.etQtdProductCart.text.isNullOrBlank().not()) {
                            product.qtd = cardView.etQtdProductCart.text.toString().toInt()
                            product.totalPrice =
                                product.qtd?.times((product.unitPrice!!.minus(product.specialPrice!!)))
                            getMyProductsToBuy()
                            handleButtonFinishShop()
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
            shopContainer.removeAllViews()
            layoutInflater.inflate(R.layout.bag, shopContainer, true)
        }
    }

    private fun handleButtonFinishShop() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        if (Cart.on.isEmpty().not()) {
            var total: Double = 0.0
            for (product in Cart.on) {
                total = total.plus(product.totalPrice!!)
            }
            btnFinishShop.setText("FINALIZAR POR ${formatter.format(total)}")
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
}
