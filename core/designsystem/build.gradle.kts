plugins {
    id("hearth.android.library")
    id("hearth.android.compose")
}

android { namespace = "com.gotb.heartandspoon.core.designsystem" }

dependencies {
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.foundation:foundation")
    implementation(project(":core:model"))
}
