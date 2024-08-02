package com.blank038.ollamachat.message.impl

import com.blank038.ollamachat.message.IMessage
import com.google.gson.JsonObject

class NormalMessage(
    objects: List<JsonObject>
) : IMessage {
    private val lastMessage: String

    init {
        val strBuilder = StringBuilder()
        for (i in objects) {
            if (i["done"]?.asBoolean == true) break
            strBuilder.append(i["response"].asString)
        }
        this.lastMessage = strBuilder.toString()
    }

    override fun getMessage(): String {
        return lastMessage
    }
}