@file:OptIn(KspExperimental::class)

package academy.kt

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider


class GenerateInterfaceProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        GenerateInterfaceProcessor(
            codeGenerator = environment.codeGenerator,
        )
}