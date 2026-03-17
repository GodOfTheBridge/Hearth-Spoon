plugins {
    id("hearth.android.library")
    id("hearth.android.compose")
}

android { namespace = "com.gotb.heartandspoon.feature.homedetails" }

dependencies {
    implementation(project(":core:designsystem"))
}
