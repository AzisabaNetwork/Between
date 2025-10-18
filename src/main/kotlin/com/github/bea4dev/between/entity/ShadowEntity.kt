package com.github.bea4dev.between.entity

import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.entity.EngineEntity
import com.github.bea4dev.vanilla_source.api.entity.ai.pathfinding.BlockPosition
import com.github.bea4dev.vanilla_source.api.util.collision.CollideOption
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector
import java.util.UUID
import kotlin.random.Random

private val ENTITY_TICK_THREAD = VanillaSourceAPI.getInstance().tickThreadPool.nextTickThread

class ShadowEntity(
    location: Location, gameProfile: GameProfile, private val isShy: Boolean
) : EngineEntity(
    ENTITY_TICK_THREAD.threadLocalCache.getGlobalWorld(location.world.name),
    VanillaSourceAPI.getInstance().nmsHandler.createNMSEntityController(
        location.world, location.x, location.y, location.z,
        EntityType.PLAYER, gameProfile
    ),
    ENTITY_TICK_THREAD,
    null
) {
    companion object {
        fun ofDefaultSkin(location: Location): ShadowEntity {
            val profile = GameProfile(UUID.randomUUID(), "旧友")
            profile.properties.put(
                "textures", Property(
                    "textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9",
                    "tTCtASRIyuzlNUgSoXgUr6arxABhCR4EQ9+eHaUoO8bADljmUFoQfb6oba8zqe2gIa2mnu5KQaOPQCxcTDjgNv9aIL2smINKxy/60VE4Mgnrh5ntH+mGuDi00V3Bk2CsObFZXz1vgk2UxdQUQ41eVQYm2xBrXFEbXMSoTafWGv0FMTPFpGxGRdduTe3QTEie3GcfAMHCn/9xMMmUxZZ6UVZ+mDe8ARt9/cmK+GmqT8m3kmrz/vq+i29KV4tWvJqsKIVAXm97jVPH9XxVR3tYlheimQSFNrCU8SzNPum/ZhxNAf5Uw90+/K0eaJE59y8tS7KDV5DHrRrHHXb/ywGGklSri1YjFm9AEBk6BeH8Y3Ot/e+zfQbF3rOny2DkBAm/v28FooYd25gXB4MjUFNPj3KdveQh7DpRAvnkmBZMqJCO+Z9fdY4Dw+jmqjII88r6mukWAODvXed/x8bvv55zzNOAxtqtwBTWHIdqWFr/7pMZF26RY1Tluw+pAWGWaKMHtqlGzyOLGMxMKwXqtLNEpIYw52ETwGKaWh8h34cOoI8dhpjfjym4UOihMmazK9LC0EUEHuBlgy5b/Ae71+6UsLNIX8bJwIvN16sP6wpSTNbV6htWoS7/ehvoxdKhI6XEUqWgEoAwmquClPfWiveCV057reoKeVHB9RdTl0sW+HM="
                )
            )

            return ShadowEntity(location, profile, false)
        }

        fun ofShadow(location: Location): ShadowEntity {
            val profile = GameProfile(UUID.randomUUID(), "？？？？？")
            profile.properties.put(
                "textures", Property(
                    "textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTYyMzAzMjA4MTQ3NiwKICAicHJvZmlsZUlkIiA6ICJkZGVkNTZlMWVmOGI0MGZlOGFkMTYyOTIwZjdhZWNkYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEaXNjb3JkQXBwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VhY2NiMDdmMDUzNmRkODE3YzE1MjVhM2EzZjk3ZTQyODA5NWM5YjI5ODQ2MGJjN2EzYTNlMWI4YjlmYzcxZmEiCiAgICB9CiAgfQp9",
                    "mxwj4Y2K3DI5cF+VNSWSntMa13cXV6SbuAVMgpsY2zrc3UWJrd9Mxy1qYefNfHL1bJMDrwZoyyTrlU3EDisoGPd1xckfOXIKThDQQVLHvhEqYjj5AAZY/9ljh0cxhDGMq/eT4Xs6vMH25jD/dCgGibcgvzeKoqk41j1v4qZXsn5tCSzXJLnVK/GjdjUSOnRs/PIjERRhuq0We9fmOEBzKA3FVHk6O1g+iIsSYDJaw2XIALcYOQlz4p0PYqctFaEGEPeM5TlZDgrSVdTEjYlENq4ktp7yIBARKh0DS5x/lnM1PY0UJ7JNLqvGczi5SyzlcoEhfqWInz97k/YHSPSMtXq3Ny57qCBnq34mXm4PBXjCjIbBo0YkL62CWHrF3HfFlTtwzkX/Ck9ehYZphXZX3d8ZS1rn+nxn/cnRa4bX8x1c+3vemZtUQpDPHfHho23qG22QtGv8kvnE+Si6WioxAkbGDx1uK0UafxeuV3KzqGX4P34InwMMB+DUxJR1ddEtPjbizCLtzCkomjXHBrhfA+TaoKzE2WAsUKKEgV17IV0YOGrkM1IOElmlyFKBCny/6Pbq473lkZH4kGRjvhLjpKeHlRuicUK4d1x/mb1Wn5rJnfOH36lUXZwrQcp4DZFnbrfoisoetelV0XA9l5FHBO86KYBnvZS9hJcM37Z28B4="
                )
            )

            return ShadowEntity(location, profile, true)
        }
    }

    private val bukkitWorld = location.world
    private var tickCount = 0
    private val rayCollideOption = CollideOption(
        FluidCollisionMode.NEVER,
        true,
        0.0,
        { block -> block.blockData.isOccluding },
        null,
        null
    )

    init {
        super.collideEntities = true
    }

    override fun tick() {
        super.tick()

        tickCount++

        val position = super.position

        if (isShy && tickCount % 10 == 0) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.world != bukkitWorld) {
                    continue
                }
                if (player.location.toVector().distance(position) > 64) {
                    continue
                }

                val startPosition = player.eyeLocation.toVector()
                val direction = position.clone().add(Vector(0.0, 1.5, 0.0)).subtract(startPosition)

                val result = super.world.rayTraceBlocks(
                    startPosition,
                    direction.normalize(),
                    direction.length(),
                    rayCollideOption
                )

                if (result != null) {
                    continue
                }

                ENTITY_TICK_THREAD.scheduleTask({ super.kill() }, 20 * 1)
                break
            }
        }

        if (isShy && tickCount % 200 == 0) {
            val randomDestination =
                position.clone().add(Vector(Random.nextInt(32) - 16, Random.nextInt(32) - 16, Random.nextInt(32) - 16))
            super.aiController.navigator.navigationGoal =
                BlockPosition(randomDestination.blockX, randomDestination.blockY, randomDestination.blockZ)
        }
    }
}