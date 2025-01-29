import ru.mamykin.foboreader.Dependencies
import ru.mamykin.foboreader.ProjectInfo

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android") version "2.54"
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

    signingConfigs {
        create("release") {
            storeFile = file("../keystore")
            storePassword = project.properties["keystorePassword"] as String
            keyAlias = project.properties["keyAlias"] as String
            keyPassword = project.properties["keyPassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            kotlin {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        listOf(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                                "${project.buildDir.absolutePath}/compose_metrics"
                        )
                    )
                    freeCompilerArgs.addAll(
                        listOf(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                                "${project.buildDir.absolutePath}/compose_reports"
                        )
                    )
                }
            }
        }

        create("profile") {
            initWith(getByName("debug"))
            isProfileable = true
            isDebuggable = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
    composeOptions.kotlinCompilerExtensionVersion = "1.5.15"

    lint {
        abortOnError = false
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":uikit"))
    implementation(project(":feature:common_book_info"))
    implementation(project(":feature:books_store"))
    implementation(project(":feature:read_book"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:my_books"))
    implementation(project(":feature:book_details"))
    implementation(project(":feature:main"))
    implementation(project(":feature:dictionary"))

    implementation(Dependencies.multidex)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.composeNavigation)
    releaseImplementation(Dependencies.leakcanaryNoOp)
    debugImplementation(Dependencies.leakcanary)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.activityX)
    implementation(Dependencies.appCompatX)
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

    kapt(Dependencies.hiltCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    androidTestImplementation(Dependencies.espressoCore)
}