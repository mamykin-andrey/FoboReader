plugins {
    id("com.android.library")
    kotlin("android")
    id("androidx.navigation.safeargs")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:common_book_info"))
}