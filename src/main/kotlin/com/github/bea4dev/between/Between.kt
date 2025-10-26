package com.github.bea4dev.between

import com.github.bea4dev.between.biome.BiomeRegistry
import com.github.bea4dev.between.dimension.DimensionRegistry
import com.github.bea4dev.between.listener.BedListener
import com.github.bea4dev.between.listener.ChunkListener
import com.github.bea4dev.between.listener.FoodLevelListener
import com.github.bea4dev.between.listener.PlayerJoinQuitListener
import com.github.bea4dev.between.listener.TestListener
import com.github.bea4dev.between.save.PlayerDataRegistry
import com.github.bea4dev.between.save.ServerData
import com.github.bea4dev.between.world.WorldRegistry
import com.github.shynixn.mccoroutine.bukkit.ShutdownStrategy
import com.github.shynixn.mccoroutine.bukkit.mcCoroutineConfiguration
import com.github.shynixn.mccoroutine.bukkit.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.time.Duration

class Between : JavaPlugin() {
    var isEnabledPlugin = false

    companion object {
        lateinit var plugin: Between
            private set
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this

        //ServerData.load()
        //PlayerDataRegistry.loadAll()

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(TestListener(), this)
        pluginManager.registerEvents(PlayerJoinQuitListener(), this)
        pluginManager.registerEvents(ChunkListener(), this)
        pluginManager.registerEvents(BedListener(), this)
        pluginManager.registerEvents(FoodLevelListener(), this)

        DimensionRegistry.init()
        BiomeRegistry.init()
        WorldRegistry.init {
            isEnabledPlugin = true
        }

        this.mcCoroutineConfiguration.shutdownStrategy = ShutdownStrategy.MANUAL
    }

    override fun onDisable() {
        //ServerData.save()
        //PlayerDataRegistry.saveAll()

        // Plugin shutdown logic
        // shutdown coroutine jobs
        runBlocking {
            withTimeout(Duration.ofSeconds(10).toMillis()) {
                this@Between.scope.coroutineContext[Job]!!.children.forEach { childJob ->
                    childJob.join()
                }
            }
        }

        this.mcCoroutineConfiguration.disposePluginSession()

    }
}
