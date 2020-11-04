buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        val kotlinVersion: String by project
        val gradlePluginVersion: String by project
        val navigationVersion: String by project

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:$gradlePluginVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
    }
}
allprojects {
    repositories {
        google()
        jcenter()
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}