package ru.hse.spb.analyzer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.hse.spb.structures.Tree
import java.util.*
import kotlin.collections.ArrayList

class FeatureFinder {

    var list: ArrayList<Tree> = ArrayList()

    val nodes: SortedSet<String> = TreeSet()

    fun countVars(content: List<Tree>): Int {
//        var result = 0
//        for (node: Tree in content) {
//            //println(node.type)
//            nodes += node.type
//            if (node.type == "PROPERTY" || node.type == "VAR") {
//                node.vars = 1;
//            }
//            else if (node.children != null) {
//                node.vars += countVars(node.children)
//            }
//            result += node.vars
//        }
//        return result
        return 0
    }

    fun dfs(content: List<Tree>, pattern: String) {
        for (node: Tree in content) {
            node.patternMatch = jacksonObjectMapper().readValue(pattern)
            if (node.children != null) {
                dfs(node.children, pattern)
                node.updateWithChildren()
            }
            node.patternMatch?.updateWithType(node.type)
        }
    }

    fun findIfs(content: ArrayList<out Tree>, pattern: String): ArrayList<Tree> {
        list.clear()
        countVars(content)
        dfs(content, pattern)
        return list
    }
}