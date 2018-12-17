package ru.hse.spb.io

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

object FileWriter {
    fun write(file: File, dirPath: String, targetDirPath: String, content: Any) {
        write(file, dirPath, targetDirPath, content, file.name)
    }

    fun write(file: File, dirPath: String, targetDirPath: String, content: Any, newName: String) {
        val relativePath = file.relativeTo(File(dirPath)).parent ?: ""
        val outputPath = File("$targetDirPath/$relativePath/$newName")

        File("$targetDirPath/${relativePath}").mkdirs()
        outputPath.writeText(ObjectMapper().writeValueAsString(content))
    }

    fun write(filename: String, dirPath: String, targetDirPath: String, content: Any) {
        write(File(filename), dirPath, targetDirPath, content)
    }

    fun write(filename: String, content: Any) {
        File(filename).writeText(ObjectMapper().writeValueAsString(content))
    }
}