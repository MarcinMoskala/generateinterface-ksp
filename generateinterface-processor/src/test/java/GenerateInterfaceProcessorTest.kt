import academy.kt.GenerateInterfaceProcessorProvider
import com.tschuchort.compiletesting.*
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.*
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertNotEquals

class GenerateInterfaceProcessorTest {

    @Test
    fun `should generate interface for simple class`() {
        assertGeneratedFile(
            sourceFileName = "RealTestRepository.kt",
            source = """
                import academy.kt.GenerateInterface
    
                @GenerateInterface("TestRepository")
                class RealTestRepository {
                    fun a(i: Int): String = TODO()
                    private fun b() {}
                }
            """,
            generatedResultFileName = "TestRepository.kt",
            generatedSource = """
                import kotlin.Int
                import kotlin.String
                
                public interface TestRepository {
                  public fun a(i: Int): String
                }
            """
        )
    }

    @Test
    fun `should fail when incorrect name`() {
        assertFailsWithMessage(
            sourceFileName = "RealTestRepository.kt",
            source = """
                import academy.kt.GenerateInterface
    
                @GenerateInterface("")
                class RealTestRepository {
                    fun a(i: Int): String = TODO()
                    private fun b() {}
                }
            """,
            message = "Interface name cannot be empty"
        )
    }
}

private fun assertGeneratedFile(
    sourceFileName: String,
    @Language("kotlin") source: String,
    generatedResultFileName: String,
    @Language("kotlin") generatedSource: String
) {
    val compilation = KotlinCompilation().apply {
        inheritClassPath = true
        kspWithCompilation = true

        sources = listOf(
            SourceFile.kotlin(sourceFileName, source)
        )
        symbolProcessorProviders = listOf(
            GenerateInterfaceProcessorProvider()
        )
    }
    val result = compilation.compile()
    assertEquals(OK, result.exitCode)

    val generated = File(
        compilation.kspSourcesDir,
        "kotlin/$generatedResultFileName"
    )
    assertEquals(
        generatedSource.trimIndent(),
        generated.readText().trimIndent()
    )
}

private fun assertFailsWithMessage(
    sourceFileName: String,
    @Language("kotlin") source: String,
    message: String
) {
    val compilation = KotlinCompilation().apply {
        inheritClassPath = true
        kspWithCompilation = true

        sources = listOf(
            SourceFile.kotlin(sourceFileName, source)
        )
        symbolProcessorProviders = listOf(
            GenerateInterfaceProcessorProvider()
        )
    }
    val result = compilation.compile()
    assertNotEquals(OK, result.exitCode)
    assertContains(result.messages, message)
}