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
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.liveDataKtx)
    implementation(Dependencies.picasso)
    implementation(Dependencies.lottie)
}