import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ru.mamykin.foboreader.main"
    compileSdk = ProjectInfo.compileSdkVersion

    defaultConfig {
        minSdk = ProjectInfo.minSdkVersion
        targetSdk = ProjectInfo.targetSdkVersion
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.viewBinding = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(project(":core"))

    implementation(Dependencies.materialLib)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.fragmentX)

    kapt(Dependencies.daggerCompiler)
}