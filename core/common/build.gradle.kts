plugins { id("hearth.android.library") }

android { namespace = "com.example.hearthspoon.core.common" }

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
