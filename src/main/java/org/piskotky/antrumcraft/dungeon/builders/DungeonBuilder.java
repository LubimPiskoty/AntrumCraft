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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class DungeonBuilder {
	public static final int FLOOR_HEIGHT = 16;
	public static void build(DungeonGenerator generator, BlockPos startPos, ServerLevel level, PortalBlockEntity portalBE) {

		System.out.println("The start room pos is: [" + generator.floors[0].getStartRoomPos().multiply(16) + "]");	
		BlockPos spawnPos = new ChunkPos(startPos.offset(generator.floors[0].getStartRoomPos().multiply(16))).getMiddleBlockPosition(startPos.getY()+1);
		portalBE.setDestination(spawnPos);


		BlockPos floorOffset = startPos.offset(generator.floors[0].getStartRoomPos().multiply(-16));
		for (Floor floor : generator.floors){
			buildFloor(floor, generator.gridSize, floorOffset, level, portalBE);
			floorOffset = floorOffset.offset(floor.getEndRoomPos().subtract(floor.getStartRoomPos()).multiply(16)).below(FLOOR_HEIGHT);
		}
	}

	private static void buildFloor(Floor floor, int gridSize, BlockPos startPos, ServerLevel level, PortalBlockEntity portalBE) {
		for (int i = 0; i < gridSize * gridSize; i++){
			int x = i / gridSize;
			int y = i % gridSize;

			Cell cell = floor.getCell(x, y);
			if (cell == null)
				continue;

			BlockPos pos = startPos.offset(floor.getStartRoomPos().multiply(16)).offset(x*16, 0, y*16);
			// Generate the cells
			switch (cell.type) {
				case CellType.START:
				case CellType.END:
					//TODO: Handle the staircase
				case CellType.ROOM:
					RoomBuilder.build(cell, level, pos);
					SideBuilder.build(cell, level, pos);
					break;

				case CellType.HALL:
					HallBuilder.build(cell, level, pos);
					break;

				default:
					continue;	
			}
	
		}
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

		// System.out.println("The size of loaded structure is: " + structure.getSize());

		boolean result = structure.placeInWorld(
				level, 
				rotated_pos, 
				new BlockPos(16, 256, 16), 
				settings,	
				level.getRandom(), 
				2);
		System.out.println("Placing struct: [" + pos + "]");
	}
}
