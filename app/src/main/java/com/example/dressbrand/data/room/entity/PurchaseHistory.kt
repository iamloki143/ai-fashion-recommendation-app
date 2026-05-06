package com.example.dressbrand.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import coil.size.Size


@Entity(
    tableName = "purchase_history"
)
data class PurchaseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val purchaseGroupId: Long,

    val productId: Int,
    val productName: String,
    val quantity:Int,
    val size: String,
    val totalPaid: Double,
    val purchasedAt:Long = System.currentTimeMillis()
)