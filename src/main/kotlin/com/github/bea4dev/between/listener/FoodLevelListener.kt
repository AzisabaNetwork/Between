package com.github.bea4dev.between.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

class FoodLevelListener: Listener {
    @EventHandler
    fun onPlayerChangeFoodLevel(event: FoodLevelChangeEvent) {
        if (event.entity.world.name == "tutorial") {
            event.isCancelled = true
        }
    }
}