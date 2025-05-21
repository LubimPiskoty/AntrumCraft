package org.piskotky.antrumcraft.item;

import org.piskotky.antrumcraft.AntrumMod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AntrumMod.MODID);

	public static void register(IEventBus eventBus){
		ITEMS.register(eventBus);
	}
} 
