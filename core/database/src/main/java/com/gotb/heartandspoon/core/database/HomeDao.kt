package com.gotb.heartandspoon.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeDao {
    @Query("SELECT * FROM home_items")
    suspend fun getAll(): List<HomeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeEntity>)
}
