package com.gotb.heartandspoon.core.model

enum class ThemeFamily(
    val storageValue: String,
) {
    Khokhloma(storageValue = "khokhloma"),
    Gzhel(storageValue = "gzhel"),
    ;

    companion object {
        fun fromStorageValue(storageValue: String?): ThemeFamily {
            return entries.firstOrNull { themeFamily ->
                themeFamily.storageValue == storageValue
            } ?: Khokhloma
        }
    }
}
