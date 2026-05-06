package com.example.dressbrand.data.room

import android.content.Context
import androidx.room.Room

import com.example.dressbrand.data.room.appdatabase.AppDatabase

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(
        context: Context
    ): AppDatabase {

        if (INSTANCE == null) {

            INSTANCE =

                Room.databaseBuilder(

                    context.applicationContext,

                    AppDatabase::class.java,

                    "dress_brand_db"

                )
                    .fallbackToDestructiveMigration()
                    .build()

        }

        return INSTANCE!!

    }

}