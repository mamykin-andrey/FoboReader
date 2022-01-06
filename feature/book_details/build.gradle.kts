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
    kapt(Dependencies.daggerCompiler)
}