plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.gotb.heartandspoon.buildlogic"

java {
    toolchain.languageVersion.set(org.gradle.jvm.toolchain.JavaLanguageVersion.of(17))
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly("com.android.tools.build:gradle:9.1.0")
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
        register("androidCompose") {
            id = "hearth.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
    }
}
