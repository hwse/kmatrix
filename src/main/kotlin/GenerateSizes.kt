import java.io.File

const val BASE_CLASS = """
interface MSize {
    val size: Int
}
"""

fun main() {
    File("src/Sizes.kt").printWriter().use { out ->
        out.println(BASE_CLASS)
        for (size in 1..1000) {
            out.println("""
            object S$size: MSize {
                override val size: Int
                    get() = $size
            }
            """.trimIndent())
        }
    }
}