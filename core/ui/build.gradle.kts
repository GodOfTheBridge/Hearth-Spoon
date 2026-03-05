plugins { id("hearth.android.library") }

android { namespace = "com.example.hearthspoon.core.ui" }

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.android)
}
