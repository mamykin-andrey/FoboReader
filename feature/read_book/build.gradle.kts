import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ru.mamykin.foboreader.read_book"
    compileSdk = ProjectInfo.compileSdkVersion

    defaultConfig {
        minSdk = ProjectInfo.minSdkVersion
        targetSdk = ProjectInfo.targetSdkVersion
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.compose = true
    lintOptions.isAbortOnError = false
    kotlinOptions.jvmTarget = "17"
    composeOptions.kotlinCompilerExtensionVersion = "1.5.8"

    buildTypes {
        buildTypes.forEach {
            it.buildConfigField("String", "googleApiKey", project.properties["googleApiKey"] as String)
            it.buildConfigField("String", "googleApiHost", project.properties["googleApiHost"] as String)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":uikit"))
    implementation(project(":feature:common_book_info"))

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.materialLib)
    implementation(Dependencies.activityX)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.appCompatX)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.dagger)

    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExt)
    implementation(Dependencies.composeToolingPreview)
    implementation(Dependencies.coil)

    kapt(Dependencies.daggerCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    androidTestImplementation(Dependencies.espressoCore)
}