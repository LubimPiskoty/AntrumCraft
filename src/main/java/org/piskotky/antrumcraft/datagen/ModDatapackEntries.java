package org.piskotky.antrumcraft.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.worldgen.ModBiomeModifier;
import org.piskotky.antrumcraft.worldgen.ModConfiguredFeatures;
import org.piskotky.antrumcraft.worldgen.ModPlacedFeatures;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDatapackEntries extends DatapackBuiltinEntriesProvider{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
		.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
		.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
		.add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifier::bootstrap);

	public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries){
		super(output, registries, BUILDER, Set.of(AntrumMod.MODID));

	}
}
