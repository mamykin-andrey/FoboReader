import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.recyclerView)

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)

    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)

    implementation(Dependencies.cardView)
    implementation(Dependencies.materialLib)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.activityX)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.appCompatX)

    api(Dependencies.lifecycleRuntimeKtx)
    api(Dependencies.liveDataKtx)

    api(Dependencies.retrofit)
    api(Dependencies.retrofitGsonConverter)
    api(Dependencies.retrofitCoroutinesAdapter)
    api(Dependencies.retrofitLoggingInterceptor)
    api(Dependencies.picasso)
    api(Dependencies.okio)
    api(Dependencies.okHttp)

    api(Dependencies.dagger)

    api(Dependencies.cicerone)

    testApi(Dependencies.junit)
    testApi(Dependencies.mockito)
    testApi(Dependencies.mockitoKotlin)
    testApi(Dependencies.kluent)

    androidTestApi(Dependencies.espressoCore)
}