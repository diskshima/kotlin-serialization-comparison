package serializing

import kotlin.system.measureTimeMillis

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonDecodingException

import serializing.A
import serializing.B

val jsonInput = "{ \"id\": 3, \"str\": \"string3\" }"
val jsonWithNull = "{ \"id\": 3, \"str\": null }"

fun tryGson() {
    println("Re-use Gson for various classes")

    val gson = Gson()
    val a1 = A(1)

    println(gson.toJson(a1))

    val b1 = B(1, "string1")
    println(gson.toJson(b1))

    val a2 = A(2)
    println(gson.toJson(a2))

    val b = gson.fromJson(jsonWithNull, B::class.java)
    println(b)

    try {
        val str = b.str
        println(str.length)
    } catch (e: NullPointerException) {
        println("Parsing will go through but trying to access it will throw an ${e}")
    }
}

fun tryMoshi() {
    println("Try Moshi")

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(A::class.java)

    val a = A(1)
    println(jsonAdapter.toJson(a))

    val bAdapter = moshi.adapter(B::class.java)
    try {
        val b = bAdapter.fromJson(jsonWithNull)
        println(b)
    } catch (e: JsonDataException) {
        println("Parsing \"${jsonWithNull}\" errors with ${e}")
    }
}

fun tryKotlinSerialization() {
    println("Kotlin Serializer")

    val json = Json(JsonConfiguration.Stable)

    val a = A(1)
    println(json.stringify(A.serializer(), a))

    try {
        val b = json.parse(B.serializer(), jsonWithNull)
        println(b)
    } catch (e: JsonDecodingException) {
        println("Parsing \"${jsonWithNull}\" errors with ${e}")
    }
}

fun compareToJson() {
    val times = 10000
    println("Running serialization ${times} times.")

    val a = A(1)

    val gson = Gson()
    val gsonTime = measureTimeMillis {
        for (i in 0..times) {
            gson.toJson(a)
        }
    }
    println("Gson: ${gsonTime}ms")

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(A::class.java)
    val moshiTime = measureTimeMillis {
        for (i in 0..times) {
            jsonAdapter.toJson(a)
        }
    }
    println("Moshi: ${moshiTime}ms")

    val json = Json(JsonConfiguration.Stable)
    val serializer = A.serializer()
    val ksTime = measureTimeMillis {
        for (i in 0..times) {
            json.stringify(serializer, a)
        }
    }
    println("Kotlin Serialization: ${ksTime}ms")
}

fun compareFromJson() {
    val times = 10000
    println("Running deserialization ${times} times.")

    val gson = Gson()
    val gsonTime = measureTimeMillis {
        for (i in 0..times) {
            gson.fromJson(jsonInput, B::class.java)
        }
    }
    println("Gson: ${gsonTime}ms")

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(B::class.java)
    val moshiTime = measureTimeMillis {
        for (i in 0..times) {
            jsonAdapter.fromJson(jsonInput)
        }
    }
    println("Moshi: ${moshiTime}ms")

    val json = Json(JsonConfiguration.Stable)
    val serializer = B.serializer()
    val ksTime = measureTimeMillis {
        for (i in 0..times) {
            json.parse(serializer, jsonInput)
        }
    }
    println("Kotlin Serialization: ${ksTime}ms")
}

fun printDivider() {
    println("-------------------------------------------")
}

fun main(args: Array<String>) {
    printDivider()
    tryGson()
    printDivider()
    tryMoshi()
    printDivider()
    tryKotlinSerialization()
    printDivider()
    compareToJson()
    printDivider()
    compareFromJson()
    printDivider()
}
