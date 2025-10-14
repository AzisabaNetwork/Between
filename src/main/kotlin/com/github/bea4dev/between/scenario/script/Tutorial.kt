package com.github.bea4dev.between.scenario.script

import com.github.bea4dev.between.coroutine.CoroutineFlagRegistry
import com.github.bea4dev.between.coroutine.MainThread
import com.github.bea4dev.between.coroutine.async
import com.github.bea4dev.between.coroutine.play
import com.github.bea4dev.between.scenario.DEFAULT_TEXT_BOX
import com.github.bea4dev.between.scenario.Scenario
import com.github.bea4dev.between.scenario.getPlayerSkin
import com.github.bea4dev.between.util.createCamera
import com.github.bea4dev.between.util.setSound
import com.github.bea4dev.between.world.WorldRegistry
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.camera.CameraPositionAt
import com.github.bea4dev.vanilla_source.api.camera.CameraPositionsManager
import com.github.bea4dev.vanilla_source.api.nms.entity.NMSEntityController
import com.github.bea4dev.vanilla_source.api.player.EnginePlayer
import com.github.bea4dev.vanilla_source.api.text.TextBox
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.time.Duration
import java.util.UUID

class Tutorial : Scenario() {
    private val PLAYER_POSITION = Vector(51.5, 9.5, 61.5)

    override suspend fun run(player: Player) {
        blackSpace(player)

        MainThread.sync {
            player.inventory.clear()
        }.await()

        val playerSkin = async {
            getPlayerSkin(player.uniqueId)
        }.await()

        val camera0 = createCamera(player)
        val camera0Positions = CameraPositionsManager.getCameraPositionsByName("tutorial_0")
        camera0.setCameraPositions(camera0Positions)
        camera0.setLookAtPositions(CameraPositionAt(PLAYER_POSITION))
        camera0.prepare()
        camera0.autoEnd(false)
        camera0.shake(false)

        val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler
        val enginePlayer = EnginePlayer.getEnginePlayer(player)

        val profile = GameProfile(UUID.randomUUID(), player.name)
        profile.properties.put("textures", Property("textures", playerSkin.first, playerSkin.second))
        val npc = nmsHandler.createNMSEntityController(
            player.world,
            PLAYER_POSITION.x,
            PLAYER_POSITION.y,
            PLAYER_POSITION.z,
            EntityType.PLAYER,
            profile
        )
        val stand = nmsHandler.createNMSEntityController(
            player.world,
            PLAYER_POSITION.x,
            PLAYER_POSITION.y,
            PLAYER_POSITION.z,
            EntityType.ITEM_DISPLAY,
            null
        )
        npc.setRotation(90.0F, 0.0F)
        npc.show(null, enginePlayer)
        stand.show(null, enginePlayer)

        val mountPacket = nmsHandler.createSetPassengersPacket(stand, intArrayOf(npc.bukkitEntity.entityId))
        nmsHandler.sendPacket(player, mountPacket)

        delay(Duration.ofSeconds(2).toMillis())

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "残業帰り。ベッドへ沈む。\n通知は鳴りやまない。\n画面を伏せ、目を閉じる。"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "おやすみ。\n\n今日は、ちゃんと眠れますように。"
        ).setSound().play().await()

        delay(Duration.ofSeconds(2).toMillis())

        blackFeedIn(player, Duration.ofSeconds(2).toMillis())

        camera0.start()

        delay(Duration.ofSeconds(2).toMillis())

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "気づけばそこにいた。\n\n"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "広い室内。椅子がひとつ。\n四角い、見覚えのある世界。\n"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "懐かしいけれど、少しだけ違う。\n\n"
        ).setSound().play().await()

        blackFeedOut(player, Duration.ofSeconds(2).toMillis())

        delay(Duration.ofSeconds(2).toMillis())

        camera0.end()

        npc.hide(null, enginePlayer)
        stand.hide(null, enginePlayer)

        MainThread.sync {
            val location = Location(WorldRegistry.TUTORIAL, 50.5, 9.0, 61.5)
            location.yaw = 90.0F
            player.teleport(location)
            player.respawnLocation = location
            player.gameMode = GameMode.ADVENTURE
        }.await()

        blackFeedIn(player, Duration.ofSeconds(2).toMillis())

        CoroutineFlagRegistry.TUTORIAL_ENTER_BED.get(player).future().await()

        blackSpace(player)

        delay(Duration.ofSeconds(3).toMillis())

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "どうやら幼い頃に遊んでいたゲームの世界へ\n閉じ込められたらしい。\n"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "でも、これで少し休める――\nそう思うと自然と肩の力が抜けるのを感じた。\n"
        ).setSound().play().await()

        delay(Duration.ofSeconds(3).toMillis())

        MainThread.sync {
            val world = Bukkit.getWorld("world")!!
            player.teleport(world.spawnLocation)
            player.gameMode = GameMode.SURVIVAL
        }.await()

        delay(Duration.ofSeconds(2).toMillis())

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "懐かしい世界。\n\n"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "今は少しだけ休もう――\n\n"
        ).setSound().play().await()
    }

    private var blackSpaceEntity: NMSEntityController? = null

    private suspend fun blackSpace(player: Player) {
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
        val spawnPacket = nmsHandler.createSpawnEntityPacket(blackSpaceEntity)
        nmsHandler.sendPacket(player, spawnPacket)

        val cameraPacket = nmsHandler.createCameraPacket(blackSpaceEntity)
        nmsHandler.sendPacket(player, cameraPacket)
    }
}