package com.example.dressbrand.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.32.138.208:8000/")
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()
    val api: ProductApi = retrofit.create(
        ProductApi::class.java
    )
}