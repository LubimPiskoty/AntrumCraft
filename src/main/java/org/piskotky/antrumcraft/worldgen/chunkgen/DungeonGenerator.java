package org.piskotky.antrumcraft.worldgen.chunkgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction8;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock.Types;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class DungeonGenerator {	
	public static final BlockState BLOCK = Blocks.STONE_BRICKS.defaultBlockState(); 
	
	// Generate a hall in the current chunk
	public static final void generateHall(ChunkAccess chunk, Direction direction){	
		//TODO: Implement the directions

		// Go over each face and based on the direction generate only some
		int width = 8;
		int height = 4;
		int chunkSize = 16; 
		Vec3i floorShape;
		floorShape = direction == Direction.EAST || direction == Direction.WEST ?
			new Vec3i(width, 1, chunkSize) : new Vec3i(chunkSize, 1, width);
		
		// Generate floor and ceiling
		generateFilledCube(chunk, new Vec3i(0, 0, 0), floorShape);
		generateFilledCube(chunk, new Vec3i(0, height, 0), floorShape);

		// Generate walls

		//generateFilledCube(chunk, new Vec3(0, 0, 0) );
		//generateFilledCube(chunk, new Vec3(0, 0, 0) );

	}

	// TODO add the limits and some exception 
	// Generate a plane inside a chunk (limit the coords to the chunk)
	public static final void generateFilledCube(ChunkAccess chunk, Vec3i pos, Vec3i size){
		BlockPos.MutableBlockPos blockPos = new MutableBlockPos();
		for(int dx = pos.getX(); dx < pos.getX() + size.getX(); dx++){
			for(int dy = pos.getY(); dy < pos.getY() + size.getY(); dy++){
				for(int dz = pos.getZ(); dz < pos.getZ() + size.getZ(); dz++){
					// Place the blocks
					int x = dx+chunk.getPos().getMinBlockX();
					int z = dz+chunk.getPos().getMinBlockZ();
					blockPos.set(x, dy, z);
					placeBlock(chunk, BLOCK, blockPos);

				}
			}
		}	
	}

	public static final void placeBlock(ChunkAccess chunk, BlockState block, BlockPos position) {
		chunk.setBlockState(position, block, false);
		//chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE).update(position.getX(), position.getY(), position.getZ(), block);
	}
}
