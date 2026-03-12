plugins {
    id("hearth.android.library")
    id("hearth.android.hilt")
}

android { namespace = "com.gotb.heartandspoon.data.settings" }

dependencies {
    implementation(project(":domain:api"))

    implementation("androidx.datastore:datastore-preferences:1.1.7")
}
