package com.blank038.ollamachat.command

import com.blank038.ollamachat.api.OllamaChatApi
import com.blank038.ollamachat.config.ApplicationConfig
import com.blank038.ollamachat.message.impl.NormalMessage
import com.blank038.ollamachat.util.ColorifyUtil.colorify
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.StringJoiner
import java.util.function.Consumer

class OllamaChatCommand : CommandExecutor {

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p3.isEmpty()) {
            this.sendHelp(p0, p2)
        } else when (p3[0]) {
            "reload" -> this.reloadConfig(p0)
            "gen" -> this.generate(p0, p3)
        }
        return false
    }

    private fun sendHelp(sender: CommandSender, label: String) {
        val key = if (sender.hasPermission("ollamachat.admin")) "admin" else "default"
        ApplicationConfig.getStringList("message.help.$key").forEach {
            sender.sendMessage(it.colorify().replace("%c", label))
        }
    }

    private fun reloadConfig(sender: CommandSender) {
        if (sender.hasPermission("ollamachat.admin")) {
            ApplicationConfig.reload()
            sender.sendMessage(ApplicationConfig.getString("message.reload"))
        }
    }

    private fun generate(sender: CommandSender, params: Array<out String>) {
        if (params.size == 1) {
            sender.sendMessage(ApplicationConfig.getString("message.pls-enter-question"))
            return
        }
        val question = params.sliceArray(1 until params.size).joinToString(" ")
        val model = ApplicationConfig.getStringOriginal("default-model")
        val host = ApplicationConfig.getStringOriginal("hosts.$model")
        val prompt = ApplicationConfig.getStringList("prompt").joinToString("\n") {
            it.replace("{question}", question)
        }
        val consumer: Consumer<NormalMessage> = Consumer<NormalMessage> {
            ApplicationConfig.getStringList("message.result").forEach { str ->
                sender.sendMessage(str.replace("%answer%", it.getMessage()).colorify())
            }
        }
        OllamaChatApi.generate(host, model, prompt, consumer) {
            sender.sendMessage(ApplicationConfig.getString("message.error"))
        }
    }
}