package ru.hse.spb

import com.xenomachina.argparser.ArgParser
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser(args)
    val inputPath by parser.storing("-i", "--input", help = "path to folder with files for feature selection")
    val outputPath by parser.storing("-o", "--output", help = "path to folder, in which will be written results")
    val patternPath by parser.storing("--pattern", help = "pattern file")

    File(outputPath).deleteRecursively()
    Runner.run(inputPath, outputPath, patternPath)
}