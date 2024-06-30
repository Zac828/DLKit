import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.File

class DokkaPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("org.jetbrains.dokka")
        }

        project.tasks.withType<DokkaTask> {
            moduleName.set("DLKit")
            val outputPath = "${project.rootDir}/document"
            outputDirectory.set(File(outputPath))
        }

        project.tasks.withType<DokkaTask> {
            dokkaSourceSets.configureEach {
                suppressObviousFunctions.set(true)
                includeNonPublic.set(false)
                skipDeprecated.set(true)
                reportUndocumented.set(true)
                skipEmptyPackages.set(true)
                noJdkLink.set(true)
                noStdlibLink.set(true)
                noAndroidSdkLink.set(true)
                suppressInheritedMembers.set(true)
                perPackageOption {
                    matchingRegex.set(".*data.*|.*domain.*|.*di.*|.*internal.*")
                    suppress.set(true)
                }
            }
        }

    }

}