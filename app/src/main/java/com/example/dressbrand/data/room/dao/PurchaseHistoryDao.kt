package com.example.dressbrand.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dressbrand.data.room.entity.PurchaseHistory

@Dao
interface PurchaseHistoryDao {
    @Insert
    suspend fun insertPurchase(
        purchase: PurchaseHistory
    )

    @Query(
        "SELECT * FROM purchase_history ORDER BY purchasedAt DESC"
    )
    suspend fun getAllPurchases(): List<PurchaseHistory>
}