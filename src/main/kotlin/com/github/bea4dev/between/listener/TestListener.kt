package com.github.bea4dev.between.listener

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.world.WorldRegistry
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.util.collision.CollideOption
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAnimationEvent
import org.bukkit.event.player.PlayerJoinEvent

class TestListener: Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        //val player = event.player
        //teleport(player)
    }

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val player = event.player
        val message = event.originalMessage()
        if (message is TextComponent && message.content() == "tutorial") {
            Bukkit.getScheduler().runTask(Between.plugin, Runnable {
                teleport(player)
            })
        }
    }

    //@EventHandler
    fun onClick(event: PlayerAnimationEvent) {
        val player = event.player

        val collideOption = CollideOption(
            FluidCollisionMode.NEVER,
            true,
            0.0,
            { block -> block.blockData.isOccluding },
            null,
            null
        )

        val tickThread = VanillaSourceAPI.getInstance().tickThreadPool.nextTickThread

        val world = tickThread.threadLocalCache.getGlobalWorld(player.world.name)

        val result = world.rayTraceBlocks(player.eyeLocation.toVector(), player.eyeLocation.direction, 64.0, collideOption)

        if (result != null) {
            val position = result.hitPosition

            player.spawnParticle(Particle.CLOUD, position.toLocation(player.world), 0)
        }
    }

    private fun teleport(player: Player) {
        val world = WorldRegistry.TUTORIAL
        val location = Location(world, 0.0, 0.0, 0.0)
        player.teleport(location)
    }
}