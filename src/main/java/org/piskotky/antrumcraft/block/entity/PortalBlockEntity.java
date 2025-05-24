package org.piskotky.antrumcraft.block.entity;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortalBlockEntity extends BlockEntity {
	private UUID portalID;
	private BlockPos otherPortalPos;
	//TODO: Implement the portal linking logic

	public PortalBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.PORTAL_BE.get(), pos, blockState);
		portalID = UUID.randomUUID();
		otherPortalPos = null;

		// Find a way to link the portals
		// Lets say that there is a predetermied grid of dungeon starts and we will try to loop over all posible positions
		// until one is free and teleport the player there and also link the portals
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putUUID("portalID", portalID);
		
		if (otherPortalPos != null)
			tag.putLong("otherPortalPos", otherPortalPos.asLong());
	}	

	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		portalID = tag.getUUID("portalID");
		
		if (tag.contains("otherPortalPos"))
			otherPortalPos = BlockPos.of(tag.getLong("otherPortalPos"));
	}

	public BlockPos getDestination() {
		return otherPortalPos;
	}

	public void setDestination(BlockPos destination) {
		otherPortalPos = destination;
		setChanged();
	}

	public void linkPortals(PortalBlockEntity other) {
		this.setDestination(other.getBlockPos());
		other.setDestination(worldPosition);

		System.out.println("Portals have been linked: " + worldPosition + " <-> " + other.getBlockPos());
	}
}
