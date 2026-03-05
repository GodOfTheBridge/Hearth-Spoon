plugins { id("hearth.android.library") }

android { namespace = "com.gotb.heartandspoon.domain.api" }

dependencies {
    implementation(project(":core:model"))
}
