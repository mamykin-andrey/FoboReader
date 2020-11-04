import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.Project
import com.android.builder.model.ApiVersion
import com.android.builder.core.DefaultApiVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs")
}

android {
    compileSdkVersion(Project.compileSdkVersion)
    buildToolsVersion(Project.buildToolsVersion)

    defaultConfig {
        multiDexEnabled = true
        applicationId = "ru.mamykin.foboreader"
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))
    implementation(project(":feature:common_book_info"))
    implementation(project(":feature:books_store"))
    implementation(project(":feature:read_book"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:my_books"))
    implementation(project(":feature:book_details"))
    implementation(Dependencies.leakcanary)
}

fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)