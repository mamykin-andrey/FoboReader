import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    kotlin("android")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(project(":core"))

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

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockito)
    testImplementation(Dependencies.mockitoKotlin)
    testImplementation(Dependencies.kluent)

    androidTestImplementation(Dependencies.espressoCore)
}