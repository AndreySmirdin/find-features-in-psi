package ru.hse.spb.structures

import jdk.nashorn.internal.ir.annotations.Ignore

class Tree : AbstractNode() {
    override val type: String = ""

    val children: ArrayList<out Tree>? = null

    @Ignore
    var patternMatch: PatternTree? = null

    fun updateWithChildren() {
        if (children == null)
            return
        val childStats: ArrayList<ArrayList<Int>> = ArrayList()
        for (child: Int in children.indices) {
            childStats.add(ArrayList())
            children[child].patternMatch?.collectValuesToArray(childStats[child])
        }
    }


}
