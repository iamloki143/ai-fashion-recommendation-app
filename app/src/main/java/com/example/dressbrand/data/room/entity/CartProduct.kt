package com.example.dressbrand.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_products")
data class CartProduct(

    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    val productId:Int,

    val productName:String,

    val imageUrl:String,

    val price:Double,

    val quantity:Int,

    val selectedSize:String
)