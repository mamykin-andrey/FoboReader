plugins {
    id("com.android.library")
    kotlin("android")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:common_book_info"))
}