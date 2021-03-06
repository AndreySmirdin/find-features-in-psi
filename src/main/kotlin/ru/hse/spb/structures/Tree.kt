package ru.hse.spb.structures

import com.fasterxml.jackson.annotation.JsonIgnore

class Tree : AbstractNode() {
    override val type: String = ""

    val children: ArrayList<out Tree>? = null

    @JsonIgnore
    var patternMatch: PatternTree? = null

    fun updateWithChildren() {
        if (children == null)
            return
        val childStats: ArrayList<ArrayList<Int>> = ArrayList()
        for (child: Int in children.indices) {
            childStats.add(ArrayList())
            children[child].patternMatch?.collectValuesToArray(childStats[child])
        }
        patternMatch?.parallelDfs(0, childStats)
    }

    @JsonIgnore
    fun isGood(): Boolean {
        return patternMatch?.current!! >= patternMatch?.min!!
    }
}
