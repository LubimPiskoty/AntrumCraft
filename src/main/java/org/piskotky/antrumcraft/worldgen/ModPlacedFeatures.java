package org.piskotky.antrumcraft.worldgen;

import java.util.List;

import org.piskotky.antrumcraft.AntrumMod;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class ModPlacedFeatures {
	public static final ResourceKey<PlacedFeature> TNT_ORE_PLACE_KEY = registerKey("tnt_ore_placed");

	public static void bootstrap(BootstrapContext<PlacedFeature> context){
		var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

		register(context, TNT_ORE_PLACE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TNT_ORE_KEY),
				ModOrePlacement.commonOrePlacement(32, HeightRangePlacement.triangle(VerticalAnchor.BOTTOM, VerticalAnchor.TOP)));
	}

	private static ResourceKey<PlacedFeature> registerKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, name));
	}

	private static void register(BootstrapContext<PlacedFeature> context,
			ResourceKey<PlacedFeature> key,
			Holder<ConfiguredFeature<?, ?>> configuration,
			List<PlacementModifier> modifiers){

		context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
	}
}
