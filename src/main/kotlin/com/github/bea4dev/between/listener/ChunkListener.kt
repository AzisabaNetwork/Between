package com.github.bea4dev.between.listener

import com.github.bea4dev.between.biome.BiomeRegistry
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent

class ChunkListener: Listener {
    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        val world = event.world
        val chunk = event.chunk
        val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler

        if (world.name == "tutorial") {
            nmsHandler.setBiomeForChunk(chunk, BiomeRegistry.BETWEEN)
        }
        if (world.name.contains("between")) {
            nmsHandler.setBiomeForChunk(chunk, BiomeRegistry.BETWEEN)
        }
    }
}