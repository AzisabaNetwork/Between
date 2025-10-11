package com.github.bea4dev.between.world

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.dimension.DimensionRegistry
import com.github.bea4dev.between.world.generator.VoidGenerator
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

object WorldRegistry {
    lateinit var TUTORIAL: World
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

            then()
        })
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