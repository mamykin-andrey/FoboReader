import com.android.builder.core.DefaultApiVersion
import com.android.builder.model.ApiVersion
import ru.mamykin.foboreader.ProjectInfo

val kotlinVersion: String by project

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(ProjectInfo.compileSdkVersion)
    buildToolsVersion(ProjectInfo.buildToolsVersion)

    defaultConfig {
        minSdkVersion = ProjectInfo.minSdkVersion.asApiVersion()
        targetSdkVersion = ProjectInfo.targetSdkVersion.asApiVersion()
        versionCode = ProjectInfo.versionCode
        versionName = ProjectInfo.versionName
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.android.support:appcompat-v7:27.1.1")
}

fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)