package dev.edsoncamargo.repository

import dev.edsoncamargo.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductRepository {
    @GET(value = "/android/rest/produto")
    fun list(): Call<List<Product>>

    @GET(value = "/android/rest/produto/{id}")
    fun get(@Path("id") id: Int): Call<Product>
}