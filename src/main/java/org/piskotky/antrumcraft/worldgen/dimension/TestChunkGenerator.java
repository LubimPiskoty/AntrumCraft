package org.piskotky.antrumcraft.worldgen.dimension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.piskotky.antrumcraft.AntrumMod;
import org.piskotky.antrumcraft.dungeon.Cell;
import org.piskotky.antrumcraft.dungeon.DungeonGenerator;
import org.piskotky.antrumcraft.dungeon.builders.DungeonBuilder;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

public class TestChunkGenerator extends ChunkGenerator {
	
	private DungeonGenerator generator;
	private DungeonBuilder builder;

	public static final MapCodec<TestChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource)
		).apply(instance, TestChunkGenerator::new));

	public static final ResourceLocation RANDOM_STREAM = ResourceLocation.fromNamespaceAndPath(AntrumMod.MODID, "chunk_random");

	public TestChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource);

		this.generator = new DungeonGenerator(4, RandomSource.create(1234));
		this.builder = new DungeonBuilder(generator);
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager,
		StructureManager structureManager, ChunkAccess chunk) {
		// TODO Auto-generated method stub
	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random,
			ChunkAccess chunk) {
		// TODO Auto-generated method stub

		BlockPos pos = chunk.getPos().getWorldPosition().offset(0, 20, 0);
		builder.buildDungeon(level, pos);
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion level) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getGenDepth() {
		return 256;
	}

	@SuppressWarnings("null")
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(
		Blender blender,
		RandomState randomState,
		StructureManager structureManager,
		ChunkAccess chunk
	) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getBaseHeight(int x, int z, Types type, LevelHeightAccessor level, RandomState random) {
		return 64;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
		int x = pos.getX()/generator.gridSize;
		int y = pos.getZ()/generator.gridSize;
		info.add("Cell[x: " + x + ", y: " + y + "]");
		if (generator.floors.length < 1)
			return;
		Cell cell = generator.floors[0].getCell(x, y);
		if (cell == null)
			return;
		info.add("Cell type:  " + cell.type);
		info.add("North side:" + cell.north);
		info.add("East side: " + cell.east);
		info.add("South side:" + cell.south);
		info.add("West side: " + cell.west);
	}
	
	@Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
		// Disable the ore gen etc
	}
}
