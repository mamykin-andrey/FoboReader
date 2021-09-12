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
    implementation(project(":widgets:error_stub"))
    kapt(Dependencies.daggerCompiler)
}