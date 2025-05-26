package org.piskotky.antrumcraft.dungeon.builders;

import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

public class StaircaseBuilder {
	private static final String STAIRCASE = "staircase_s";
	
	public static void build(Cell cell, ServerLevel level, BlockPos pos) {
		Rotation rotation = Rotation.NONE;

		if (cell.north != SideType.WALL) rotation = Rotation.CLOCKWISE_180;  
		else if (cell.east != SideType.WALL) rotation = Rotation.COUNTERCLOCKWISE_90;  
		if (cell.west != SideType.WALL) rotation = Rotation.CLOCKWISE_90;  

		DungeonBuilder.placeStructure(STAIRCASE, level, pos, rotation);	
	}
}
