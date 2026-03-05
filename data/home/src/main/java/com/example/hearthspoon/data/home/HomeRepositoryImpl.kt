package com.example.hearthspoon.data.home

import com.example.hearthspoon.core.database.HomeDao
import com.example.hearthspoon.core.database.HomeEntity
import com.example.hearthspoon.core.model.HomeItem
import com.example.hearthspoon.domain.api.HomeRepository
import io.ktor.client.HttpClient
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val homeDao: HomeDao,
) : HomeRepository {
    override suspend fun getHomeItems(): List<HomeItem> {
        httpClient.toString() // placeholder network touchpoint
        val local = homeDao.getAll()
        if (local.isNotEmpty()) return local.map { it.toModel() }

        val seeded = listOf(
            HomeEntity(1, "Welcome to Hearth Spoon"),
            HomeEntity(2, "Compose + MVI + Hilt"),
        )
        homeDao.insertAll(seeded)
        return seeded.map { it.toModel() }
    }
}

private fun HomeEntity.toModel() = HomeItem(id = id, title = title)
