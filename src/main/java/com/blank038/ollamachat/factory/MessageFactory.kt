@file:Suppress("UNCHECKED_CAST")

package com.blank038.ollamachat.factory

import com.blank038.ollamachat.message.IMessage
import com.blank038.ollamachat.message.impl.NormalMessage
import com.google.gson.JsonObject
import kotlin.reflect.KClass

object MessageFactory {

    fun <T : IMessage> create(clazz: KClass<T>, objects: List<JsonObject>): T {
        return when (clazz) {
            NormalMessage::class -> NormalMessage(objects) as T
            else -> throw IllegalArgumentException("Unsupported message type: ${clazz.simpleName}")
        }
    }
}