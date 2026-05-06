package com.example.dressbrand.data

import com.google.gson.annotations.SerializedName

data class Product(

    @SerializedName("product_id")
    val productId:Int,

    @SerializedName("product_name")
    val productName:String,

    @SerializedName("description")
    val description:String,

    @SerializedName("category")
    val category:String,

    @SerializedName("price")
    val price:Double,

    @SerializedName("rating")
    val rating:Double,

    @SerializedName("popularity_score")
    val popularityScore:Int,

    @SerializedName("image_url")
    val imageUrl:String,

    @SerializedName("size_xs")
    val sizeXS:Int,

    @SerializedName("size_s")
    val sizeS:Int,

    @SerializedName("size_m")
    val sizeM:Int,

    @SerializedName("size_l")
    val sizeL:Int,

    @SerializedName("size_xl")
    val sizeXL:Int
)