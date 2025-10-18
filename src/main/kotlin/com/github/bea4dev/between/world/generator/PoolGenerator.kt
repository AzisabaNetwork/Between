package com.github.bea4dev.between.world.generator

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.Random

class PoolGenerator : ChunkGenerator() {
    override fun generateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        for (x in 0 until 16) {
            for (z in 0 until 16) {
                for (y in worldInfo.minHeight until 64 + 5) {
                    chunkData.setBlock(x, y, z, Material.WHITE_CONCRETE)
                }
                chunkData.setBlock(x, 64 + 5, z, Material.BARRIER)
            }
        }
    }

    override fun generateBedrock(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        for (x in 0 until 16) {
            for (z in 0 until 16) {
                chunkData.setBlock(x, -64, z, Material.BEDROCK)
            }
        }
    }
}