package com.github.bea4dev.between.save

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object PlayerDataRegistry {
    private val map = ConcurrentHashMap<UUID, PlayerData>()
    private val file = File("player_save.yml")
    private lateinit var yml: YamlConfiguration

    operator fun get(player: Player): PlayerData {
        return map.computeIfAbsent(player.uniqueId) { PlayerData(player) }
    }

    fun saveAll() {
        for (data in map.values) {
            data.save(yml)
        }
        yml.save(file)
    }

    fun loadAll() {
        if (!file.exists()) {
            file.createNewFile()
        }
        yml = YamlConfiguration.loadConfiguration(file)
    }

    fun load(playerData: PlayerData) {
        playerData.load(yml)
    }
}

class PlayerData(player: Player) {
    val uuid = player.uniqueId

    var finishedTutorial = false

    fun save(yml: YamlConfiguration) {
        yml.set("$uuid.finishedTutorial", finishedTutorial)
    }

    fun load(yml: YamlConfiguration) {
        if (yml.contains(uuid.toString())) {
            finishedTutorial = yml.getBoolean("$uuid.finishedTutorial")
        }
    }
}