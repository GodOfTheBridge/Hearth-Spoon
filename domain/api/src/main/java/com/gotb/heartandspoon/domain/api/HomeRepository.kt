package com.gotb.heartandspoon.domain.api

import com.gotb.heartandspoon.core.model.HomeItem

interface HomeRepository {
    suspend fun getHomeItems(): List<HomeItem>
}
