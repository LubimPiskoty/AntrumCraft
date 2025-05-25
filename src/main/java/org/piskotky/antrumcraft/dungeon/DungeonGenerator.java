package org.piskotky.antrumcraft.dungeon;

import java.io.FileWriter;
import java.io.IOException;

import org.piskotky.antrumcraft.dungeon.Cell.SideType;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public class DungeonGenerator {
	
	// List of all floors 
	public Floor[] floors;

	public int gridSize;

	public RandomSource random;


	public DungeonGenerator(int floorCount, RandomSource random){
		this(floorCount, random, false);
	}

	public static DungeonGenerator createDebugDungeon(int floorCount, RandomSource random) {
		return new DungeonGenerator(floorCount, random, true);
	}

	private DungeonGenerator(int floorCount, RandomSource random, boolean isDebug) {
		this.random = random;
		this.gridSize = 16; 

		floors = new Floor[floorCount];
		for(int i = 0; i < floorCount; i++){
			floors[i] = isDebug ? new Floor() : new Floor(10);
		}
	}

	// Data structure to create and manage floors
	public class Floor {
		// Floor consist of grid of cells
		private Cell[][] grid;
		private BlockPos endRoomPos;
		private BlockPos startRoomPos;

		public BlockPos getStartRoomPos() {
			return startRoomPos;
		}

		public BlockPos getEndRoomPos() {
			return endRoomPos;
		}

		public Floor(int roomCount){
			grid = new Cell[gridSize][gridSize];
			// Init the grid to empty state
			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++)
					grid[i][j] = null;	
			}
			generateFloor(roomCount);
		}

		public Cell getCell(int x, int y) {
			//TODOO: Possible bug with offsets
			if (x >= grid.length || x < 0) return null;
			if (y >= grid.length || y < 0) return null;
			return grid[x][y];
		}
		
		private void generateFloor(int roomCount){
			// Generate the start and the end
			// Make a symbolic path from start to end and then generate rooms along the path which will later get all connected together
			//
			// TODO: Make the room and hall generation
			grid[0][0] = Cell.makeStart(SideType.WALL, SideType.WALL, SideType.DOOR, SideType.WALL);
			grid[2][2] = Cell.makeEnd(SideType.WALL,SideType.WALL, SideType.WALL, SideType.DOOR);
			grid[0][2] = Cell.makeRoom(SideType.DOOR, SideType.DOOR, SideType.NONE, SideType.WALL);
			grid[0][3] = Cell.makeRoom(SideType.NONE, SideType.WALL, SideType.NONE, SideType.WALL);
			grid[0][4] = Cell.makeRoom(SideType.NONE, SideType.DOOR, SideType.WALL, SideType.WALL);
			// Manually connect the rooms
			grid[0][1] = Cell.makeHall(SideType.NONE, SideType.WALL, SideType.NONE, SideType.WALL);
			grid[1][2] = Cell.makeHall(SideType.WALL, SideType.NONE, SideType.WALL, SideType.NONE);

		}

		/* If no arguments are passed a debug floor is generated */
		public Floor(){
			gridSize = 8;
			grid = new Cell[gridSize][gridSize];
			// Init the grid to empty state
			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++)
					grid[i][j] = null;	
			}

			grid[2][0] = Cell.makeStart(SideType.WALL, SideType.WALL, SideType.DOOR, SideType.WALL);
			startRoomPos = new BlockPos(2, 0 ,0);
			grid[2][1] = Cell.makeHall(SideType.NONE, SideType.WALL, SideType.NONE, SideType.WALL); 
			grid[2][2] = Cell.makeHall(SideType.NONE, SideType.NONE, SideType.NONE, SideType.NONE);

			// Way to the end
			grid[1][2] = Cell.makeHall(SideType.WALL, SideType.NONE, SideType.WALL, SideType.NONE); 
			grid[0][2] = Cell.makeHall(SideType.WALL, SideType.NONE, SideType.NONE, SideType.WALL);
			grid[0][3] = Cell.makeEnd(SideType.DOOR, SideType.WALL, SideType.WALL, SideType.WALL);
			endRoomPos = new BlockPos(0, 0, 3);

			// Way to Big room
			grid[2][3] = Cell.makeHall(SideType.NONE, SideType.NONE, SideType.NONE, SideType.WALL);
			grid[2][4] = Cell.makeRoom(SideType.DOOR, SideType.NONE, SideType.NONE, SideType.NONE);
			grid[2][5] = Cell.makeRoom(SideType.NONE, SideType.NONE, SideType.NONE, SideType.NONE);
			grid[2][6] = Cell.makeRoom(SideType.WALL, SideType.NONE, SideType.WALL, SideType.NONE);
			grid[1][4] = Cell.makeRoom(SideType.WALL, SideType.NONE, SideType.NONE, SideType.WALL);
			grid[1][5] = Cell.makeRoom(SideType.NONE, SideType.NONE, SideType.NONE, SideType.WALL);
			grid[1][6] = Cell.makeRoom(SideType.NONE, SideType.NONE, SideType.WALL, SideType.WALL);
			grid[3][4] = Cell.makeRoom(SideType.WALL, SideType.WALL, SideType.NONE, SideType.WALL);
			grid[3][5] = Cell.makeRoom(SideType.NONE, SideType.WALL, SideType.NONE, SideType.WALL);
			grid[3][6] = Cell.makeRoom(SideType.NONE, SideType.WALL, SideType.WALL, SideType.NONE);

			// Way to the corner room
			grid[3][2] = Cell.makeHall(SideType.WALL, SideType.NONE, SideType.NONE, SideType.NONE);
			grid[3][3] = Cell.makeHall(SideType.NONE, SideType.NONE, SideType.WALL, SideType.NONE);
			grid[4][3] = Cell.makeHall(SideType.NONE, SideType.WALL, SideType.NONE, SideType.NONE);
			grid[4][2] = Cell.makeRoom(SideType.WALL, SideType.WALL, SideType.DOOR, SideType.DOOR);

			// Way to the ?hall room?
			grid[4][4] = Cell.makeHall(SideType.NONE, SideType.NONE, SideType.NONE, SideType.WALL);
			grid[4][5] = Cell.makeHall(SideType.NONE, SideType.WALL, SideType.NONE, SideType.WALL);
			grid[4][6] = Cell.makeHall(SideType.NONE, SideType.NONE, SideType.WALL, SideType.WALL);

			grid[5][4] = Cell.makeHall(SideType.WALL, SideType.WALL, SideType.NONE, SideType.NONE);
			grid[5][5] = Cell.makeRoom(SideType.DOOR, SideType.WALL, SideType.DOOR, SideType.WALL);
			grid[5][6] = Cell.makeHall(SideType.NONE, SideType.WALL, SideType.WALL, SideType.NONE);

		}

	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"grid\": [\n");
		for (int y = 0; y < gridSize; y++) {
			sb.append("      [");
			for (int x = 0; x < gridSize; x++) {
				Cell cell = grid[x][y];
				if (cell == null) {
					sb.append("null");
				} else {
					sb.append(cell.toJson());
				}
				if (x < gridSize - 1) sb.append(", ");
			}
			sb.append("]");
			if (y < gridSize - 1) sb.append(",");
			sb.append("\n");
		}
		sb.append("    ] }");
		return sb.toString();
		}
	}

	public void exportToJson(String path) {
		try (FileWriter writer = new FileWriter(path)) {
			writer.write("{\n  \"floors\": [\n");
			for (int i = 0; i < floors.length; i++) {
				writer.write("    " + floors[i].toJson());
				if (i < floors.length - 1) writer.write(",");
				writer.write("\n");
			}
			writer.write("  ]\n}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
