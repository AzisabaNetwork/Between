package com.github.bea4dev.between.biome

import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.biome.BiomeDataContainer
import org.bukkit.Color

object BiomeRegistry {
    lateinit var BETWEEN: Any
        private set

    fun init() {
        val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler

        val between = BiomeDataContainer()
        nmsHandler.setDefaultBiomeData(between)
        between.fogColorRGB = Color.GRAY.asRGB()
        between.music = "minecraft:none"
        between.temperature = 0.8F
        between.temperatureAttribute = BiomeDataContainer.TemperatureAttribute.NORMAL
        between.grassBlockColorRGB = rgbIntFromHex("91BD59")
        BETWEEN = nmsHandler.createBiome("between", between)
    }

    fun rgbIntFromHex(hex: String): Int {
        val s = hex.removePrefix("#")
        require(s.length == 6) { "Hex must be 6 digits like RRGGBB" }
        val r = s.take(2).toInt(16)
        val g = s.substring(2, 4).toInt(16)
        val b = s.substring(4, 6).toInt(16)
        return (r shl 16) or (g shl 8) or b
    }
}