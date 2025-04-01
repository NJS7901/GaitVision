// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    id("com.chaquo.python") version "16.0.0" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url="https://chaquo.com/maven")
    }
    dependencies {
    }
}
//Might be needed, but can stay commented out for now (10/8/24 Trevor Spencer)
/*
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
    }
}*/
