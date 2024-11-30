import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.8.22"
}

android {
    namespace = "ru.mamykin.foboreader.main"
    compileSdk = ProjectInfo.compileSdkVersion

    defaultConfig {
        minSdk = ProjectInfo.minSdkVersion
        targetSdk = ProjectInfo.targetSdkVersion
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.8"

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
    implementation(project(":uikit"))

    implementation(Dependencies.materialLib)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.kotlinSerialization)

    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeMaterial3)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExt)
    implementation(Dependencies.composeToolingPreview)
    implementation(Dependencies.coil)
    implementation(Dependencies.composeNavigation)
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)

    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)

}