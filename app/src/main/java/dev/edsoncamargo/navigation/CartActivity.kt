package dev.edsoncamargo.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dev.edsoncamargo.R
import dev.edsoncamargo.models.Cart

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        getMyProducts()
    }

    fun getMyProducts() {
        Log.e("INFO", Cart.on.toString())
    }
}
