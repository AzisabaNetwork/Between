package com.github.bea4dev.between.util

import com.github.bea4dev.between.Between
import org.bukkit.World
import world.chiyogami.chiyogamilib.scheduler.WorldThreadRunnable

fun World.schedule(runnable: () -> Unit) {
    object : WorldThreadRunnable(this) {
        override fun run() {
            runnable()
        }
    }.runTask(Between.plugin)
}