package com.gotb.heartandspoon.core.model

enum class AppLanguage(
    val storageValue: String,
    val languageTag: String?,
) {
    System(
        storageValue = "system",
        languageTag = null,
    ),
    English(
        storageValue = "english",
        languageTag = "en",
    ),
    Russian(
        storageValue = "russian",
        languageTag = "ru",
    ),
    ;

    companion object {
        fun fromStorageValue(storageValue: String?): AppLanguage {
            return entries.firstOrNull { appLanguage ->
                appLanguage.storageValue == storageValue
            } ?: System
        }
    }
}
