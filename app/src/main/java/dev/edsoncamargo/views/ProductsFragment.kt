package dev.edsoncamargo.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.PicassoProvider
import dev.edsoncamargo.R
import dev.edsoncamargo.models.Cart
import dev.edsoncamargo.models.Product
import dev.edsoncamargo.models.ProductCart
import dev.edsoncamargo.repository.ProductRepository
import dev.edsoncamargo.utils.progress
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.fragment_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProductsFragment : Fragment() {

    var loading: AlertDialog? = null
    var category: Int = -1

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
        handleButtonSearchFilter()
        onCreateSpinnerCategoriesValues()
        getProducts()
    }

    private fun handleButtonSearchFilter() {
        btnSearchFilter.setOnClickListener {
            loading = progress(activity!!, layoutInflater)
            getProducts()
        }
    }

    private fun updateUi(products: List<Product>?) {
        val activity: Activity? = activity
        if (activity != null) {
            fragmentProductsContainer.removeAllViews()
            val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            if (products != null) {
                Log.e("INFO", products.toString())
                for (product in products) {
                    val cardView =
                        layoutInflater.inflate(R.layout.card_item, fragmentProductsContainer, false)
                    cardView.tvProductNameList.text = product.nomeProduto
                    cardView.tvProductPriceList.text = formatter.format(product.precProduto)
                    Picasso.get()
                        .load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/${product.idProduto}")
                        .into(cardView.imageProductList)
                    cardView.btnAddToCard.setOnClickListener {
                        if (Cart.on.isEmpty().not()) {
                            for ((i, p) in Cart.on.withIndex()) {
                                if (p.id == product.idProduto) {
                                    p.qtd = p.qtd!!.plus(1)
                                    p.totalPrice =
                                        p.totalPrice!!.plus(p.totalPrice!! - p.specialPrice!!)
                                    Snackbar
                                        .make(
                                            fragmentProductsContainer,
                                            "${product.nomeProduto} adicionado ao carrinho. \nQuantidade: ${p.qtd}",
                                            Snackbar.LENGTH_LONG
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
                                            fragmentProductsContainer,
                                            "${product.nomeProduto} adicionado ao carrinho. \nQuantidade: ${productAdded.qtd}",
                                            Snackbar.LENGTH_LONG
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
                                    fragmentProductsContainer,
                                    "${product.nomeProduto} adicionado ao carrinho.",
                                    Snackbar.LENGTH_LONG
                                )
                                .show()
                            return@setOnClickListener
                        }
                    }
                    cardView.btnSeeProduct.setOnClickListener {
                        val i = Intent(activity, ProductDetailsActivity::class.java)
                        i.putExtra("id", product.idProduto)
                        startActivity(i)
                    }
                    fragmentProductsContainer.addView(cardView)
                }
            }
        }
    }

    private fun getProducts() {
        val request = changeRequestType()
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
        request!!.enqueue(callback)
    }

    private fun changeRequestType(): Call<List<Product>>? {
        if (etSearchProductName.text.isNullOrBlank() && category == -1) {
            return createGenericRetrofit().list()
        } else {
            if (etSearchProductName.text.isNullOrBlank()
                    .not() && category == -1
            ) {
                return createGenericRetrofit().listByName(
                    etSearchProductName.text.toString().trim()
                )
            } else if (etSearchProductName.text.isNullOrBlank() && category != -1) {
                return createGenericRetrofit().listByCategory(category)
            } else if (etSearchProductName.text.isNullOrBlank()
                    .not() && category != -1
            ) {
                return createGenericRetrofit().listByNameAndCategory(
                    etSearchProductName.text.toString().trim(), category
                )
            }
        }
        return null
    }

    private fun createGenericRetrofit(): ProductRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ProductRepository::class.java)
    }

    private fun onCreateSpinnerCategoriesValues() {
        val spinner = activity!!.findViewById<Spinner>(R.id.spSearchCategories)
        val items = arrayOf(
            "Seleciona uma categoria",
            "Alimentação",
            "Espécies",
            "Farmácia",
            "Adestramento",
            "Higiene e Beleza",
            "Acessórios",
            "Pesticidas",
            "Outro Tésteé"
        )
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (spinner.selectedItemPosition) {
                    0 -> {
                        category = -1
                    }
                    1 -> {
                        category = 1
                    }
                    2 -> {
                        category = 2
                    }
                    3 -> {
                        category = 3
                    }
                    4 -> {
                        category = 4
                    }
                    5 -> {
                        category = 5
                    }
                    6 -> {
                        category = 6
                    }
                    7 -> {
                        category = 7
                    }
                    8 -> {
                        category = 20
                    }
                }
            }
        }
    }

}
