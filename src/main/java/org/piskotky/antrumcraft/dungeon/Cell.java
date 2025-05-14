package org.piskotky.antrumcraft.dungeon;

import java.util.List;

public final class Cell {

	public enum CellType {ROOM, HALL, START, END}
	public enum SideType {WALL, DOOR, NONE}

	public CellType type;

	public SideType north;
	public SideType east;
	public SideType south;
	public SideType west;

	private Cell(CellType type, SideType north, SideType east, SideType south, SideType west){
		this.type = type;
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}

	public static Cell makeRoom(SideType north, SideType east, SideType south, SideType west) {
		return new Cell(CellType.ROOM, north, east, south, west);
	}

	public static Cell makeStart(SideType north, SideType east, SideType south, SideType west) {
		return new Cell(CellType.START, north, east, south, west);
	}
	
	public static Cell makeEnd(SideType north, SideType east, SideType south, SideType west) {
		return new Cell(CellType.END, north, east, south, west);
	}
	
	public static Cell makeHall(SideType north, SideType east, SideType south, SideType west) {
		return new Cell(CellType.HALL, north, east, south, west);
	}

	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"type\":\"").append(type.toString()).append("\",");
		sb.append("\"sides\":[");
		SideType[] sides = new SideType[4];
		sides[0] = north;
		sides[1] = east;
		sides[2] = south;
		sides[3] = west;
		for (int i = 0; i < 4; i++) {
			sb.append("\"").append(sides[i].toString()).append("\"");
			if (i < 3) sb.append(",");
		}
		sb.append("]}");
		return sb.toString();
	}
}
