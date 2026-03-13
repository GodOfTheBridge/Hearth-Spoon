package com.gotb.heartandspoon.core.model

enum class ThemeMode(
    val storageValue: String,
) {
    Light(storageValue = "light"),
    System(storageValue = "system"),
    Dark(storageValue = "dark"),
    ;

    companion object {
        fun fromStorageValue(storageValue: String?): ThemeMode {
            return entries.firstOrNull { themeMode ->
                themeMode.storageValue == storageValue
            } ?: System
        }
    }
}

fun ThemeMode.resolveEffectiveThemeMode(systemIsDarkTheme: Boolean): ThemeMode =
    when (this) {
        ThemeMode.System ->
            if (systemIsDarkTheme) {
                ThemeMode.Dark
            } else {
                ThemeMode.Light
            }

        ThemeMode.Light,
        ThemeMode.Dark,
        -> this
    }

fun ThemeMode.isEffectivelyDark(systemIsDarkTheme: Boolean): Boolean =
    resolveEffectiveThemeMode(systemIsDarkTheme = systemIsDarkTheme) == ThemeMode.Dark
