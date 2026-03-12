package com.gotb.heartandspoon.core.model

enum class ThemeMode(
    val storageValue: String,
) {
    Light(storageValue = "light"),
    Dark(storageValue = "dark"),
    ;

    companion object {
        fun fromStorageValue(storageValue: String?): ThemeMode {
            return entries.firstOrNull { themeMode ->
                themeMode.storageValue == storageValue
            } ?: Light
        }
    }
}
