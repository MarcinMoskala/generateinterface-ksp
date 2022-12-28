import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
}

group = "academy.kt"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation(project(":generateinterface-annotations"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.20-1.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}