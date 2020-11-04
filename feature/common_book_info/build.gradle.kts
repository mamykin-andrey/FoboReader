import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.Project
import com.android.builder.model.ApiVersion
import com.android.builder.core.DefaultApiVersion

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Project.compileSdkVersion)
    buildToolsVersion(Project.buildToolsVersion)

    defaultConfig {
        minSdkVersion = Project.minSdkVersion.asApiVersion()
        targetSdkVersion = Project.targetSdkVersion.asApiVersion()
        versionCode = Project.versionCode
        versionName = Project.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    kapt(Dependencies.roomCompiler)
}

fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)