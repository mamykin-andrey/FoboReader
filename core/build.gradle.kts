import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ru.mamykin.foboreader.core"
    compileSdk = ProjectInfo.compileSdkVersion

    defaultConfig {
        minSdk = ProjectInfo.minSdkVersion
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.15"

    lint {
        abortOnError = false
    }
}

dependencies {
    implementation(Dependencies.appCompatX)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGsonConverter)
    implementation(Dependencies.retrofitCoroutinesAdapter)
    implementation(Dependencies.retrofitLoggingInterceptor)
    implementation(Dependencies.okio)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.hilt)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial3)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExt)
    implementation(Dependencies.composeToolingPreview)
    implementation(Dependencies.composeNavigation)

    kapt(Dependencies.hiltCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    androidTestImplementation(Dependencies.espressoCore)
}