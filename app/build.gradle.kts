plugins {
    id("hearth.android.application")
    id("hearth.android.compose")
    id("hearth.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.gotb.heartandspoon"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(project(":core:designsystem"))
    implementation(project(":domain:api"))
    implementation(project(":feature:home"))
    implementation(project(":feature:home-details"))
    implementation(project(":feature:profile"))
    implementation(project(":data:home"))
    implementation(project(":data:settings"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}
