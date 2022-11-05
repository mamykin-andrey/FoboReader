buildscript {
    val compose_ui_version by extra("1.1.1")
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        val kotlinVersion = "1.6.0"
        val gradlePluginVersion = "7.3.1"

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:$gradlePluginVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    id("net.rdrei.android.buildtimetracker") version "0.11.0"
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
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