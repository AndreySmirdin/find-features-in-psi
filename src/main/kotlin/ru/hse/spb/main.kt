package ru.hse.spb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.hse.spb.structures.PatternTree

fun main(args: Array<String>) {
    val data = jacksonObjectMapper().readValue<PatternTree>("""
        |{
        |   "type": "IF",
        |   "min": 5,
        |   "children": [{
        |       "type": "TRY",
        |       "min": 3
        |       }
        |   ]}
        |}
    """.trimMargin())
    print(data)
    //Runner.run("../DataSet/Dataset_part3_2", "../Dir")
}