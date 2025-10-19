package com.github.bea4dev.between.scenario

import com.github.bea4dev.between.coroutine.MainThread
import com.github.bea4dev.between.coroutine.launch
import com.github.bea4dev.between.world.WorldRegistry
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.entity.tick.TickThread
import com.github.bea4dev.vanilla_source.api.nms.entity.NMSEntityController
import com.google.gson.JsonParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Boat
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.io.InputStreamReader
import java.net.URL
import java.time.Duration
import java.util.UUID


val SCENARIO_TICK_THREAD: TickThread = VanillaSourceAPI.getInstance().tickThreadPool.nextTickThread
const val DEFAULT_TEXT_BOX = "\uE201"
const val BLACK_SCREEN_TEXT = "\uE101"

abstract class Scenario {
    protected abstract suspend fun run(player: Player)

    fun start(player: Player) {
        SCENARIO_TICK_THREAD.launch {
            run(player)
        }
    }

    fun black(player: Player) {
        player.showTitle(
            Title.title(
                Component.text(BLACK_SCREEN_TEXT),
                Component.empty(),
                Title.Times.times(Duration.ZERO, Duration.ofHours(1), Duration.ZERO)
            )
        )
    }

    fun blackFeedOut(player: Player, feedOutMilli: Long) {
        player.showTitle(
            Title.title(
                Component.text(BLACK_SCREEN_TEXT),
                Component.empty(),
                Title.Times.times(Duration.ofMillis(feedOutMilli), Duration.ofHours(1), Duration.ZERO)
            )
        )
    }

    fun blackFeedIn(player: Player, feedInMilli: Long) {
        player.showTitle(
            Title.title(
                Component.text(BLACK_SCREEN_TEXT),
                Component.empty(),
                Title.Times.times(Duration.ZERO, Duration.ofMillis(1), Duration.ofMillis(feedInMilli))
            )
        )
    }

    fun clearBlack(player: Player) {
        player.resetTitle()
    }

    private var blackSpaceEntity: NMSEntityController? = null

    suspend fun blackSpace(player: Player) {
        MainThread.sync {
            player.gameMode = GameMode.SPECTATOR
            player.teleport(Location(WorldRegistry.TUTORIAL, 24.5, 4.0, 40.5))
        }.await()

        val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler

        if (blackSpaceEntity != null) {
            val destroyPacket = nmsHandler.createEntityDestroyPacket(blackSpaceEntity)
            nmsHandler.sendPacket(player, destroyPacket)
        }
        blackSpaceEntity = nmsHandler.createNMSEntityController(WorldRegistry.TUTORIAL, 24.5, 4.0, 40.5, EntityType.BOAT, null)
        val bukkitBoat = blackSpaceEntity?.bukkitEntity as Boat
        bukkitBoat.boatType = Boat.Type.BAMBOO
        val spawnPacket = nmsHandler.createSpawnEntityPacket(blackSpaceEntity)
        nmsHandler.sendPacket(player, spawnPacket)

        val cameraPacket = nmsHandler.createCameraPacket(blackSpaceEntity)
        nmsHandler.sendPacket(player, cameraPacket)
    }
}

fun getPlayerSkin(uuid: UUID): Pair<String, String> {
    try {
        val url =
            URL(("https://sessionserver.mojang.com/session/minecraft/profile/$uuid") + "?unsigned=false")
        val reader = InputStreamReader(url.openStream())
        val property = JsonParser().parse(reader).asJsonObject.get("properties").asJsonArray.get(0).asJsonObject
        val texture = property["value"].asString
        val signature = property["signature"].asString
        return Pair(texture, signature)
    } catch (e: Exception) {
        e.printStackTrace()
        return Pair("", "")
    }
}