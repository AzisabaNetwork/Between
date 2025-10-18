package com.github.bea4dev.between.chest

import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.inventory.ItemStack
import java.util.Random
import kotlin.math.abs

class ChestProcessor(
    private val world: World,
    private val itemList: List<ItemStack>,
    private val maxItemsPerChest: Int = 3,
    private val replaceIfNotEmpty: Boolean = false
) : com.github.bea4dev.vanilla_source.api.asset.BlockProcessor {

    override fun processBlock(block: Block) {
        val state = block.state
        if (state is Chest) {
            fillChestDeterministically(state)
        }
    }

    private fun fillChestDeterministically(chest: Chest) {
        // ダブルチェストもまとめて扱いたいなら ↓ を `val inv = chest.inventory` に変える
        val inv = chest.blockInventory

        // 既に中身がある場合はスキップ（必要なら置換も可）
        if (!replaceIfNotEmpty && !inv.isEmpty) return
        if (replaceIfNotEmpty) inv.clear()

        // シードはワールドUUID + 座標
        val loc = chest.block.location
        val seed = seedFrom(
            world.mostSignificantUuidBits(), world.leastSignificantUuidBits(),
            loc.blockX, loc.blockY, loc.blockZ
        )
        val rng = Random(seed)

        // 1〜maxItemsPerChest
        val count = 1 + rng.nextInt(maxItemsPerChest.coerceAtLeast(1))

        val usedSlots = HashSet<Int>()
        repeat(count) {
            var slot: Int
            do {
                slot = rng.nextInt(inv.size)
            } while (!usedSlots.add(slot))

            val template = itemList[rng.nextInt(itemList.size)]
            val stack = template.clone()

            inv.setItem(slot, stack)
        }
    }

    private fun seedFrom(worldMsb: Long, worldLsb: Long, x: Int, y: Int, z: Int): Long {
        var h = worldMsb xor worldLsb.rotateLeft(13)
        h = (h xor (x.toLong() * 73856093L) xor MIX_A).mix() xor MIX_B
        h = (h xor (y.toLong() * 19349663L) xor MIX_A).mix() xor MIX_B
        h = (h xor (z.toLong() * 83492791L) xor MIX_A).mix() xor MIX_B
        return abs(h)
    }

    private val MIX_A: Long = 0x9E3779B97F4A7C15uL.toLong()
    private val MIX_B: Long = 0xC2B2AE3D27D4EB4FuL.toLong()
    private val MUL_1: Long = 0xBF58476D1CE4E5B9uL.toLong()
    private val MUL_2: Long = 0x94D049BB133111EBuL.toLong()

    private fun Long.mix(): Long {
        var x = this
        x = (x xor (x ushr 30)) * MUL_1
        x = (x xor (x ushr 27)) * MUL_2
        return x xor (x ushr 31)
    }

    private fun Long.rotateLeft(distance: Int): Long =
        (this shl distance) or (this ushr (64 - distance))

    private fun World.mostSignificantUuidBits(): Long = this.uid.mostSignificantBits
    private fun World.leastSignificantUuidBits(): Long = this.uid.leastSignificantBits
}
