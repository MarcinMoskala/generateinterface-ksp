import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
    application
}

group = "academy.kt"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":generateinterface-annotations"))
    ksp(project(":generateinterface-processor"))
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("academy.kt.MainKt")
}