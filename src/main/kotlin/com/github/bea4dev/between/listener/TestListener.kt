package com.github.bea4dev.between.listener

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.world.WorldRegistry
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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

    private fun teleport(player: Player) {
        val world = WorldRegistry.TUTORIAL
        val location = Location(world, 0.0, 0.0, 0.0)
        player.teleport(location)
    }
}