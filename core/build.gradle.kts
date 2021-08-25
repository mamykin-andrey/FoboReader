import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
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
    api(Dependencies.lifecycleRuntimeKtx)
    api(Dependencies.liveDataKtx)
    api(Dependencies.viewModelKtx)

    api(Dependencies.retrofit)
    api(Dependencies.retrofitGsonConverter)
    api(Dependencies.retrofitCoroutinesAdapter)
    api(Dependencies.retrofitLoggingInterceptor)
    api(Dependencies.picasso)
    api(Dependencies.okio)
    api(Dependencies.okHttp)

    api(Dependencies.flowbinding)

    api(Dependencies.multidex)

    api(Dependencies.recyclical)
    api(Dependencies.dagger)

    api(Dependencies.cicerone)

    testApi(Dependencies.junit)
    testApi(Dependencies.mockito)
    testApi(Dependencies.mockitoKotlin)
    testApi(Dependencies.kluent)

    androidTestApi(Dependencies.espressoCore)
}