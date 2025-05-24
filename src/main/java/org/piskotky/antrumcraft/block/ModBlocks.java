package org.piskotky.antrumcraft.block;

import java.util.function.Function;
import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.block.custom.PortalBlock;
import org.piskotky.antrumcraft.item.ModItems;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AntrumMod.MODID);

	public static final DeferredBlock<Block> DUNGEON_PORTAL_BLOCK = registerBlock("dungeon_portal", 
			properties -> new PortalBlock(properties),
			BlockBehaviour.Properties.of()
				.strength(4.0f)
			);

	private static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> blockFactory, BlockBehaviour.Properties blockProperties){
		DeferredBlock<B> toReturn = BLOCKS.registerBlock(name, blockFactory, blockProperties);
		ModItems.ITEMS.registerSimpleBlockItem(toReturn);
		return toReturn;
	}

	public static void register(IEventBus eventBus){
		BLOCKS.register(eventBus);
	}
}
