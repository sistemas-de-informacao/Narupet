package dev.edsoncamargo.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dev.edsoncamargo.R
import dev.edsoncamargo.models.ProductCartOrdered
import dev.edsoncamargo.navigation.MainActivity
import kotlinx.android.synthetic.main.activity_ordered_products.*
import kotlinx.android.synthetic.main.card_item_product_ordered.view.*
import java.text.NumberFormat
import java.util.*

class OrderedProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_products)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("ordered")
        getProducts()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("intent", "ordered")
        startActivity(i)
        return true
    }

    private fun getProducts() {
        Log.e("INFO", ProductCartOrdered.on.toString())
        containerProductsOrdered.removeAllViews()
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        for (product in ProductCartOrdered.on) {
            val cardView =
                layoutInflater.inflate(
                    R.layout.card_item_product_ordered,
                    containerProductsOrdered,
                    false
                )
            cardView.tvProductNameOrdered.text = product.name
            cardView.tvQtdProductOrdered.text = "Quantidade ${product.qtd.toString()}"
            cardView.tvUnitPriceOrdered.text = "Unidade ${formatter.format(product.unitPrice)}"
            cardView.tvTotalPriceProductOrdered.text =
                "Total ${formatter.format(product.totalPrice)}"
            containerProductsOrdered.addView(cardView)
        }
    }
}
