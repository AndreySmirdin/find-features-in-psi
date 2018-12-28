package ru.hse.spb

import com.fasterxml.jackson.core.type.TypeReference
import ru.hse.spb.analyzer.FeatureFinder
import ru.hse.spb.helpers.TimeLogger
import ru.hse.spb.io.FileWriter
import ru.hse.spb.io.JsonFilesReader
import ru.hse.spb.structures.Tree
import java.io.File
import java.nio.file.Files

enum class StructureType {
    TREE, LIST
}

object FilesCounter {
    var counter = 0
}

object Runner {
    private const val TOTAL_FILES = 880593

//    private fun writeGeneratedNgrams(ngramGenerator: NgramGenerator) {
//        val writeTimeLogger = TimeLogger(task_name = "N-grams write")
//        FileWriter.write(ALL_NGRAMS_FILE_PATH, ngramGenerator.allNgrams)
//        writeTimeLogger.finish()
//    }

    private fun checkAlreadyExist(file: File, dirPath: String, targetDirPath: String): Boolean {
        val relativePath = file.relativeTo(File(dirPath))
        val outputPath = File("$targetDirPath/$relativePath")
        val fileExist = Files.exists(outputPath.toPath())

        if (fileExist) {
            FilesCounter.counter++
            println("SKIP: PSI ALREADY FACTORIZED (${FilesCounter.counter} out of $TOTAL_FILES)")
        }

        return fileExist
    }

    fun run(structuresPath: String, structureVectorsPath: String) {
        val timeLogger = TimeLogger(task_name = "N-gram extraction")

        val data = """
            |{
            |   "type": "IF",
            |   "min": 5,
            |   "children": [{
            |       "type": "TRY",
            |       "min": 3
            |       }
            |   ]}
            |}""".trimMargin()

        try {
            findFeatures(structuresPath, structureVectorsPath, data);
        } catch (e: Exception) {
            println("EXCEPTION: $e")
        }

        timeLogger.finish(fullFinish = true)
    }

    private fun findFeatures(treesPath: String, treeVectorsPath: String, pattern: String) {
        val treeReference = object : TypeReference<ArrayList<Tree>>() {}
        val featureFinder = FeatureFinder()
        //val additionalFileCheck = { file: File -> checkAlreadyExist(file, treesPath, treeVectorsPath) }

        JsonFilesReader<ArrayList<Tree>>(treesPath, ".json", treeReference).run { content: ArrayList<Tree>, file: File ->
            FilesCounter.counter++
            if (content.size == 0) {
                println("SKIP: EMPTY PSI FILE (${FilesCounter.counter} out of $TOTAL_FILES)")
                return@run
            }
            println("(${FilesCounter.counter} out of $TOTAL_FILES) $file")
            val trees = featureFinder.findIfs(content, pattern)
            for ((i, tree) in trees.withIndex()) {
                val newName = file.name.substringBefore('.') + "_" + i + ".kt.json"
                val list = ArrayList<Tree>()
                list.add(tree)
                FileWriter.write(file, treesPath, treeVectorsPath, list, newName);
            }
        }

        for (a in featureFinder.nodes) {
            println(a)
        }
    }
}