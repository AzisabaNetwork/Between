package com.github.bea4dev.between.dimension

import com.github.bea4dev.vanilla_source.api.VanillaSourceAPI
import com.github.bea4dev.vanilla_source.api.dimension.DimensionTypeContainer

object DimensionRegistry {
    lateinit var BETWEEN: Any
        private set

    fun init() {
        val slDimensionTypeContainer = DimensionTypeContainer.DimensionTypeContainerBuilder()
            .hasSkyLight(true)
            .ultraWarm(false)
            .effects(DimensionTypeContainer.EffectsType.THE_NETHER)
            .fixedTime(6000)
            .ambientLight(0.0f)
            .monsterSettings(DimensionTypeContainer.MonsterSettings(false, false, 15))
            .bedWorks(true)
            .build()
        val nmsHandler = VanillaSourceAPI.getInstance().nmsHandler
        BETWEEN = nmsHandler.createDimensionType("between", slDimensionTypeContainer)
    }
}