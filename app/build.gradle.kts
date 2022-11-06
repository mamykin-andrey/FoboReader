import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(project(":feature:main"))

    implementation(Dependencies.multidex)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.recyclerView)
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
    implementation(Dependencies.cicerone)
    implementation(Dependencies.jankStats)

    kapt(Dependencies.daggerCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    androidTestImplementation(Dependencies.espressoCore)
}