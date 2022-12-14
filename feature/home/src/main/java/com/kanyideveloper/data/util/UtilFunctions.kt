package com.kanyideveloper.data.util

internal fun String.stringToList(): List<String> {
    return this.split("\r\n")
}