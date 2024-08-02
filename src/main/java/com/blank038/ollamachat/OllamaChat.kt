package com.blank038.ollamachat

import com.aystudio.core.bukkit.plugin.AyPlugin
import com.blank038.ollamachat.command.OllamaChatCommand
import com.blank038.ollamachat.config.ApplicationConfig
import org.bukkit.Bukkit

class OllamaChat : AyPlugin() {

    companion object {
        lateinit var instance: OllamaChat
    }

    override fun onEnable() {
        instance = this
        ApplicationConfig.reload()
        Bukkit.getPluginCommand("ollamachat").executor = OllamaChatCommand()
    }
}