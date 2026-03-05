plugins {
    id("hearth.android.library")
    id("hearth.android.hilt")
}

android { namespace = "com.example.hearthspoon.data.home" }

dependencies {
    implementation(project(":domain:api"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
}
