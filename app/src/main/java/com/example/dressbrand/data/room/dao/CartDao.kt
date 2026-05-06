package com.example.dressbrand.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.example.dressbrand.data.room.entity.CartProduct

@Dao
interface CartDao {

    @Insert
    suspend fun insertCartItem(
        item: CartProduct
    )

    @Query(
        "SELECT * FROM cart_products"
    )
    suspend fun getCartItems():
            List<CartProduct>

    @Query(
        "DELETE FROM cart_products WHERE id=:id"
    )
    suspend fun removeCartItem(
        id:Int
    )

    @Query(
        "DELETE FROM cart_products"
    )
    suspend fun clearCart()

}