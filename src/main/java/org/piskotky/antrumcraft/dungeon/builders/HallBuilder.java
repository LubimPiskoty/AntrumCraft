package org.piskotky.antrumcraft.dungeon.builders;

import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.CellType;
import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public class HallBuilder implements CellBuilder {
	private static String HALL_STRAIGHT = "hall_ns";
	private static String HALL_4WAY = "hall_4way";
	private static String HALL_3WAY = "hall_3way_nse";
	private static String HALL_CORNER = "hall_corner_ne";

	@Override
	public void build(Cell cell, WorldGenRegion region, BlockPos pos) {
		// Count the number of walls
		int wallCount = 0;
		if (cell.north != SideType.NONE) wallCount++;
		if (cell.east != SideType.NONE) wallCount++;
		if (cell.south != SideType.NONE) wallCount++;
		if (cell.west != SideType.NONE) wallCount++;

		switch (wallCount) {
			case 0:
				build4Way(cell, region, pos);
				break;
			case 1:
				build3Way(cell, region, pos);
				break;
			case 2:
				if ((cell.north == SideType.NONE && cell.south == SideType.NONE) ||
						(cell.east == SideType.NONE && cell.west == SideType.NONE))  // It is a straight piece
					buildStraight(cell, region, pos);
				else
					buildCorner(cell, region, pos);
				break;
		}
	}

	private void buildStraight(Cell cell, WorldGenRegion region, BlockPos pos) {
		Rotation rotation = cell.north != SideType.NONE ? // It is a ew type needs to be flipped
							Rotation.CLOCKWISE_90 : Rotation.NONE;

		DungeonBuilder.placeStructure(HALL_STRAIGHT, region, pos, rotation);
	}

	private void buildCorner(Cell cell, WorldGenRegion region, BlockPos pos) {
		Rotation rotation;
		//WARNING: Current hall_corner_ne is not NE but SW so if it is changed also change this
		if (cell.north == SideType.NONE) { // There is path on top
			rotation = cell.east == SideType.NONE ? 
				Rotation.NONE:// Has to be SW
				Rotation.COUNTERCLOCKWISE_90;// Has to be SE
		
		}else { // There has to be path down
			rotation = cell.east == SideType.NONE ?
				Rotation.CLOCKWISE_90 : // Has to be NW
				Rotation.CLOCKWISE_180; // Has to be NE
		}

		DungeonBuilder.placeStructure(HALL_CORNER, region, pos, rotation);
	}

	private void build3Way(Cell cell, WorldGenRegion region, BlockPos pos) {
		Rotation rotation;

		if (cell.north != SideType.NONE) rotation = Rotation.CLOCKWISE_90; 	
		else if (cell.east != SideType.NONE) rotation = Rotation.CLOCKWISE_180; 	
		else if (cell.south != SideType.NONE) rotation = Rotation.COUNTERCLOCKWISE_90; 	
		else rotation = Rotation.NONE; 	

		DungeonBuilder.placeStructure(HALL_3WAY, region, pos, rotation);
	}

	private void build4Way(Cell cell, WorldGenRegion region, BlockPos pos) {
		DungeonBuilder.placeStructure(HALL_4WAY, region, pos, Rotation.NONE);
	}

}
