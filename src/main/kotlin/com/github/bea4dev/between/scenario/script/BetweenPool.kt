package com.github.bea4dev.between.scenario.script

import com.github.bea4dev.between.coroutine.MainThread
import com.github.bea4dev.between.coroutine.play
import com.github.bea4dev.between.scenario.DEFAULT_TEXT_BOX
import com.github.bea4dev.between.scenario.Scenario
import com.github.bea4dev.between.util.backToOverworld
import com.github.bea4dev.between.util.setSound
import com.github.bea4dev.vanilla_source.api.text.TextBox
import kotlinx.coroutines.delay
import org.bukkit.entity.Player
import java.time.Duration

class BetweenPool : Scenario() {
    override suspend fun run(player: Player) {
        blackSpace(player)

        delay(Duration.ofSeconds(2).toMillis())

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "この精神世界に、出口はあるのだろうか。\n\n"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "このまま出られなくても――。\nそんな考えが、頭をよぎる。\n"
        ).setSound().play().await()

        TextBox(
            player,
            DEFAULT_TEXT_BOX,
            "",
            1,
            "何かとても大切なことを、\n忘れている気がする――。\n"
        ).setSound().play().await()

        delay(Duration.ofSeconds(2).toMillis())

        MainThread.sync {
            player.backToOverworld()
        }.await()
    }
}