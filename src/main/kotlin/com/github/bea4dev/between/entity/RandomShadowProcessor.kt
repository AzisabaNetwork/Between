package com.github.bea4dev.between.entity

import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.entity.TickBase
import com.github.bea4dev.vanilla_source.api.entity.tick.TickThread
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.random.Random

private const val RANGE = 64

class RandomShadowProcessor(private val player: Player, private val thread: TickThread) : TickBase {
    companion object {
        private val TICK_THREAD = VanillaSourceAPI.getInstance().tickThreadPool.nextTickThread

        fun start(player: Player) {
            val processor = RandomShadowProcessor(player, TICK_THREAD)
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
                    val upIsSolid = world.getType(x, y + 1, z)?.isSolid ?: true

                    if (!blockIsSolid && downIsSolid && !upIsSolid) {
                        if (Random.nextInt(100) == 0) {
                            ShadowEntity
                                .ofShadow(Location(location.world, x.toDouble(), y.toDouble(), z.toDouble()))
                                .spawn()
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