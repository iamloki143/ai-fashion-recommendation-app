package com.example.dressbrand.data

data class CartItem (
    val productId: Int,
    val productName: String,
    val imageUrl: String,
    val price: Double,
    var quantity: Int,
    val selectedSize: String
)