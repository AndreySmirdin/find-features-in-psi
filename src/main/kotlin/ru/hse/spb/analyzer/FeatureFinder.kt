package ru.hse.spb.analyzer

import ru.hse.spb.structures.Tree
import java.util.*

class FeatureFinder {

    var list: ArrayList<Tree> = ArrayList()

    val nodes: SortedSet<String> = TreeSet()

    fun countVars(content: ArrayList<out Tree>): Int {
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

    fun dfs(content: ArrayList<out Tree>) {
//        for (node: Tree in content) {
//            if (node.vars >= 3) {
//                if (node.type == "IF") {
//                    list.add(node)
//                } else if (node.children != null) {
//                    dfs(node.children)
//                }
//            }
//        }
    }

    fun findIfs(content: ArrayList<out Tree>): ArrayList<Tree> {
        list.clear()
        countVars(content)
        dfs(content)
        return list
    }
}