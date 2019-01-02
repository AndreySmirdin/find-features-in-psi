package ru.hse.spb

import com.fasterxml.jackson.core.type.TypeReference
import ru.hse.spb.analyzer.FeatureFinder
import ru.hse.spb.helpers.TimeLogger
import ru.hse.spb.io.FileWriter
import ru.hse.spb.io.JsonFilesReader
import ru.hse.spb.structures.Tree
import java.io.File

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
        val featureFinder = FeatureFinder(pattern)

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
}