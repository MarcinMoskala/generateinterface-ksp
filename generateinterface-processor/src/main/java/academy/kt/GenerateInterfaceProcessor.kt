@file:Suppress("UnnecessaryVariable")
@file:OptIn(KspExperimental::class)

package academy.kt

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.Variance.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.*
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

@OptIn(KspExperimental::class)
class GenerateInterfaceProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getSymbolsWithAnnotation(GenerateInterface::class.qualifiedName.toString())
            .filterIsInstance<KSClassDeclaration>()
            // Keeping this logging to make it possible to observe incremental build
            .also { logger.warn("Generating for ${it.joinToString { it.simpleName.getShortName() }}") }
            .forEach(::generateInterface)

        return emptyList()
    }

    private fun generateInterface(
        annotatedClass: KSClassDeclaration
    ) {
        val interfaceName = annotatedClass
            .getAnnotationsByType(GenerateInterface::class)
            .single()
            .name
        val interfacePackage = annotatedClass
            .qualifiedName
            ?.getQualifier()
            .orEmpty()

        if (interfaceName.isBlank()) {
            throw Error("Interface name cannot be empty")
        }

        val publicMethods = annotatedClass.getDeclaredFunctions()
            .filter { it.isPublic() && !it.isConstructor() }

        val fileSpec = buildInterfaceFile(interfacePackage, interfaceName, publicMethods)

        val dependencies = Dependencies(aggregating = false, annotatedClass.containingFile!!)
        fileSpec.writeTo(codeGenerator, dependencies)
    }

    private fun buildInterfaceFile(
        interfacePackage: String,
        interfaceName: String,
        publicMethods: Sequence<KSFunctionDeclaration>,
    ): FileSpec = FileSpec
        .builder(interfacePackage, interfaceName)
        .addType(buildInterface(interfaceName, publicMethods))
        .build()

    private fun buildInterface(
        interfaceName: String,
        publicMethods: Sequence<KSFunctionDeclaration>,
    ): TypeSpec = TypeSpec
        .interfaceBuilder(interfaceName)
        .addFunctions(publicMethods.map(::buildInterfaceMethod).toList())
        .build()

    private fun buildInterfaceMethod(
        function: KSFunctionDeclaration,
    ): FunSpec = FunSpec
        .builder(function.simpleName.getShortName())
        .addModifiers(buildFunctionModifiers(function.modifiers))
        .addParameters(
            function.parameters
                .map(::buildInterfaceMethodParameter)
        )
        .returns(function.returnType!!.toTypeName())
        .addAnnotations(function.annotations.map { it.toAnnotationSpec() }.toList())
        .build()

    private fun buildFunctionModifiers(
        modifiers: Set<Modifier>
    ) = modifiers
        .filterNot { it in IGNORED_MODIFIERS }
        .plus(Modifier.ABSTRACT)
        .mapNotNull { it.toKModifier() }

    private fun buildInterfaceMethodParameter(
        variableElement: KSValueParameter,
    ): ParameterSpec = ParameterSpec
        .builder(
            variableElement.name!!.getShortName(),
            variableElement.type.toTypeName(),
        )
        .addAnnotations(variableElement.annotations.map { it.toAnnotationSpec() }.toList())
        .build()

    companion object {
        val IGNORED_MODIFIERS = listOf(Modifier.OPEN, Modifier.OVERRIDE, Modifier.PUBLIC)
    }
}
