package academy.kt

import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
annotation class GenerateInterface(val name: String)