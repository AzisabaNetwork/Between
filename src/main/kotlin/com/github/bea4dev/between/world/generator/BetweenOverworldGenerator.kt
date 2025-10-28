package com.github.bea4dev.between.world.generator

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.Random

private class NoiseVariables(seed: Long) {
    val baseNoise: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed).build())
        .scale(0.008)
        .build()
}

class BetweenOverworldGenerator(seed: Long) : ChunkGenerator() {
    private val variables = ThreadLocal.withInitial { NoiseVariables(seed) }

    override fun shouldGenerateCaves(): Boolean {
        return true
    }

    override fun shouldGenerateDecorations(): Boolean {
        return true
    }

    override fun generateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        val variables = this.variables.get()

        for (x in 0 until 16) {
            for (z in 0 until 16) {
                val worldX = chunkX * 16 + x
                val worldZ = chunkZ * 16 + z

                val surfaceHeight = (variables.baseNoise.evaluateNoise(worldX.toDouble(), worldZ.toDouble()) * 8 + 64).toInt()

                for (y in worldInfo.minHeight until worldInfo.maxHeight) {
                    if (y == surfaceHeight) {
                        chunkData.setBlock(x, y, z, Material.GRASS_BLOCK)
                    } else if (y < surfaceHeight) {
                        chunkData.setBlock(x, y, z, Material.STONE)
                    }
                }
            }
        }
    }
}