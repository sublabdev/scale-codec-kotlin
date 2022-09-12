package dev.sublab.scale.helpers

fun ByteArray.toHex() = joinToString(separator = "") { "%02x".format(it) }
fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}