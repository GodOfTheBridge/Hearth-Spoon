plugins { id("hearth.android.library") }

android { namespace = "com.gotb.heartandspoon.domain.api" }

dependencies {
    api(project(":core:model"))
    api(libs.kotlinx.coroutines.android)
}

