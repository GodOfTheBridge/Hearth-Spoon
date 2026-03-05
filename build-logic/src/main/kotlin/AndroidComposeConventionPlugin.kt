import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure(CommonExtension::class.java) {
            buildFeatures.compose = true
        }

        dependencies {
            add("implementation", platform("androidx.compose:compose-bom:2024.09.00"))
            add("implementation", "androidx.compose.runtime:runtime")
            add("implementation", "androidx.compose.ui:ui")
            add("implementation", "androidx.compose.material3:material3")
        }
    }
}
