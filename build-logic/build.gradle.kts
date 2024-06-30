plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.dokka.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("dokka") {
            id = "com.zac.dlkit.dokka"
            implementationClass = "DokkaPlugin"
        }
        register("mavenPublish") {
            id = "com.zac.dlkit.maven.publish"
            implementationClass = "MavenPublishPlugin"
        }
    }
}