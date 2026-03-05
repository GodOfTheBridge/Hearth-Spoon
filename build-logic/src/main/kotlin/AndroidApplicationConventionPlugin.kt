import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            defaultConfig {
                targetSdk = 34
                versionCode = 1
                versionName = "1.0"
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }

        dependencies {
            add("implementation", platform("androidx.compose:compose-bom:2024.09.00"))
            add("implementation", "androidx.activity:activity-compose:1.9.2")
            add("implementation", "androidx.compose.ui:ui")
            add("implementation", "androidx.compose.material3:material3:1.3.0")
            add("implementation", "androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
        }
    }
}
