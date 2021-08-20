import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
    id("ru.mamykin.foboreader")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))
    implementation(project(":feature:common_book_info"))
    implementation(project(":feature:books_store"))
    implementation(project(":feature:read_book"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:my_books"))
    implementation(project(":feature:book_details"))
    kapt(Dependencies.daggerCompiler)
//    implementation(Dependencies.leakcanary)
}