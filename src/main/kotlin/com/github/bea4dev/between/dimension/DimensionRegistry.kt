package com.github.bea4dev.between.dimension

import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.dimension.DimensionTypeContainer

object DimensionRegistry {
    lateinit var BETWEEN: Any
        private set
    lateinit var BETWEEN_NO_FOG: Any

    fun init() {
        val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler

        val betweenDimensionTypeContainer = DimensionTypeContainer.DimensionTypeContainerBuilder()
            .hasSkyLight(true)
            .ultraWarm(false)
            .effects(DimensionTypeContainer.EffectsType.THE_NETHER)
            .fixedTime(6000)
            .ambientLight(0.0f)
            .monsterSettings(DimensionTypeContainer.MonsterSettings(false, false, 15))
            .bedWorks(true)
            .build()
        BETWEEN = nmsHandler.createDimensionType("between", betweenDimensionTypeContainer)

        val betweenNoFogDimensionTypeContainer = DimensionTypeContainer.DimensionTypeContainerBuilder()
            .hasSkyLight(true)
            .ultraWarm(false)
            .effects(DimensionTypeContainer.EffectsType.OVERWORLD)
            .fixedTime(6000)
            .ambientLight(0.0f)
            .monsterSettings(DimensionTypeContainer.MonsterSettings(false, false, 15))
            .bedWorks(true)
            .build()
        BETWEEN_NO_FOG = nmsHandler.createDimensionType("between_no_fog", betweenNoFogDimensionTypeContainer)
    }
}