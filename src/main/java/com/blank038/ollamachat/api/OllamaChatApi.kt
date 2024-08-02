package com.blank038.ollamachat.api

import com.blank038.ollamachat.message.IMessage
import com.blank038.ollamachat.network.OllamaConnector
import com.google.gson.JsonObject
import java.util.function.Consumer

object OllamaChatApi {

    inline fun <reified T : IMessage> generate(
        url: String,
        model: String,
        prompt: String,
        consumer: Consumer<T>,
        error: Runnable = Runnable { }
    ) {
        val obj = JsonObject()
        obj.addProperty("model", model)
        obj.addProperty("prompt", prompt)
        OllamaConnector(url, obj, consumer, error).open(T::class)
    }
}