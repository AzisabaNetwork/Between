package com.github.bea4dev.between.world

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.dimension.DimensionRegistry
import com.github.bea4dev.between.save.ServerData
import com.github.bea4dev.between.util.schedule
import com.github.bea4dev.between.world.generator.SingleBlockGenerator
import com.github.bea4dev.between.world.generator.VoidGenerator
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.asset.JigsawProcessor
import com.github.bea4dev.vanilla_source.api.asset.JigsawReferenceManager
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.WorldCreator
import world.chiyogami.chiyogamilib.scheduler.WorldThreadRunnable
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path

object WorldRegistry {
    lateinit var TUTORIAL: World
        private set
    lateinit var BETWEEN_LIBRARY: World
        private set

    fun init(then: () -> Unit) {
        Bukkit.getScheduler().runTask(Between.plugin, Runnable {
            val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler

            TUTORIAL = Bukkit.createWorld(
                WorldCreator("tutorial")
                    .generator(VoidGenerator())
                    .environment(World.Environment.NORMAL)
            )!!
            nmsHandler.setDimensionType(TUTORIAL, DimensionRegistry.BETWEEN)

            deleteDirectory(Path("between_library"))

            BETWEEN_LIBRARY = Bukkit.createWorld(
                WorldCreator("between_library")
                    .generator(SingleBlockGenerator(Material.LIGHT_GRAY_TERRACOTTA))
                    .environment(World.Environment.NORMAL)
            )!!
            nmsHandler.setDimensionType(BETWEEN_LIBRARY, DimensionRegistry.BETWEEN)

            BETWEEN_LIBRARY.setGameRule(GameRule.DO_MOB_SPAWNING, false)

            if (ServerData.firstSetup) {
                BETWEEN_LIBRARY.schedule {
                    JigsawProcessor(
                        BETWEEN_LIBRARY.getBlockAt(0, 64, 0),
                        JigsawReferenceManager.getFromName(NamespacedKey.fromString("lib_start")).get(0),
                        1000,
                        0
                    ).start()
                }

                ServerData.firstSetup = false
            }

            then()
        })
    }

    fun getNextBetween(): World {
        val between = when (ServerData.betweenCount) {
            0 -> BETWEEN_LIBRARY
            else -> BETWEEN_LIBRARY
        }
        ServerData.betweenCount++

        return between
    }

    @Throws(IOException::class)
    private fun deleteDirectory(directory: Path) {
        if (!Files.exists(directory)) {
            return
        }

        Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                Files.delete(file)
                return FileVisitResult.CONTINUE
            }

            @Throws(IOException::class)
            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                Files.delete(dir)
                return FileVisitResult.CONTINUE
            }
        })
    }
}