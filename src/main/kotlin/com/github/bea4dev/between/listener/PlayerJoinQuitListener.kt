package com.github.bea4dev.between.listener

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.entity.RandomEntitySpawner
import com.github.bea4dev.between.save.PlayerDataRegistry
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

        //PlayerDataRegistry.load(PlayerDataRegistry[player])

        if (!PlayerDataRegistry[player].finishedTutorial) {
            Tutorial().start(player)
        }

        RandomEntitySpawner.start(player)
    }
}