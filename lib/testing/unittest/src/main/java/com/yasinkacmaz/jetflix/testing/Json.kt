package com.yasinkacmaz.jetflix.testing

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.Reader
import java.lang.IllegalArgumentException

val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
}

inline fun <reified T : Any> parseJson(fileName: String): T {
    val jsonString = json.jsonStringFromFile(fileName)
    return json.decodeFromString(jsonString)
}

fun Json.jsonStringFromFile(fileName: String): String {
    val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
    return inputStream?.bufferedReader()?.use(Reader::readText)
        ?: throw IllegalArgumentException("$fileName resource not found")
}
