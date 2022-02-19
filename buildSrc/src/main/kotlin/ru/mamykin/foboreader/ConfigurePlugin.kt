package ru.mamykin.foboreader

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

typealias AndroidBaseExtensions = BaseExtension

@Suppress("unused")
open class ConfigurePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.configureAndroidModule()
        project.takeIf { it.isAppModule() }?.configureAppModule()
    }

    private fun Project.configureAndroidModule() = extensions.getByType<AndroidBaseExtensions>().run {
        compileSdkVersion(ProjectInfo.compileSdkVersion)

        defaultConfig {
            minSdkVersion(ProjectInfo.minSdkVersion)
            targetSdkVersion(ProjectInfo.targetSdkVersion)
            versionCode = ProjectInfo.versionCode
            versionName = ProjectInfo.versionName
            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }

            getByName("debug") {
                isMinifyEnabled = false
                isDebuggable = true
            }

            buildTypes.forEach {
                it.buildConfigField("String", "googleApiKey", project.properties["googleApiKey"] as String)
                it.buildConfigField("String", "googleApiHost", project.properties["googleApiHost"] as String)
            }
        }

        buildFeatures.viewBinding = true

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        lintOptions {
            isAbortOnError = false
        }
    }

    private fun Project.configureAppModule() = extensions.getByType<AndroidBaseExtensions>().run {
        defaultConfig {
            multiDexEnabled = true
            applicationId = "ru.mamykin.foboreader"
        }
    }

    private fun Project.isAppModule(): Boolean {
        return plugins.toList().any { it is AppPlugin }
    }
}