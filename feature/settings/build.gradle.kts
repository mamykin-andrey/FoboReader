import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":widgets:progress_bar"))
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.recyclerView)
    kapt(Dependencies.daggerCompiler)
}