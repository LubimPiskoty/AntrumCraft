package org.piskotky.antrumcraft;

import org.piskotky.antrumcraft.dungeon.DungeonGenerator;

import net.minecraft.util.RandomSource;

public class DebugDungeonGen {
    public static void main(String[] args) {
        DungeonGenerator gen = new DungeonGenerator(1, RandomSource.create(1234));
        gen.exportToJson("debug_dungeon.json");
        System.out.println("Dungeon exported to debug_dungeon.json");
    }
}
