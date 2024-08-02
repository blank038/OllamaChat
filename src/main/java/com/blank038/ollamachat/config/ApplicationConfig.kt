package com.blank038.ollamachat.config

import com.blank038.ollamachat.OllamaChat.Companion.instance
import com.blank038.ollamachat.util.ColorifyUtil.colorify

object ApplicationConfig {

    fun reload() {
        instance.saveDefaultConfig()
        instance.reloadConfig()
    }

    fun getStringOriginal(key: String): String {
        return this.getString(key, prefix = false, colorify = false)
    }

    fun getString(key: String, prefix: Boolean = true, colorify: Boolean = true): String {
        var message: String = instance.config.getString(key, "")!!
        if (prefix) {
            message = instance.config.getString("message.prefix", "") + message
        }
        return if (colorify) message.colorify() else message
    }

    fun getStringList(key: String): List<String> {
        return instance.config.getStringList(key)
    }
}