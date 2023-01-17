import academy.kt.GenerateInterfaceProcessorProvider
import com.tschuchort.compiletesting.*
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.OK
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals
import java.io.File

class GenerateInterfaceProcessorTest {

    @Test
    fun `test my annotation processor`() {
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
}