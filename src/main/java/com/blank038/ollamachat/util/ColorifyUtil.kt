package com.blank038.ollamachat.util

import org.bukkit.ChatColor

object ColorifyUtil {

    fun String.colorify(): String {
        return ChatColor.translateAlternateColorCodes('&', this)
    }
}