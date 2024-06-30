// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

val minSdkVersion: Int by extra(24)
val targetSdkVersion: Int by extra(34)
val releaseVersionCode: String by extra("0.1.0")