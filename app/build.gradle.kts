import ru.mamykin.foboreader.Dependencies

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
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
    implementation(project(":feature:main"))
    implementation(Dependencies.multidex)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.leakcanary)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.cardView)
    implementation(Dependencies.materialLib)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.activityX)
    implementation(Dependencies.fragmentX)
    implementation(Dependencies.appCompatX)
    kapt(Dependencies.daggerCompiler)
}