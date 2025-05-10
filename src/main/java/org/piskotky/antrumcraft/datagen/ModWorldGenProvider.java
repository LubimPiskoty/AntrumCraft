package org.piskotky.antrumcraft.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.worldgen.dimension.ModDimensions;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
	
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
		.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
		.add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem);
		//.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
		//.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
		//.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
		//.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType);


	public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of(AntrumMod.MODID));
	}
	
}
