package org.piskotky.antrumcraft.worldgen;

import java.util.List;

import org.piskotky.antrumcraft.AntrumMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ModConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> TNT_ORE_KEY = registerKey("tnt_ore");
		// CF -> PF -> BM

	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		RuleTest tntReplacables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

		List<OreConfiguration.TargetBlockState> overworldTntOres = List.of(
				OreConfiguration.target(tntReplacables, net.minecraft.world.level.block.Blocks.TNT.defaultBlockState()));

		register(context,
				TNT_ORE_KEY,
				Feature.ORE,
				new OreConfiguration(overworldTntOres, 32));
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
	}
	
	public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
			BootstrapContext<ConfiguredFeature<?, ?>> context,
			ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configurations){

		context.register(key, new ConfiguredFeature<>(feature, configurations));
	}

}
