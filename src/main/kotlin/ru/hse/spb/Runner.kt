package ru.hse.spb

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.hse.spb.analyzer.FeatureFinder
import ru.hse.spb.helpers.TimeLogger
import ru.hse.spb.io.FileWriter
import ru.hse.spb.io.JsonFilesReader
import ru.hse.spb.structures.PatternTree
import ru.hse.spb.structures.Tree
import java.io.File
import java.util.*

object FilesCounter {
    var counter = 0
}

object Runner {
    fun run(structuresPath: String, structureVectorsPath: String, patternPath: String) {
        val timeLogger = TimeLogger(task_name = "Feature extraction")

        val file = File(patternPath)
        val data = file.readText()

        try {
            findFeatures(structuresPath, structureVectorsPath, data);
        } catch (e: Exception) {
            println("EXCEPTION: $e")
        }

        timeLogger.finish(fullFinish = true)
    }

    private fun findFeatures(treesPath: String, treeVectorsPath: String, pattern: String) {
        val treeReference = object : TypeReference<ArrayList<Tree>>() {}

        val patternTree: PatternTree = jacksonObjectMapper().readValue(pattern)

        val badType = repeatedType(patternTree)
        if (badType != null) {
            throw IllegalArgumentException("$badType is repeated twice in the pattern. Don't do that.")
        }

        val featureFinder = FeatureFinder(patternTree)

        JsonFilesReader<ArrayList<Tree>>(treesPath, ".json", treeReference).run { content: ArrayList<Tree>, file: File ->
            FilesCounter.counter++
            if (content.size == 0) {
                println("SKIP: EMPTY PSI FILE (${FilesCounter.counter})")
                return@run
            }
            println("(${FilesCounter.counter}) $file")
            val trees = featureFinder.findSubtrees(content)
            for ((i, tree) in trees.withIndex()) {
                val newName = file.name.substringBefore('.') + "_" + i + ".kt.json"
                val list = ArrayList<Tree>()
                list.add(tree)
                FileWriter.write(file, treesPath, treeVectorsPath, list, newName);
            }
        }
    }

    private fun repeatedType(patternTree: PatternTree): String? {
        fun repeatedType(patternTree: PatternTree, types: Set<String>): String? {
            if (types.contains(patternTree.type)) return patternTree.type
            types.plus(patternTree.type)
            for (child in patternTree.children) {
                val res = repeatedType(child, types)
                if (res != null) return res
            }
            return null
        }
        return repeatedType(patternTree, TreeSet())
    }
}