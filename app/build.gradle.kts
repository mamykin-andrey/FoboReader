import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ru.mamykin.foboreader"
    compileSdk = ProjectInfo.compileSdkVersion

    defaultConfig {
        multiDexEnabled = true
        applicationId = "ru.mamykin.foboreader"
        minSdk = ProjectInfo.minSdkVersion
        targetSdk = ProjectInfo.targetSdkVersion
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.8"

    lintOptions {
        isAbortOnError = false
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:common_book_info"))
    implementation(project(":feature:books_store"))
    implementation(project(":feature:read_book"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:my_books"))
    implementation(project(":feature:book_details"))
    implementation(project(":feature:main"))

    implementation(Dependencies.multidex)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.composeNavigation)
    releaseImplementation(Dependencies.leakcanaryNoOp)
    debugImplementation(Dependencies.leakcanary)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.activityX)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.appCompatX)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGsonConverter)
    implementation(Dependencies.retrofitCoroutinesAdapter)
    implementation(Dependencies.retrofitLoggingInterceptor)
    implementation(Dependencies.okio)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.dagger)
    implementation(Dependencies.jankStats)

    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExt)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeToolingPreview)

    kapt(Dependencies.daggerCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    androidTestImplementation(Dependencies.espressoCore)
}