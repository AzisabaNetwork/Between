package com.github.bea4dev.between.util

import com.github.bea4dev.vanilla_source.api.text.TextBox
import net.kyori.adventure.sound.Sound

fun TextBox.setSound(): TextBox {
    this.setHigherSound(Sound.sound(org.bukkit.Sound.ENTITY_ITEM_PICKUP, Sound.Source.MASTER, Float.MAX_VALUE, 1.0F))
    this.setLowerSound(Sound.sound(org.bukkit.Sound.ENTITY_ITEM_PICKUP, Sound.Source.MASTER, Float.MAX_VALUE, 1.0F))
    return this
}