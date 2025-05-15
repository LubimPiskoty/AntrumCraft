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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

		String structName = cell.type == CellType.HALL ? "hall_ew" : "room";
		StructureTemplate struct = loadStructure(region, structName);
		StructurePlaceSettings settings = new StructurePlaceSettings();
		
		//TODO: Add the edge spawning too

		BlockPos size = new BlockPos(struct.getSize());
		settings
			.setRotation(Rotation.CLOCKWISE_90)
			.setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2))
			.setMirror(Mirror.NONE)
			.setIgnoreEntities(true);
	
		System.out.println("The size of loaded structure is: " + struct.getSize());	
		boolean result = struct.placeInWorld(
				region, 
				pos, 
				new BlockPos(16, 256, 16), 
				settings,	
				region.getRandom(), 
				2);
		System.out.println("Placing of struct in: [" + pos + "]has result of: " + result);
	}

	StructureTemplate loadStructure(WorldGenRegion region, String name) {
		StructureTemplateManager manager = region.getServer().getStructureManager();
		return manager.getOrCreate(ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
	}

}
