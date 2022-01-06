import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:common_book_info"))

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.cardView)
    implementation(Dependencies.materialLib)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.activityX)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.appCompatX)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.liveDataKtx)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGsonConverter)
    implementation(Dependencies.retrofitCoroutinesAdapter)
    implementation(Dependencies.retrofitLoggingInterceptor)
    implementation(Dependencies.picasso)
    implementation(Dependencies.okio)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.dagger)
    implementation(Dependencies.cicerone)

    kapt(Dependencies.daggerCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockito)
    testImplementation(Dependencies.mockitoKotlin)
    testImplementation(Dependencies.kluent)

    androidTestImplementation(Dependencies.espressoCore)
}