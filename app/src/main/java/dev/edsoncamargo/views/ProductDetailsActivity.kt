package dev.edsoncamargo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import dev.edsoncamargo.R
import dev.edsoncamargo.models.Product
import dev.edsoncamargo.repository.ProductRepository
import dev.edsoncamargo.utils.progress
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.fragment_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {

    var loading: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loading = progress(this, layoutInflater)
        getProductDetails()
    }

    private fun getProductDetails() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productRepository = retrofit.create(ProductRepository::class.java)
        val request = productRepository.get(intent.getIntExtra("id", -1))
        val callback = object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    supportActionBar?.title = product!!.nomeProduto
                    tvProductNameDetails.text = product.nomeProduto
                    tvProductDescriprionDetails.text = product.descProduto
                    tvSpecialPriceDetails.text =
                        "Oferta de desconto ${formatter.format(product.descontoPromocao)}"
                    tvProductPriceDetails.text = formatter.format(product.precProduto)
                }
                loading!!.dismiss()
            }


            override fun onFailure(call: Call<Product>, t: Throwable) {
                Snackbar
                    .make(
                        containerProductDetails,
                        "Não foi possível conectar-se a internet.",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
                Log.e("ERRO", "Falha de conexão (e: ${t.message})")
                loading!!.dismiss()
            }
        }
        request.enqueue(callback)
    }
}
