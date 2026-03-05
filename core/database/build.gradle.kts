plugins {
    id("hearth.android.library")
    id("hearth.android.hilt")
    id("hearth.android.room")
}

android { namespace = "com.example.hearthspoon.core.database" }

dependencies {
    implementation(project(":core:model"))
}
