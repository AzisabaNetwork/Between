package com.github.bea4dev.between.util

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player

fun Player.backToOverworld() {
    val world = Bukkit.getWorld("world")!!
    val playerRespawnLocation = this.respawnLocation

    if (playerRespawnLocation?.world == world) {
        this.teleport(playerRespawnLocation)
    } else {
        this.teleport(world.spawnLocation)
    }

    this.gameMode = GameMode.SURVIVAL
}

fun World.setMorning() {
    this.time = 1000
}