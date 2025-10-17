package com.github.bea4dev.between.save

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ServerData {
    private val file = File("server_saves.yml")

    var betweenCount = 0
    var firstSetup = true

    fun load() {
        // 初回はデフォルトで保存してから読む
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            save()
        }

        val yml = YamlConfiguration.loadConfiguration(file)

        if (yml.contains("between-count")) {
            betweenCount = yml.getInt("between-count")
        }
        if (yml.contains("first-setup")) {
            firstSetup = yml.getBoolean("first-setup")
        }
    }

    fun save() {
        val yml = YamlConfiguration()

        yml.set("between-count", betweenCount)
        yml.set("first-setup", firstSetup)

        runCatching {
            file.parentFile?.mkdirs()
            yml.save(file)
        }.onFailure { it.printStackTrace() }
    }
}