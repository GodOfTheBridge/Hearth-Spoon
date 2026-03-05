package com.example.hearthspoon.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_items")
data class HomeEntity(
    @PrimaryKey val id: Long,
    val title: String,
)
