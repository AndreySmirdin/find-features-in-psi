package ru.hse.spb.structures

class PatternTree : AbstractNode() {
    override val type: String = ""
    val min: Int = 0
    val children: ArrayList<out PatternTree>? = null

    var current: Int = 0

    fun collectValuesToArray(list: ArrayList<Int>) {
        if (children == null)
            return
        list.add(current)
        for (child: PatternTree in children) {
            child.collectValuesToArray(list)
        }
    }

    fun parallelDfs(i: Int, childStats: ArrayList<ArrayList<Int>>): Int {
        current = childStats.fold(0) { acc, list -> acc + list[i] }
        var nxt = i + 1
        if (children != null)
            for (child: PatternTree in children) {
                nxt = child.parallelDfs(nxt, childStats)
            }
        return nxt
    }

    fun updateWithType(type: String) {
        if (this.type == type) {
            current += 1
            return
        }
        if (children == null)
            return
        for (child: PatternTree in children) {
            child.updateWithType(type)
        }
    }
}