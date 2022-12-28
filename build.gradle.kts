import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    id("com.google.devtools.ksp") version "1.7.22-1.0.8"
    application
}

group = "academy.kt"
version = "1.0-SNAPSHOT"

// Makes generated code visible to IDE
kotlin.sourceSets.main {
    kotlin.srcDirs(
        file("$buildDir/generated/ksp/main/kotlin"),
    )
}

dependencies {
    implementation(project(":generateinterface-annotations"))
    ksp(project(":generateinterface-processor"))
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt")
}