package dev.edsoncamargo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dev.edsoncamargo.R
import dev.edsoncamargo.models.Cart
import dev.edsoncamargo.models.Product
import dev.edsoncamargo.models.ProductCart
import dev.edsoncamargo.repository.ProductRepository
import dev.edsoncamargo.utils.progress
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.card_item.view.*
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
                    btnAddProductDetailsToCart.setOnClickListener {
                        if (Cart.on.isEmpty().not()) {
                            for ((i, p) in Cart.on.withIndex()) {
                                if (p.id == product.idProduto) {
                                    p.qtd = p.qtd!!.plus(1)
                                    p.totalPrice =
                                        p.totalPrice!!.plus(p.totalPrice!! - p.specialPrice!!)
                                    Snackbar
                                        .make(
                                            containerProductDetails,
                                            "${product.nomeProduto.substring(
                                                0,
                                                8
                                            )} adicionado ao carrinho. \nQuantidade: ${p.qtd}",
                                            Snackbar.LENGTH_SHORT
                                        )
                                        .show()
                                    return@setOnClickListener
                                } else if (i >= Cart.on.size - 1) {
                                    val productAdded = ProductCart(
                                        product.nomeProduto,
                                        product.idProduto,
                                        null,
                                        product.precProduto,
                                        null,
                                        product.descontoPromocao
                                    )
                                    productAdded.qtd = 1
                                    productAdded.totalPrice =
                                        product.precProduto - product.descontoPromocao
                                    Cart.on.add(productAdded)
                                    Snackbar
                                        .make(
                                            containerProductDetails,
                                            "${product.nomeProduto.substring(
                                                0,
                                                8
                                            )} adicionado ao carrinho. \nQuantidade: ${productAdded.qtd}",
                                            Snackbar.LENGTH_SHORT
                                        )
                                        .show()
                                    return@setOnClickListener
                                }
                            }
                        } else {
                            val productAdded = ProductCart(
                                product.nomeProduto,
                                product.idProduto,
                                null,
                                product.precProduto,
                                null,
                                product.descontoPromocao
                            )
                            productAdded.qtd = 1
                            productAdded.totalPrice = product.precProduto - product.descontoPromocao
                            Cart.on.add(productAdded)
                            Snackbar
                                .make(
                                    containerProductDetails,
                                    "${product.nomeProduto.substring(
                                        0,
                                        8
                                    )} adicionado ao carrinho.",
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                            return@setOnClickListener
                        }
                    }
                    Picasso.get()
                        .load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/${product.idProduto}")
                        .into(productImageDetails)
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
