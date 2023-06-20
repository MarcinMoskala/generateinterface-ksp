import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
}

group = "academy.kt"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation(project(":generateinterface-annotations"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.10-1.0.9")
    testImplementation(kotlin("test"))
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.9")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.9")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}