import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ru.mamykin.foboreader.store"
    compileSdk = ProjectInfo.compileSdkVersion

    defaultConfig {
        minSdk = ProjectInfo.minSdkVersion
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures.compose = true
    kotlinOptions.jvmTarget = "17"
    composeOptions.kotlinCompilerExtensionVersion = "1.5.15"

    lint {
        abortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":uikit"))

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.okio)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.hilt)
    implementation(Dependencies.hiltNavigation)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial3)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExt)
    implementation(Dependencies.composeToolingPreview)
    implementation(Dependencies.coil)
    implementation(Dependencies.composeNavigation)

    kapt(Dependencies.hiltCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    androidTestImplementation(Dependencies.espressoCore)
}