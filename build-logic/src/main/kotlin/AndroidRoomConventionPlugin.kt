import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        dependencies {
            add("implementation", "androidx.room:room-runtime:2.7.2")
            add("implementation", "androidx.room:room-ktx:2.7.2")
            add("ksp", "androidx.room:room-compiler:2.7.2")
        }
    }
}
