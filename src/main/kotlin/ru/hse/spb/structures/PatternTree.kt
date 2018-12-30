package ru.hse.spb.structures

class PatternTree(val min: Int, override val type: String) : AbstractNode() {
    val children: ArrayList<PatternTree> = ArrayList()

    var current: Int = 0

    fun collectValuesToArray(list: ArrayList<Int>) {
        list.add(current)
        for (child: PatternTree in children) {
            child.collectValuesToArray(list)
        }
    }

    fun parallelDfs(i: Int, childStats: ArrayList<ArrayList<Int>>): Int {
        current = childStats.fold(0) { acc, list -> acc + list[i] }
        var nxt = i + 1
        for (child: PatternTree in children) {
            nxt = child.parallelDfs(nxt, childStats)
        }
        return nxt
    }

    fun updateWithType(type: String) {
        if (this.type.toLowerCase() == type.toLowerCase() && childrenSatisfied()) {
            current += 1
            resetLower()
            return
        }

        for (child: PatternTree in children) {
            child.updateWithType(type)
        }
    }

    private fun resetLower() {
        for (child in children) {
            child.current = 0
            child.resetLower()
        }
    }

    private fun childrenSatisfied(): Boolean {
        for (child in children) {
            if (child.current < child.min)
                return false
        }
        return true
    }

    fun createCopy(): PatternTree {
        val copy = PatternTree(min, type)
        for (child in children) {
            copy.children.add(child.createCopy())
        }
        return copy
    }
}