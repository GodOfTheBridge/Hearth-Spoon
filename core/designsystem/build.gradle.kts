plugins {
    id("hearth.android.library")
    id("hearth.android.compose")
}

android { namespace = "com.gotb.heartandspoon.core.designsystem" }

dependencies {
    implementation(project(":core:model"))
}
