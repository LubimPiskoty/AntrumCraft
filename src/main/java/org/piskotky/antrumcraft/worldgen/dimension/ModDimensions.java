package org.piskotky.antrumcraft.worldgen.dimension;

import java.util.OptionalLong;

import org.piskotky.antrumcraft.AntrumMod;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class ModDimensions {
	public static final ResourceKey<LevelStem> DUNGEON_DIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
			ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, "dungeon_dim"));
	public static final ResourceKey<Level> DUNGEON_DIM_LEVEL_TYPE = ResourceKey.create(Registries.DIMENSION,
			ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, "dungeon_dim"));
	public static final ResourceKey<DimensionType> DUNGEON_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, 
			ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, "dungeon_dim_type"));
		

	public static void bootstrapType(BootstrapContext<DimensionType> context){
		context.register(DUNGEON_DIM_TYPE, new DimensionType(
					OptionalLong.of(1),
					false,
					true,
					false,
					false,
					0.01,
					false,
					false,
					0,
					64, 
					64, 
					BlockTags.INFINIBURN_OVERWORLD,
					BuiltinDimensionTypes.OVERWORLD_EFFECTS,
					1.0f,
					new DimensionType.MonsterSettings(
						false,
						false,
						ConstantInt.of(0),
						0)));
	}

	public static void bootstrapStem(BootstrapContext<LevelStem> context) {

		HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

		NoiseBasedChunkGenerator wrappedBasedChunkGenerator = new NoiseBasedChunkGenerator(
				new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.DEEP_DARK)), 
				noiseGenSettings.getOrThrow(NoiseGeneratorSettings.CAVES));

		TestChunkGenerator customChunkGenerator = new TestChunkGenerator(
				new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.THE_VOID))
				);
		//LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.DUNGEON_DIM_TYPE), wrappedBasedChunkGenerator);
		LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.DUNGEON_DIM_TYPE), customChunkGenerator);

		context.register(ModDimensions.DUNGEON_DIM_KEY, stem);
	}
}
