package org.piskotky.antrumcraft.dungeon.builders;

import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public class RoomBuilder implements CellBuilder {
	private String ROOM_FULL = "room_center";
	private String ROOM_HALF = "room_side_s";
	private String ROOM_QUATER = ""; //TODO: Add
	private String ROOM_THREEQUATERS = "room_corner_ne"; 

	@Override
	public void build(Cell cell, WorldGenRegion region, BlockPos pos) {
		int wallCount = 0;
		if (cell.north != SideType.NONE) wallCount++;
		if (cell.east != SideType.NONE) wallCount++;
		if (cell.south != SideType.NONE) wallCount++;
		if (cell.west != SideType.NONE) wallCount++;
		//TODO: Add all variants
		switch (wallCount) {
			case 0:
				buildFull(cell, region, pos);
				break;

			case 1: 
				buildHalf(cell, region, pos);
				break;

			case 2: //TODO: Add this case
				buildCorner(cell, region, pos);
				break;

			case 3: //TODO: Add this too
				break;

			case 4:	//TODO: Add this case
				break;
		}
		buildFull(cell, region, pos);
	}

	private void buildFull(Cell cell, WorldGenRegion region, BlockPos pos) {
		DungeonBuilder.placeStructure(ROOM_FULL, region, pos, Rotation.NONE);
	}

	
	private void buildHalf(Cell cell, WorldGenRegion region, BlockPos pos) {
		Rotation rotation = Rotation.NONE;
		
		if (cell.north == SideType.NONE) rotation = Rotation.CLOCKWISE_180;
		else if (cell.east == SideType.NONE) rotation = Rotation.COUNTERCLOCKWISE_90;
		else rotation = Rotation.CLOCKWISE_90;

		DungeonBuilder.placeStructure(ROOM_HALF, region, pos, rotation);
	}


	private void buildCorner(Cell cell, WorldGenRegion region, BlockPos pos) {
		Rotation rotation = Rotation.NONE;

		if (cell.north == SideType.NONE && cell.west == SideType.NONE) rotation = Rotation.COUNTERCLOCKWISE_90;
		else if (cell.south == SideType.NONE && cell.west == SideType.NONE) rotation = Rotation.CLOCKWISE_180;
		else rotation = Rotation.CLOCKWISE_90;

		DungeonBuilder.placeStructure(ROOM_THREEQUATERS, region, pos, rotation);
	}
}
