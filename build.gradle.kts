buildscript {
    repositories {
        jcenter()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
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

plugins {
    id("net.rdrei.android.buildtimetracker") version "0.11.0"
}

buildtimetracker {
    reporters {
        register("summary") {
            options["ordered"] = "true"
            options["barstyle"] = "ascii"
            options["shortenTaskNames"] = "false"
        }
        register("csv") {
            options["output"] = "build/build_time.csv"
            options["append"] = "true"
        }
    }
}