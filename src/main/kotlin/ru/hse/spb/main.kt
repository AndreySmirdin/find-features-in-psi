package ru.hse.spb

import java.io.File

fun main(args: Array<String>) {
    File("../Dir").deleteRecursively()
    Runner.run("../DataSet", "../Dir")
}