package org.piskotky.antrumcraft.datagen;

import java.util.concurrent.CompletableFuture;

import org.piskotky.antrumcraft.AntrumMod;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = AntrumMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new ModWorldGenProvider(packOutput, lookupProvider));
	}
	
}
