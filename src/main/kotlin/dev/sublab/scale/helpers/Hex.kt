package dev.sublab.scale.helpers

fun ByteArray.toHex(includePrefix: Boolean = false): String {
    val encoded = joinToString(separator = "") { "%02x".format(it) }
    val prefix = if (includePrefix) "0x" else ""
    return prefix+encoded
}

fun String.decodeHex(): ByteArray {
    var value = this
    if (value.startsWith("0x")) value = value.substring(2)

    check(value.length % 2 == 0) { "Must have an even length" }

    return value.chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}