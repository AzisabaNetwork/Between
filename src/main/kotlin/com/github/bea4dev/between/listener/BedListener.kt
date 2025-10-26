package com.github.bea4dev.between.listener

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent
import com.github.bea4dev.between.coroutine.CoroutineFlagRegistry
import com.github.bea4dev.between.scenario.BLACK_SCREEN_TEXT
import com.github.bea4dev.between.scenario.script.BetweenLibrary
import com.github.bea4dev.between.scenario.script.BetweenOffice
import com.github.bea4dev.between.scenario.script.BetweenPool
import com.github.bea4dev.between.util.setMorning
import com.github.bea4dev.between.world.WorldRegistry
import io.papermc.paper.event.player.PlayerDeepSleepEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Tag
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.inventory.ItemStack
import java.time.Duration

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

        if (worldName == "world") {
            removeBeds(player)
        }

        if (worldName == "tutorial") {
            CoroutineFlagRegistry.TUTORIAL_ENTER_BED.onComplete(player)
            return
        }

        if (isBetween(worldName)) {
            Bukkit.getWorld("world")!!.setMorning()
        }

        when (worldName) {
            "between_library" -> {
                BetweenLibrary().start(player)
            }
            "between_office" -> {
                BetweenOffice().start(player)
            }
            "between_pool" -> {
                BetweenPool().start(player)
            }
            "world" -> {
                val between = WorldRegistry.getNextBetween()
                val location = Location(between, 0.5, 64.0, 0.5)

                player.showTitle(
                    Title.title(
                        Component.text(BLACK_SCREEN_TEXT),
                        Component.empty(),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(3))
                    )
                )

                player.teleport(location)
            }
        }
    }

    private fun isBetween(worldName: String): Boolean {
        return worldName == "tutorial" || worldName.contains("between")
    }

    fun removeBeds(player: Player): Int {
        val inv = player.inventory
        var removed = 0

        for (slot in 0 until inv.size) {
            val item: ItemStack? = inv.getItem(slot)
            if (item != null && Tag.BEDS.isTagged(item.type)) {
                removed += item.amount
                inv.clear(slot)
            }
        }

        inv.itemInOffHand.let { off ->
            if (Tag.BEDS.isTagged(off.type)) {
                removed += off.amount
                inv.setItemInOffHand(null)
            }
        }

        player.updateInventory()
        return removed
    }
}