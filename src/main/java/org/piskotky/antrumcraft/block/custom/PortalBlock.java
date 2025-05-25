package org.piskotky.antrumcraft.block.custom;

import java.util.EnumSet;
import javax.annotation.Nullable;

import org.piskotky.antrumcraft.block.ModBlocks;
import org.piskotky.antrumcraft.block.entity.PortalBlockEntity;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator;
import org.piskotky.antrumcraft.dungeon.builders.DungeonBuilder;
import org.piskotky.antrumcraft.worldgen.dimension.ModDimensions;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.Relative;

public class PortalBlock extends BaseEntityBlock {
	public static final MapCodec<PortalBlock> CODEC = PortalBlock.simpleCodec(PortalBlock::new);

	private static final int DUNGEON_SIZE = 250*16; // Chunks

	public PortalBlock(Properties pProperties) {
		super(pProperties);
	}


	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	/* Block entity stuff */
	
	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PortalBlockEntity(pos, state);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide)
			return InteractionResult.SUCCESS;

		MinecraftServer server = level.getServer();
		if (level.getBlockEntity(pos) instanceof PortalBlockEntity portalBE && server != null) {
			BlockPos destinationPos = portalBE.getDestination();
			// Teleport back to overworld
			if (level.dimension() == ModDimensions.DUNGEON_DIM_LEVEL_TYPE){
				handleReturnFromDungeon(player, portalBE, server);
				return InteractionResult.SUCCESS;
			}

			handleTeleportingToDungeon(destinationPos, portalBE, player, server);
		}

		return InteractionResult.SUCCESS;

	}

	private void handleReturnFromDungeon(Player player, PortalBlockEntity portalBE, MinecraftServer server){
		BlockPos destinationPos = portalBE.getDestination(); // Assumes that the portal was creating with a link
		if (destinationPos == null)
			destinationPos = new BlockPos(0, 256, 0); // Fallback to idk where just to prevent crashing

		teleportTo(player, destinationPos, server.getLevel(Level.OVERWORLD));
	}


	private void handleTeleportingToDungeon(BlockPos destinationPos, PortalBlockEntity portalBE, Player player, MinecraftServer server) {
		ServerLevel dungeonDim = server.getLevel(ModDimensions.DUNGEON_DIM_LEVEL_TYPE);
		// Get the portal block entity and retrive the otherPortalPos
		// If it is not set that means that the portal has not been linked yet
		if (destinationPos == null){
			// Hard code 1k dungeons but it can be much higher tbh
			int i = 0;
			while (true){
				System.out.println("TRYING CANDIDATE POS: " + i);
				BlockPos candidatePos = getAvailablePortalPosition(i, dungeonDim.getMaxY()-32);
				
				
				if (Math.abs(candidatePos.getX()) >= MinecraftServer.ABSOLUTE_MAX_WORLD_SIZE ||
						Math.abs(candidatePos.getZ()) >= MinecraftServer.ABSOLUTE_MAX_WORLD_SIZE)
					break;	// Handle out of bounds position as a full one

				if (!isPortalPresent(server, candidatePos, dungeonDim)){
					System.out.println("FOUND A EMPTY CANDIDATE POS: " + candidatePos);
					generateDungeon(candidatePos, dungeonDim, portalBE);
					destinationPos = portalBE.getDestination();
					break;
				}
				i++;
			}
			// If not found then handle clearing oldest dimension (will need to save the last time used in the blockentity)
			if (destinationPos == null){
				System.out.println("NO VALID CANDIDATE POS");
				return;
			}
		}

		teleportTo(player, destinationPos, dungeonDim);
	}


	private void teleportTo(Player player, BlockPos pos, ServerLevel dimension) {
		int posX, posY, posZ;
		posX = pos.getX();
		posY = pos.getY();
		posZ = pos.getZ();
	
		if (dimension != null && !player.isPassenger()){
			player.teleportTo(dimension, posX, posY, posZ, EnumSet.noneOf(Relative.class), player.getYRot(), player.getXRot(), true);		
		}
	}

	private static BlockPos getAvailablePortalPosition(int index, int height) {
		// Use some kind of hilberts curve to index the whole dimension
		int[] coords = unpairZ2(index);
		return new BlockPos(coords[0] * DUNGEON_SIZE, height, coords[1] * DUNGEON_SIZE);
	}


	/*  The mapping into integer lattice*/

	static int toZ(int n){
		return n % 2 == 0 ? n / 2 : -(n / 2 + 1);
	}

	static int[] unpairZ2(int n) {
		int w = (int)(Math.sqrt(8 * n + 1) - 1) / 2;
		int t = w * (w + 1) / 2;
		int x_n = w - (n - t);
		int y_n = n - t;
		return new int[]{toZ(x_n), toZ(y_n)};
	}


	public boolean isPortalPresent(MinecraftServer server, BlockPos position, ServerLevel dimension){
		ChunkPos chunkPos = new ChunkPos(position);
		// Try to find a block at this specified pos
		// Assumes generateDungeon has placed it there
		return !dimension.getBlockState(chunkPos.getWorldPosition()).isAir();
	}


	private void generateDungeon(BlockPos pos, ServerLevel level, PortalBlockEntity portalBE) {
		DungeonGenerator dungeonLayout;

		//dungeonLayout = new DungeonGenerator(2, RandomSource.create());
		dungeonLayout = DungeonGenerator.createDebugDungeon(6, level.getRandom());
		
		ChunkAccess chunk = level.getChunk(pos);

		// Each Chunk with portal block will have a block on the start of the chunk
		chunk.setBlockState(new ChunkPos(pos).getWorldPosition(), Blocks.BEDROCK.defaultBlockState(), false);
		
		DungeonBuilder.build(dungeonLayout, pos, level, portalBE);

		// Manualy create the portal
		BlockPos chunkCenter = pos.offset(8, 16, 8);
		chunk.setBlockState(chunkCenter, ModBlocks.DUNGEON_PORTAL_BLOCK.get().defaultBlockState(), false);

		// Build platform under the portal block TEMPORARY SOLUTION
		BlockState concreteBS = Blocks.BLACK_CONCRETE.defaultBlockState();
		for (BlockPos foundationPos : 
				BlockPos.betweenClosed(chunkCenter.below().north().east(), chunkCenter.below().south().west())){
			chunk.setBlockState(foundationPos, concreteBS, false);
		}

		((PortalBlockEntity)level.getBlockEntity(chunkCenter)).setDestination(portalBE.getBlockPos().north());
	}
}
