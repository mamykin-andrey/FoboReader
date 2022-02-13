package ru.mamykin.foboreader

object Versions {
    const val roomVersion = "2.4.0"
    const val coroutinesVersion = "1.3.2"
    const val retrofitVersion = "2.3.0"
    const val okHttpVersion = "4.9.0"
    const val picassoVersion = "2.5.2"
    const val mockkVersion = "1.12.2"
    const val junitVersion = "4.12"
    const val multidexVersion = "2.0.1"
    const val coreKtxVersion = "1.0.2"
    const val recyclerViewVersion = "1.2.0-alpha03"
    const val materialLibVersion = "1.0.0"
    const val constraintLayoutVersion = "1.1.3"
    const val androidxVersion = "1.1.0"
    const val retrofitCoroutinesAdapterVersion = "0.9.2"
    const val retrofitLoggingInterceptorVersion = "4.3.0"
    const val espressoCoreVersion = "3.1.1"
    const val leakcanaryVersion = "2.5"
    const val daggerVersion = "2.38.1"
    const val ciceroneVersion = "7.1"
    const val lifecycleVersion = "2.3.1"
    const val lottieVersion = "4.2.2"
}

object Dependencies {
    const val activityX = "androidx.activity:activity:${Versions.androidxVersion}"
    const val fragmentX = "androidx.fragment:fragment:${Versions.androidxVersion}"
    const val appCompatX = "androidx.appcompat:appcompat:${Versions.androidxVersion}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val cardView = "androidx.cardview:cardview:${Versions.materialLibVersion}"
    const val materialLib = "com.google.android.material:material:${Versions.materialLibVersion}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val okio = "com.squareup.okio:okio:${Versions.retrofitVersion}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttpVersion}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
    const val retrofitCoroutinesAdapter =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofitCoroutinesAdapterVersion}"
    const val retrofitLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.retrofitLoggingInterceptorVersion}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picassoVersion}"
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val mockk = "io.mockk:mockk:${Versions.mockkVersion}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCoreVersion}"
    const val multidex = "androidx.multidex:multidex:${Versions.multidexVersion}"
    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanaryVersion}"
    const val dagger = "com.google.dagger:dagger:${Versions.daggerVersion}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
    const val cicerone = "com.github.terrakok:cicerone:${Versions.ciceroneVersion}"
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottieVersion}"
}