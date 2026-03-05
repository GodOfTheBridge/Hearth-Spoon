plugins {
    `kotlin-dsl`
}

group = "com.gotb.heartandspoon.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.5.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "hearth.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "hearth.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "hearth.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "hearth.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}
