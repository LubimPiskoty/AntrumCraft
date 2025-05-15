package org.piskotky.antrumcraft.dungeon.builders;


import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.Cell.CellType;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator.Floor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class DungeonBuilder {
	private DungeonGenerator generator;

	public DungeonBuilder(DungeonGenerator generator) {
		this.generator = generator;
	}

	public void buildDungeon(WorldGenRegion region, BlockPos pos) {
		if (region.getServer() == null) System.out.println("ERROR: The server is null");
	
			
		Floor floor = generator.floors[0];
		Cell cell = floor.getCell(pos.getX()/generator.gridSize, pos.getZ()/generator.gridSize);
		//Cell cell = floor.getCell(pos.getX()%generator.gridSize, pos.getZ()%generator.gridSize);
		if (cell == null)
			return;

		// Generate the cells
		switch (cell.type) {
			case CellType.ROOM:
			case CellType.START:
			case CellType.END:
				new RoomBuilder().build(cell, region, pos);
				new SideBuilder().build(cell, region, pos);
				break;
			
			case CellType.HALL:
				new HallBuilder().build(cell, region, pos);
				break;

			default:
				return;	
		}

	}

	public static StructureTemplate loadStructure(WorldGenRegion region, String name) {
		StructureTemplateManager manager = region.getServer().getStructureManager();
		StructureTemplate template = manager.getOrCreate(ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
		if (template.getSize() == Vec3i.ZERO) 
			System.out.println("ERROR: Unable to load structure: " + AntrumMod.MODID + ":" + name);
		return template;
	}
	

	//Mirror doesnt work
	public static void placeStructure(String structName, WorldGenRegion region, BlockPos pos, Rotation rotation){
		placeStructure(loadStructure(region, structName), region, pos, Mirror.NONE, rotation);	
	}

	private static void placeStructure(StructureTemplate structure, WorldGenRegion region, BlockPos pos, 
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
				region, 
				rotated_pos, 
				new BlockPos(16, 256, 16), 
				settings,	
				region.getRandom(), 
				2);
		System.out.println("Placing of struct in: [" + pos + "]has result of: " + result);
	}
}
