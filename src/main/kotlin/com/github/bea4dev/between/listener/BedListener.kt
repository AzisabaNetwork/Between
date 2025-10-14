package com.github.bea4dev.between.listener

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent
import com.github.bea4dev.between.coroutine.CoroutineFlagRegistry
import io.papermc.paper.event.player.PlayerDeepSleepEvent
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent

class BedListener : Listener {
    @EventHandler
    fun onPlayerSetRespawn(event: PlayerSetSpawnEvent) {
        // betweenでベッドに入るときはリスポーン場所を上書きしないようにする
        val worldName = event.player.world.name
        if (isBetween(worldName)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerEnterBed(event: PlayerBedEnterEvent) {
        val player = event.player
        val worldName = player.world.name
        val allow = isBetween(worldName)

        if (!allow) {
            return
        }

        event.setUseBed(Event.Result.ALLOW)
    }

    @EventHandler
    fun onPlayerSleep(event: PlayerDeepSleepEvent) {
        val player = event.player
        val worldName = player.world.name

        if (worldName == "tutorial") {
            CoroutineFlagRegistry.TUTORIAL_ENTER_BED.onComplete(player)
            return
        }

        if (isBetween(worldName)) {
            val world = Bukkit.getWorld("world")!!
            val playerRespawnLocation = player.respawnLocation

            if (playerRespawnLocation?.world == world) {
                player.teleport(playerRespawnLocation)
            } else {
                player.teleport(world.spawnLocation)
            }
        }
    }

    private fun isBetween(worldName: String): Boolean {
        return worldName == "tutorial" || worldName.contains("between")
    }
}