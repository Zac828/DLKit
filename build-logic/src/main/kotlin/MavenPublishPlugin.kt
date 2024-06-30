import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publisher.MavenPublisher
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra

class MavenPublishPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("maven-publish")
        }

        project.afterEvaluate {
            configurePublish()
        }
    }

    private fun Project.configurePublish() {
        project.extensions.configure<PublishingExtension> {
            val releaseArtifact = project.extra["ReleaseArtifact"] as String
            val releaseVersion = project.rootProject.extra["releaseVersionCode"] as String
            val owner = "Zac828"
            val repository = "DLKit"

            publications {
                create(releaseArtifact, MavenPublication::class.java) {
                    from(project.components.findByName("release"))
                    groupId = GROUP_ID
                    artifactId = releaseArtifact
                    version = releaseVersion

                    println("groupId: $groupId, artifactId: $artifactId, version: $version")
                }
            }
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = project.uri("https://maven.pkg.github.com/$owner/$repository")
                    credentials {
                        username = gradleLocalProperties(project.rootDir, project.providers).getProperty("github.package.user") ?: System.getenv("GITHUB_PACKAGE_USER")
                        password = gradleLocalProperties(project.rootDir, project.providers).getProperty("github.package.password") ?: System.getenv("GITHUB_PACKAGE_PASSWORD")
                    }
                }
            }
        }
    }

    companion object {
        private const val GROUP_ID = "com.zac.dlkit"
    }

}