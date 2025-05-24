package org.piskotky.antrumcraft.dungeon.builders;


import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.block.entity.PortalBlockEntity;
import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.CellType;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator.Floor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class DungeonBuilder {

	public static PortalBlockEntity build(DungeonGenerator generator, BlockPos startPos, ServerLevel level) {
		System.out.println("STARTING DUNGEON BUILDER");
		//TODO: Build all floors
		Floor floor = generator.floors[0];

		// Build floor
		for (int i = 0; i < generator.gridSize * generator.gridSize; i++){
			int x = i / generator.gridSize;
			int y = i % generator.gridSize;

			Cell cell = floor.getCell(x, y);
			if (cell == null)
				continue;


			BlockPos pos = startPos.offset(x*16, 0, y*16);
			System.out.println("Building floor: " + i + " at: [" + pos + "]");
			// Generate the cells
			switch (cell.type) {
				case CellType.ROOM:
				case CellType.START:
				case CellType.END:
					new RoomBuilder().build(cell, level, pos);
					new SideBuilder().build(cell, level, pos);
					break;

				case CellType.HALL:
					new HallBuilder().build(cell, level, pos);
					break;

				default:
					continue;	
			}
		}

		//TODO: return the starting room portal block or at least start position
		return null;
	}

	public static StructureTemplate loadStructure(ServerLevel level, String name) {
		StructureTemplateManager manager = level.getServer().getStructureManager();
		StructureTemplate template = manager.getOrCreate(ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
		if (template.getSize() == Vec3i.ZERO) 
			System.out.println("ERROR: Unable to load structure: " + AntrumMod.MODID + ":" + name);
		return template;
	}
	

	//Mirror doesnt work
	public static void placeStructure(String structName, ServerLevel level, BlockPos pos, Rotation rotation){
		placeStructure(loadStructure(level, structName), level, pos, Mirror.NONE, rotation);	
	}

	private static void placeStructure(StructureTemplate structure, ServerLevel level, BlockPos pos, 
										Mirror mirror, Rotation rotation){
		Vec3i size = structure.getSize();
		StructurePlaceSettings settings = new StructurePlaceSettings()
			.setMirror(mirror)
			.setRotation(rotation)
			.setRotationPivot(new BlockPos(-1, 0, -1));

		BlockPos rotated_pos = pos;
		switch (rotation) {
			case Rotation.NONE:
				rotated_pos = pos;	
				break;
				
			case Rotation.CLOCKWISE_90:
				rotated_pos = new BlockPos(pos.getX() + size.getX() + 1, pos.getY(), pos.getZ());	
				break;

			case Rotation.CLOCKWISE_180:
				rotated_pos = new BlockPos(pos.getX() + size.getX() + 1, pos.getY(), pos.getZ() + size.getZ()+ 1);	
				break;

			case Rotation.COUNTERCLOCKWISE_90:
				rotated_pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + size.getZ() + 1);	
				break;
		}

		System.out.println("The size of loaded structure is: " + structure.getSize());

		boolean result = structure.placeInWorld(
				level, 
				rotated_pos, 
				new BlockPos(16, 256, 16), 
				settings,	
				level.getRandom(), 
				2);
		System.out.println("Placing of struct in: [" + pos + "]has result of: " + result);
	}
}
