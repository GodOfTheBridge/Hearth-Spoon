package com.gotb.heartandspoon.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HomeEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class HearthSpoonDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
}
