package com.example.dressbrand.data.room.dao

import android.hardware.camera2.params.BlackLevelPattern.COUNT
import android.hardware.camera2.params.RggbChannelVector.COUNT
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dressbrand.data.room.entity.SearchHistory
import okhttp3.internal.http2.Settings.Companion.COUNT

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insertSearch(search: SearchHistory)

    @Query("SELECT * FROM search_history ORDER BY id DESC")
    suspend fun getAllSearches(): List<SearchHistory>

    @Query("DELETE FROM search_history WHERE id=:id")
    suspend fun deleteSearch(id: Int)

    @Query("SELECT searchText FROM search_history GROUP BY searchText ORDER BY COUNT(*) DESC LIMIT 1")
    suspend fun getMostFrequentSearch():
            String?
}