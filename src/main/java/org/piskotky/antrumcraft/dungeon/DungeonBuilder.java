package org.piskotky.antrumcraft.dungeon;


import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.dungeon.Cell.CellType;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator.Floor;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
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

		CellBuilder.buildCell(cell, region, pos);

	
	}

	public static StructureTemplate loadStructure(WorldGenRegion region, String name) {
		StructureTemplateManager manager = region.getServer().getStructureManager();
		return manager.getOrCreate(ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
	}

}
