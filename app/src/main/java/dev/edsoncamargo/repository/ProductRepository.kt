package dev.edsoncamargo.repository

import dev.edsoncamargo.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductRepository {
    @GET(value = "/android/rest/produto")
    fun list(): Call<Set<Product>>

    @GET(value = "/android/rest/produto/{id}")
    fun get(@Path("id") id: Int): Call<Product>

    @GET(value = "/android/rest/produto/{name}")
    fun listByName(@Path("name") name: String): Call<Set<Product>>

    @GET(value = "/android/rest/produto/categoria/{id}")
    fun listByCategory(@Path("id") name: Int): Call<Set<Product>>

    @GET(value = "/android/rest/produto/{name}/{category}")
    fun listByNameAndCategory(@Path("name") name: String, @Path("category") category: Int): Call<Set<Product>>
}