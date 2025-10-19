package com.github.bea4dev.between.entity

import com.github.bea4dev.between.Between
import com.github.bea4dev.between.world.WorldRegistry
import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.entity.TickBase
import com.github.bea4dev.vanilla_source.api.entity.tick.TickThread
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

private const val RANGE = 64
private const val MAX_SPAWN_COUNT = 6

object GlobalShadowEntityCounter {
    private val map = ConcurrentHashMap<String, AtomicInteger>()

    @Synchronized
    fun trySpawn(world: World, runnable: Runnable) {
        val counter = map.computeIfAbsent(world.name) { AtomicInteger() }
        if (counter.get() > MAX_SPAWN_COUNT) {
            return
        }
        counter.incrementAndGet()

        runnable.run()
    }

    fun onDespawn(world: World) {
        map[world.name]!!.decrementAndGet()
    }
}

class RandomEntitySpawner(private val player: Player, private val thread: TickThread) : TickBase {
    companion object {
        private val TICK_THREAD = VanillaSourceAPI.getInstance().tickThreadPool.nextTickThread

        fun start(player: Player) {
            val processor = RandomEntitySpawner(player, TICK_THREAD)
            TICK_THREAD.addEntity(processor)
        }
    }

    private var tickCount = 0

    override fun tick() {
        tickCount++

        if (tickCount % 200 != 0) {
            return
        }

        val location = player.location

        if (!location.world.name.contains("between")) {
            return
        }

        val world = thread.threadLocalCache.getGlobalWorld(location.world.name)

        for (x in (location.blockX - RANGE) until (location.blockX + RANGE)) {
            for (y in (location.blockY - RANGE) until (location.blockY + RANGE)) {
                for (z in (location.blockZ - RANGE) until (location.blockZ + RANGE)) {
                    val blockIsSolid = world.getType(x, y, z)?.isSolid ?: true
                    val downIsSolid = world.getType(x, y - 1, z)?.isSolid ?: false
                    val downIsBarrier = world.getType(x, y - 1, z) == Material.BARRIER
                    val upIsSolid = world.getType(x, y + 1, z)?.isSolid ?: true

                    if (!blockIsSolid && downIsSolid && !upIsSolid && !downIsBarrier) {
                        if (Random.nextInt(5000) == 0) {
                            GlobalShadowEntityCounter.trySpawn(location.world) {
                                ShadowEntity
                                    .ofShadow(
                                        Location(
                                            location.world,
                                            x.toDouble() + 0.5,
                                            y.toDouble(),
                                            z.toDouble() + 0.5
                                        )
                                    )
                                    .spawn()
                            }
                        }

                        if (location.world == WorldRegistry.BETWEEN_OFFICE && Random.nextInt(5000) == 0) {
                            Bukkit.getScheduler().runTask(Between.plugin, Runnable {
                                if (location.world.entities.stream()
                                        .filter { entity -> entity.type == EntityType.WITHER_SKELETON }.count() < 20
                                ) {
                                    location.world.spawnEntity(
                                        Location(
                                            location.world,
                                            x.toDouble() + 0.5,
                                            y.toDouble(),
                                            z.toDouble() + 0.5
                                        ), EntityType.WITHER_SKELETON
                                    )
                                }
                            })
                        }
                    }
                }
            }
        }

    }

    override fun shouldRemove(): Boolean {
        return !player.isOnline
    }
}