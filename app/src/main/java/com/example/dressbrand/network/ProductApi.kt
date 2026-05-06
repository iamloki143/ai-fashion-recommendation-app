package com.example.dressbrand.network

import com.example.dressbrand.data.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("popular")
    suspend fun getPopularProducts(): List<Product>

    @GET("recommended/{query}")
    suspend fun getRecommendedProducts(
        @Path("query") query: String
    ): List<Product>
}