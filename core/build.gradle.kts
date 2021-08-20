import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("androidx.navigation.safeargs")
    id("ru.mamykin.foboreader")
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
    api(Dependencies.dagger)

    testApi(Dependencies.junit)
    testApi(Dependencies.mockito)
    testApi(Dependencies.mockitoKotlin)
    testApi(Dependencies.kluent)

    androidTestApi(Dependencies.espressoCore)
}