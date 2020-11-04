import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.Project
import com.android.builder.model.ApiVersion
import com.android.builder.core.DefaultApiVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(Dependencies.kotlinStdLib)

    api(Dependencies.coreKtx)
    api(Dependencies.recyclerView)

    api(Dependencies.coroutinesCore)
    api(Dependencies.coroutinesAndroid)

    api(Dependencies.koinScope)
    api(Dependencies.koinViewModel)

    api(Dependencies.roomRuntime)
    api(Dependencies.roomKtx)

    api(Dependencies.cardView)
    api(Dependencies.materialLib)
    api(Dependencies.constraintLayout)

    api(Dependencies.lifecycleExtensions)
    api(Dependencies.lifecycleViewModel)

    api(Dependencies.retrofit)
    api(Dependencies.retrofitGsonConverter)
    api(Dependencies.retrofitCoroutinesAdapter)
    api(Dependencies.retrofitLoggingInterceptor)
    api(Dependencies.picasso)
    api(Dependencies.okio)
    api(Dependencies.okHttp)

    api(Dependencies.navigationFragment)
    api(Dependencies.navigationUi)

    api(Dependencies.flowbinding)

    api(Dependencies.multidex)

    api(Dependencies.recyclical)

    testApi(Dependencies.junit)
    testApi(Dependencies.mockito)
    testApi(Dependencies.mockitoKotlin)
    testApi(Dependencies.kluent)

    androidTestApi(Dependencies.espressoCore)
}

fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)