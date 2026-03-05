plugins { id("hearth.android.library") }

android { namespace = "com.example.hearthspoon.core.designsystem" }

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
}
