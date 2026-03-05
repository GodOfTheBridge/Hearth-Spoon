package com.example.hearthspoon.domain.api

import com.example.hearthspoon.core.model.HomeItem

interface HomeRepository {
    suspend fun getHomeItems(): List<HomeItem>
}
