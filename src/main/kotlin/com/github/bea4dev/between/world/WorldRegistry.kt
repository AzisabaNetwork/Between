package com.github.bea4dev.between.world

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.chest.ChestProcessor
import com.github.bea4dev.between.dimension.DimensionRegistry
import com.github.bea4dev.between.save.ServerData
import com.github.bea4dev.between.util.schedule
import com.github.bea4dev.between.world.generator.PoolGenerator
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
import org.bukkit.inventory.ItemStack
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
    lateinit var BETWEEN_OFFICE: World
        private set
    lateinit var BETWEEN_POOL: World
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
            deleteDirectory(Path("between_office"))
            deleteDirectory(Path("between_pool"))

            BETWEEN_LIBRARY = Bukkit.createWorld(
                WorldCreator("between_library")
                    .generator(SingleBlockGenerator(Material.LIGHT_GRAY_TERRACOTTA))
                    .environment(World.Environment.NORMAL)
            )!!
            nmsHandler.setDimensionType(BETWEEN_LIBRARY, DimensionRegistry.BETWEEN)
            BETWEEN_LIBRARY.setGameRule(GameRule.DO_MOB_SPAWNING, false)

            BETWEEN_OFFICE = Bukkit.createWorld(
                WorldCreator("between_office")
                    .generator(SingleBlockGenerator(Material.WHITE_CONCRETE))
                    .environment(World.Environment.NORMAL)
            )!!
            nmsHandler.setDimensionType(BETWEEN_OFFICE, DimensionRegistry.BETWEEN)
            BETWEEN_OFFICE.setGameRule(GameRule.DO_MOB_SPAWNING, false)

            BETWEEN_POOL = Bukkit.createWorld(
                WorldCreator("between_pool")
                    .generator(PoolGenerator())
                    .environment(World.Environment.NORMAL)
            )!!
            nmsHandler.setDimensionType(BETWEEN_POOL, DimensionRegistry.BETWEEN_NO_FOG)
            BETWEEN_POOL.setGameRule(GameRule.DO_MOB_SPAWNING, false)

            if (ServerData.firstSetup) {
                BETWEEN_LIBRARY.schedule {
                    val chestProcessor = ChestProcessor(
                        BETWEEN_LIBRARY,
                        listOf(
                            ItemStack(Material.BREAD).also { item -> item.amount = 1 },
                            ItemStack(Material.IRON_INGOT).also { item -> item.amount = 2 },
                            ItemStack(Material.RED_BED).also { item -> item.amount = 1 },
                            ItemStack(Material.MUSIC_DISC_OTHERSIDE).also { item -> item.amount = 1 },
                            ItemStack(Material.REDSTONE).also { item -> item.amount = 3 },
                            ItemStack(Material.GOLD_INGOT).also { item -> item.amount = 1 },
                            ItemStack(Material.BREAD).also { item -> item.amount = 3 },
                        ),
                        5,
                        false
                    )

                    JigsawProcessor(
                        BETWEEN_LIBRARY.getBlockAt(0, 64, 0),
                        JigsawReferenceManager.getFromName(NamespacedKey.fromString("lib_start"))[0],
                        1500,
                        0
                    ).blockProcessor(chestProcessor).start()
                }

                BETWEEN_OFFICE.schedule {
                    val chestProcessor = ChestProcessor(
                        BETWEEN_OFFICE,
                        listOf(
                            ItemStack(Material.BREAD).also { item -> item.amount = 1 },
                            ItemStack(Material.IRON_INGOT).also { item -> item.amount = 2 },
                            ItemStack(Material.RED_BED).also { item -> item.amount = 1 },
                            ItemStack(Material.MUSIC_DISC_MALL).also { item -> item.amount = 1 },
                            ItemStack(Material.REDSTONE).also { item -> item.amount = 3 },
                            ItemStack(Material.GOLD_INGOT).also { item -> item.amount = 1 },
                            ItemStack(Material.BREAD).also { item -> item.amount = 3 },
                        ),
                        5,
                        false
                    )

                    JigsawProcessor(
                        BETWEEN_OFFICE.getBlockAt(0, 64, 0),
                        JigsawReferenceManager.getFromName(NamespacedKey.fromString("office_sn"))[0],
                        1500,
                        0
                    ).blockProcessor(chestProcessor).start()
                }

                BETWEEN_POOL.schedule {
                    val chestProcessor = ChestProcessor(
                        BETWEEN_POOL,
                        listOf(
                            ItemStack(Material.BREAD).also { item -> item.amount = 1 },
                            ItemStack(Material.IRON_INGOT).also { item -> item.amount = 2 },
                            ItemStack(Material.RED_BED).also { item -> item.amount = 1 },
                            ItemStack(Material.MUSIC_DISC_MALL).also { item -> item.amount = 1 },
                            ItemStack(Material.REDSTONE).also { item -> item.amount = 3 },
                            ItemStack(Material.GOLD_INGOT).also { item -> item.amount = 1 },
                            ItemStack(Material.BREAD).also { item -> item.amount = 3 },
                        ),
                        5,
                        false
                    )

                    JigsawProcessor(
                        BETWEEN_POOL.getBlockAt(0, 64, 0),
                        JigsawReferenceManager.getFromName(NamespacedKey.fromString("pool_sn"))[0],
                        500,
                        0
                    ).blockProcessor(chestProcessor).start()
                }

                ServerData.firstSetup = false
            }

            then()
        })
    }

    fun getNextBetween(): World {
        val between = when (ServerData.betweenCount) {
            0 -> BETWEEN_LIBRARY
            1 -> BETWEEN_OFFICE
            else -> BETWEEN_POOL
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