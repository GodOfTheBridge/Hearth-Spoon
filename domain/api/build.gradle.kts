plugins { id("hearth.android.library") }

android { namespace = "com.example.hearthspoon.domain.api" }

dependencies {
    implementation(project(":core:model"))
}
