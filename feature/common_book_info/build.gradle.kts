import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(project(":core"))

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)

    kapt(Dependencies.roomCompiler)
}