package com.github.bea4dev.between.listener

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.entity.RandomShadowProcessor
import com.github.bea4dev.between.scenario.script.Tutorial
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinQuitListener: Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (!Between.plugin.isEnabledPlugin) {
            player.kick(Component.text("Sorry! Try again later!"))
            return
        }

        Tutorial().start(player)

        RandomShadowProcessor.start(player)
    }
}