package org.piskotky.antrumcraft.dungeon.builders;

import java.util.List;

import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

public class SideBuilder implements CellBuilder {
	private static String WALL = "wall_n";
	private static String DOOR = "wall_door_n";
	
	@Override
	public void build(Cell cell, ServerLevel level, BlockPos pos) {
		// build all sides into a list in order:
		// [north, east, south, west]
		List<SideType> sides = List.of(cell.north, cell.east, cell.south, cell.west);
		List<Rotation> rotations = List.of(Rotation.NONE, Rotation.CLOCKWISE_90, Rotation.CLOCKWISE_180, Rotation.COUNTERCLOCKWISE_90);

		for (int i = 0; i < 4; i++) {
			switch (sides.get(i)) {
				case SideType.WALL:
					DungeonBuilder.placeStructure(WALL, level, pos, rotations.get(i));
					break;

				case SideType.DOOR:
					DungeonBuilder.placeStructure(DOOR, level, pos, rotations.get(i));
					break;

				case SideType.NONE:
					// Dont add anything
					break;

			}
		}

	}

	
}
