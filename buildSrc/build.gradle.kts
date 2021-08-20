plugins {
    `kotlin-dsl`
}
repositories {
    jcenter()
    google()
}
dependencies {
    // TODO: load from gradle.properties
    implementation("com.android.tools.build:gradle:4.1.0")
    implementation("com.android.tools.build:gradle-api:4.1.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
}