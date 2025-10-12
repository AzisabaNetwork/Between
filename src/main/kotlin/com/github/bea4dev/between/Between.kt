package com.github.bea4dev.between

import com.github.bea4dev.between.biome.BiomeRegistry
import com.github.bea4dev.between.dimension.DimensionRegistry
import com.github.bea4dev.between.listener.BedListener
import com.github.bea4dev.between.listener.ChunkListener
import com.github.bea4dev.between.listener.PlayerJoinQuitListener
import com.github.bea4dev.between.listener.TestListener
import com.github.bea4dev.between.world.WorldRegistry
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Between : JavaPlugin() {
    var isEnabledPlugin = false

    companion object {
        lateinit var plugin: Between
            private set
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(TestListener(), this)
        pluginManager.registerEvents(PlayerJoinQuitListener(), this)
        pluginManager.registerEvents(ChunkListener(), this)
        pluginManager.registerEvents(BedListener(), this)

        DimensionRegistry.init()
        BiomeRegistry.init()
        WorldRegistry.init {
            isEnabledPlugin = true
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
