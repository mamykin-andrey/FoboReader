import com.android.builder.core.DefaultApiVersion
import com.android.builder.model.ApiVersion
import ru.mamykin.foboreader.Project

plugins {
    id("com.android.library")
    kotlin("android")
    id("androidx.navigation.safeargs")
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
}

fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)