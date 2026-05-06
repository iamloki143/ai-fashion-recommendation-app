package com.example.dressbrand.data.room.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dressbrand.data.room.dao.CartDao
import com.example.dressbrand.data.room.dao.PurchaseHistoryDao
import com.example.dressbrand.data.room.dao.SearchHistoryDao
import com.example.dressbrand.data.room.entity.CartProduct
import com.example.dressbrand.data.room.entity.PurchaseHistory
import com.example.dressbrand.data.room.entity.SearchHistory


@Database(entities = [PurchaseHistory::class, SearchHistory::class, CartProduct::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun SearchHistoryDao(): SearchHistoryDao
    abstract fun PurchaseHistoryDao(): PurchaseHistoryDao
    abstract fun CartDao(): CartDao
}