package org.piskotky.antrumcraft.block.custom;

import java.util.EnumSet;

import org.piskotky.antrumcraft.worldgen.dimension.ModDimensions;

import net.minecraft.client.gui.font.providers.UnihexProvider.Dimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.Relative;

public class ModPortalBlock extends Block {

	public ModPortalBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		// Some check to see wheter the player can change dim
		handleDungeonPortal(player, pos);
		return InteractionResult.SUCCESS;

	}

	private void handleDungeonPortal(Player player, BlockPos pos) {
		if (player.level() instanceof ServerLevel serverLevel){
			MinecraftServer minecraftServer = serverLevel.getServer();
			ResourceKey<Level> resourceKey = player.level().dimension() == ModDimensions.DUNGEON_DIM_LEVEL_TYPE ?
				Level.OVERWORLD : ModDimensions.DUNGEON_DIM_LEVEL_TYPE;

			ServerLevel portalDimension = minecraftServer.getLevel(resourceKey);
			if (portalDimension != null && !player.isPassenger()){
				player.teleportTo(portalDimension, 0, 20, 0, EnumSet.noneOf(Relative.class), player.getYRot(), player.getXRot(), true);		
			}
				
		}
	}

	
}
