package com.blank038.ollamachat.provider

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

object GsonProvider {
    private val gson = GsonBuilder().create()

    fun fromString(string: String): JsonObject {
        return gson.fromJson(string, JsonObject::class.java)
    }
}