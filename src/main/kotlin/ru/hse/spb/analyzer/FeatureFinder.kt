package ru.hse.spb.analyzer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.hse.spb.structures.PatternTree
import ru.hse.spb.structures.Tree

class FeatureFinder(pattern: String) {

    var list: ArrayList<Tree> = ArrayList()

    private val patternTree: PatternTree = jacksonObjectMapper().readValue(pattern)

    private fun chooseSubtrees(content: List<Tree>) {
        for (node in content) {
            if (!node.isGood())
                continue
            if (!hasSuitable(node.children)) {
                list.add(node)
            } else if (node.children != null) {
                chooseSubtrees(node.children)
            }
        }
    }

    private fun hasSuitable(list: ArrayList<out Tree>?): Boolean {
        if (list == null)
            return false
        for (node in list) {
            if (node.isGood())
                return true
        }
        return false
    }

    fun dfs(content: List<Tree>) {
        for (node: Tree in content) {
            node.patternMatch = patternTree.createCopy()
            if (node.children != null) {
                dfs(node.children)
                node.updateWithChildren()
            }
            node.patternMatch?.updateWithType(node.type)
        }
    }

    fun findSubtrees(content: ArrayList<out Tree>): ArrayList<Tree> {
        list.clear()
        dfs(content)
        chooseSubtrees(content)
        return list
    }
}