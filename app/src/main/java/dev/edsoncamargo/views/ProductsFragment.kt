package dev.edsoncamargo.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

import dev.edsoncamargo.R
import dev.edsoncamargo.models.Product
import dev.edsoncamargo.repository.ProductRepository
import dev.edsoncamargo.utils.progress
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.fragment_products.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProductsFragment : Fragment() {

    var loading: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onResume() {
        super.onResume()
        loading = progress(activity!!, layoutInflater)
        getAllProducts()
    }

    private fun updateUi(products: List<Product>) {
        fragmentProductsContainer.removeAllViews()
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        for (product in products) {
            val cardView =
                layoutInflater.inflate(R.layout.card_item, fragmentProductsContainer, false)
            cardView.tvProductNameList.text = product.nomeProduto
            cardView.tvProductPriceList.text = formatter.format(product.precProduto)
            cardView.btnAddToCard.setOnClickListener {
                Snackbar
                    .make(
                        fragmentProductsContainer,
                        "${product.nomeProduto} adicionado ao carrinho.",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
            cardView.btnSeeProduct.setOnClickListener {
                val i = Intent(activity, ProductDetailsActivity::class.java)
                i.putExtra("id", product.idProduto)
                startActivity(i)
            }
            fragmentProductsContainer.addView(cardView)
        }
    }

    private fun getAllProducts() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productRepository = retrofit.create(ProductRepository::class.java)
        val request = productRepository.list()
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        updateUi(products)
                    } else {
                        Toast.makeText(
                            context,
                            "Nenhum produto encontrado.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    Snackbar
                        .make(
                            fragmentProductsContainer,
                            "Não foi possível conectar-se ao servidor, tente novamente mais tarde.",
                            Snackbar.LENGTH_LONG
                        )
                        .show()
                    Log.e("ERRO", "Falha de conexão (response: ${response.errorBody()})")
                }
                loading!!.dismiss()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar
                    .make(
                        fragmentProductsContainer,
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
