plugins { id("hearth.android.library") }

android { namespace = "com.gotb.heartandspoon.core.common" }

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
