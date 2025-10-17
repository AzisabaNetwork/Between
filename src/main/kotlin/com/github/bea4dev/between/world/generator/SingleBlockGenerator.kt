package com.github.bea4dev.between.world.generator

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.Random

class SingleBlockGenerator(private val block: Material) : ChunkGenerator() {
    override fun generateBedrock(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        for (x in 0 until 16) {
            for (z in 0 until 16) {
                chunkData.setBlock(x, -64, z, Material.BEDROCK)
            }
        }
    }

    override fun generateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        for (x in 0 until 16) {
            for (z in 0 until 16) {
                for (y in worldInfo.minHeight until worldInfo.maxHeight) {
                    chunkData.setBlock(x, y, z, block)
                }
            }
        }
    }
}