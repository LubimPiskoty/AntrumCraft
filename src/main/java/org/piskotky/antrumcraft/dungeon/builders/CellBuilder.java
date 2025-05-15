package org.piskotky.antrumcraft.dungeon.builders;

import org.piskotky.antrumcraft.dungeon.Cell;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;

public interface CellBuilder {
	public void build(Cell cell, WorldGenRegion region, BlockPos pos);
}
