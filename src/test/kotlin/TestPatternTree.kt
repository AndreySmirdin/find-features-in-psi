import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import ru.hse.spb.analyzer.FeatureFinder
import ru.hse.spb.structures.Tree

class TestPatternTree {

    val jsonTree = """{
           |    "type": "if",
           |    "children": [
           |        {"type": "val"},
           |        {"type": "val"},
           |        {"type": "if",
           |         "children": [
           |            {"type": "val"},
           |            {"type": "val"},
           |            {"type": "val"}
           |         ]}
           |    ]
           |}
       """.trimMargin()

    @Test
    fun testUpdatesOfCurrents() {
        runTest(2, 2)
        runTest(3, 1)
    }

    private fun runTest(valNumber: Int, expected: Int) {
        val tree = jacksonObjectMapper().readValue<Tree>(jsonTree)

        FeatureFinder("""
            |{
            |   "type": "if",
            |   "min": 2,
            |   "children": [{
            |       "type": "val",
            |       "min": $valNumber
            |       }
            |   ]}
            |}""".trimMargin()).dfs(listOf(tree))
        assertThat(tree.patternMatch?.current, `is`(expected))
    }
}