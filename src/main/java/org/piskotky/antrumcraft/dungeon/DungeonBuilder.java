package org.piskotky.antrumcraft.dungeon;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.dungeon.Cell.CellType;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator.Floor;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkAccess;
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
		if (cell == null)
			return;

		String structName = cell.type == CellType.HALL ? "hall" : "room";
		StructureTemplate struct = loadStructure(region, structName);

		System.out.println("The size of loaded structure is: " + struct.getSize());	
		boolean result = struct.placeInWorld(
				region, 
				pos, 
				BlockPos.ZERO, 
				new StructurePlaceSettings(),	
				region.getRandom(), 
				2);
		System.out.println("Placing of struct has result of: " + result);
		//room.placeInWorld(
		//	region, // ✅ WorldGenRegion allows actual placement
		//	pos,    // origin of structure placement
		//	BlockPos.ZERO,
		//	settings,
		//	region.getRandom(), // random source for jigsaws, processors, etc.
		//	Block.UPDATE_ALL // flags: 2 = set blocks without notifying neighbors
		//);
	}

	StructureTemplate loadStructure(WorldGenRegion region, String name) {
		StructureTemplateManager manager = region.getServer().getStructureManager();
		return manager.getOrCreate(ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
	}

}
