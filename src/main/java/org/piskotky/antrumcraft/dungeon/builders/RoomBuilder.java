package org.piskotky.antrumcraft.dungeon.builders;

import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

public class RoomBuilder {
	private static final String ROOM_FULL = "room_center";
	private static final String ROOM_HALF = "room_side_s";
	private static final String ROOM_QUATER = ""; //TODO: Add
	private static final String ROOM_THREEQUATERS = "room_corner_ne"; 

	public static void build(Cell cell, ServerLevel level, BlockPos pos) {
		int wallCount = 0;
		if (cell.north != SideType.NONE) wallCount++;
		if (cell.east != SideType.NONE) wallCount++;
		if (cell.south != SideType.NONE) wallCount++;
		if (cell.west != SideType.NONE) wallCount++;
		//TODO: Add all variants
		switch (wallCount) {
			case 0:
				buildFull(cell, level, pos);
				break;

			case 1: 
				buildHalf(cell, level, pos);
				break;

			case 2: //TODO: Add this case
				buildCorner(cell, level, pos);
				break;

			case 3: //TODO: Add this too
				break;

			case 4:	//TODO: Add this case
				break;
		}
		buildFull(cell, level, pos);
	}

	private static void buildFull(Cell cell, ServerLevel level, BlockPos pos) {
		DungeonBuilder.placeStructure(ROOM_FULL, level, pos, Rotation.NONE);
	}

	
	private static void buildHalf(Cell cell, ServerLevel level, BlockPos pos) {
		Rotation rotation = Rotation.NONE;
		
		if (cell.north == SideType.NONE) rotation = Rotation.CLOCKWISE_180;
		else if (cell.east == SideType.NONE) rotation = Rotation.COUNTERCLOCKWISE_90;
		else rotation = Rotation.CLOCKWISE_90;

		DungeonBuilder.placeStructure(ROOM_HALF, level, pos, rotation);
	}


	private static void buildCorner(Cell cell, ServerLevel level, BlockPos pos) {
		Rotation rotation = Rotation.NONE;

		if (cell.north == SideType.NONE && cell.west == SideType.NONE) rotation = Rotation.COUNTERCLOCKWISE_90;
		else if (cell.south == SideType.NONE && cell.west == SideType.NONE) rotation = Rotation.CLOCKWISE_180;
		else rotation = Rotation.CLOCKWISE_90;

		DungeonBuilder.placeStructure(ROOM_THREEQUATERS, level, pos, rotation);
	}
}
