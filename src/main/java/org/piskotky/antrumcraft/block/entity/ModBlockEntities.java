package org.piskotky.antrumcraft.block.entity;

import java.util.Set;
import java.util.function.Supplier;

import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.block.ModBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AntrumMod.MODID);

	public static final Supplier<BlockEntityType<PortalBlockEntity>> PORTAL_BE =
		BLOCK_ENTITIES.register("dungeon_portal_be",  () -> 
			new BlockEntityType<PortalBlockEntity> (
				PortalBlockEntity::new,
				Set.of(ModBlocks.DUNGEON_PORTAL_BLOCK.get())
			)
		);

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
	
}
