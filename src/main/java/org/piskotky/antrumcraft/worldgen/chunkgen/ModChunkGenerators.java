package org.piskotky.antrumcraft.worldgen.chunkgen;

import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.worldgen.dimension.TestChunkGenerator;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModChunkGenerators {
	// Chunk Generators
	public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS =
        DeferredRegister.create(Registries.CHUNK_GENERATOR, AntrumMod.MODID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<? extends ChunkGenerator>> TEST_CHUNK_GENERATOR =
        CHUNK_GENERATORS.register("test_chunk_generator", () -> TestChunkGenerator.CODEC);

	public static void register(IEventBus bus) {
        CHUNK_GENERATORS.register(bus);
    }

}
