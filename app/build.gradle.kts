plugins {
    id("hearth.android.application")
    id("hearth.android.hilt")
}

android {
    namespace = "com.example.hearthspoon"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":feature:home"))
    implementation(project(":data:home"))

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
}
