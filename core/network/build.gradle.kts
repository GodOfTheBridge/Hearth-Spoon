plugins {
    id("hearth.android.library")
    id("hearth.android.hilt")
}

android { namespace = "com.example.hearthspoon.core.network" }

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}
