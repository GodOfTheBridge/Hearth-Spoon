plugins {
    id("hearth.android.library")
    id("hearth.android.hilt")
}

android { namespace = "com.gotb.heartandspoon.data.home" }

dependencies {
    implementation(project(":domain:api"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(libs.ktor.client.core)
    implementation(project(":core:database"))
}
