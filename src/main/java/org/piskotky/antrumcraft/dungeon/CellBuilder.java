package org.piskotky.antrumcraft.dungeon;

import org.piskotky.antrumcraft.dungeon.Cell.CellType;
import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class CellBuilder {

	public static void buildCell(Cell cell, WorldGenRegion region, BlockPos pos){
		//TODO: make this into some kind of design to handle differend kinds of strucutres
		
		// Spawn the cell
		String name;
		Rotation rotation = Rotation.NONE;
		switch (cell.type) {
			case CellType.HALL:
				name = "hall_ew";
				rotation = (cell.east == SideType.NONE || cell.east == SideType.DOOR) ? Rotation.NONE : Rotation.CLOCKWISE_90;
				break;

			case CellType.ROOM:
			case CellType.START:
			case CellType.END:
				name = "room";
				break;

			default:
				System.out.println("ERROR: Type not handled!");
				return;
		}

		StructureTemplate struct = DungeonBuilder.loadStructure(region, name);
		placeStructure(struct, region, pos, rotation);

		// Spawn the sides
		placeStructure(DungeonBuilder.loadStructure(region, getNameFromSideType(cell.west)), region, pos, Rotation.NONE);
		placeStructure(DungeonBuilder.loadStructure(region, getNameFromSideType(cell.north)), region, pos, Rotation.CLOCKWISE_90);
		placeStructure(DungeonBuilder.loadStructure(region, getNameFromSideType(cell.east)), region, pos, Rotation.CLOCKWISE_180);
		placeStructure(DungeonBuilder.loadStructure(region, getNameFromSideType(cell.south)), region, pos, Rotation.COUNTERCLOCKWISE_90);

	}

	public static String getNameFromSideType(SideType sideType){
		switch (sideType) {
			case SideType.WALL:
				return "wall_w";
			case SideType.NONE:
				return "";
			case SideType.DOOR:
				return "door_w";
			default:
				return "";

		}
	}

	public static void placeStructure(StructureTemplate structure, WorldGenRegion region, BlockPos pos, Rotation rotation){
		Vec3i size = structure.getSize();
		StructurePlaceSettings settings = new StructurePlaceSettings()
			.setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2))
			.setRotation(rotation);

		System.out.println("The size of loaded structure is: " + structure.getSize());	
		boolean result = structure.placeInWorld(
				region, 
				pos, 
				new BlockPos(16, 256, 16), 
				settings,	
				region.getRandom(), 
				2);
		System.out.println("Placing of struct in: [" + pos + "]has result of: " + result);
	}

}
