plugins {
    id("hearth.android.library")
    id("hearth.android.compose")
    id("hearth.android.hilt")
}

android { namespace = "com.gotb.heartandspoon.feature.profile" }

dependencies {
    implementation(project(":domain:api"))

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}
