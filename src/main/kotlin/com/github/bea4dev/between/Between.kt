package com.github.bea4dev.between

import org.bukkit.plugin.java.JavaPlugin

class Between : JavaPlugin() {
    companion object {
        lateinit var plugin: Between
            private set
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
