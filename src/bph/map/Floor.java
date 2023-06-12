package bph.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

import bph.character.Hero;
import bph.room.BasicRoom;
import bph.room.DoorRoom;
import bph.room.FightRoom;
import bph.room.HealerRoom;
import bph.room.MerchantRoom;
import bph.room.Room;
import bph.room.TreasureRoom;

public class Floor {

	private final Room[][] matrix;
	private Cell cellstart;
	private HashSet<Cell> visited = new HashSet<Cell>();
	ArrayList<Cell> dirs = new ArrayList<Cell>(
			Arrays.asList(new Cell(-1, 0), new Cell(1, 0), new Cell(0, -1), new Cell(0, 1)));

	public Floor() {
		matrix = new Room[5][11];
		generate();
		setRoom(randomFloor());
	}

	private void generate() {
		for (var i = 0; i < matrix.length; i++) {
			for (var j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = null;
			}
		}
	}

	public boolean isAcessible(HashSet<Cell> visitedcells, int roomi, int roomj, int i, int j) {
		Objects.requireNonNull(visitedcells);
		Cell actual = new Cell(roomi, roomj);
		if (roomi == i && roomj == j) {
			return true;
		}
		if (roomi < 0 || roomi >= matrix[0].length || roomj < 0 || roomj >= matrix.length) {
			return false;
		}
		if (getRoom(roomj,roomi)==null ||(getRoom(roomj,roomi).typeRoom()!=1 && !visited.contains(actual)) || visitedcells.contains(actual)) {
			return false;
		}

		visitedcells.add(new Cell(roomi, roomj));
		for (Cell dir : dirs) {
			if (isAcessible(visitedcells, roomi + dir.i(), roomj + dir.j(), i, j)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Room> randomFloor() {
		var pair = generateStartEnd();
		var start = new Cell(pair.get(0).last(), pair.get(0).first());
		var end = new Cell(pair.get(1).last(), pair.get(1).first());
		HashSet<Cell> path;
		do {
			path = generatePath(new HashSet<Cell>(), start.i(), start.j(), end.i(), end.j(), new Random());

		} while (!path.contains(end));
		return generateRoomContent(path, start, end);
	}

	private ArrayList<Room> generateRoomContent(HashSet<Cell> path, Cell start, Cell end) {
		var tuples = new ArrayList<Room>();
		int addroom = 0;
		for (Cell cell : path) {
			if (cell.equals(end)) {
				tuples.add(new DoorRoom(cell.j(), cell.i()));
			} else if (cell.equals(start)) {
				tuples.add(new BasicRoom(cell.j(), cell.i()));
				this.cellstart = start;
			} else {

				if (addroom < 3) {
					tuples.add(new FightRoom(cell.j(), cell.i()));
				} else if (addroom < 4) {
					tuples.add(new MerchantRoom(cell.j(), cell.i()));
				} else if (addroom < 5) {
					tuples.add(new HealerRoom(cell.j(), cell.i()));
				} else if (addroom < 7) {
					tuples.add(new TreasureRoom(cell.j(), cell.i()));
				} else {
					tuples.add(new BasicRoom(cell.j(), cell.i()));
				}
				addroom++;
			}
		}
		return tuples;
	}


	private HashSet<Cell> generatePath(HashSet<Cell> path, int starti, int startj, int endi, int endj, Random rand) {
		Objects.requireNonNull(path);
		Cell actual = new Cell(starti, startj);
		if (starti == endi && startj == endj || path.contains(actual)) {
			return path;
		}
		if (starti < 0 || starti >= matrix[0].length || startj < 0 || startj >= matrix.length) {
			return path;
		}
		path.add(actual);
		if (rig(actual, endi, endj)) {
			path.add(new Cell(endi, endj));
		} else {
			int chance = rand.nextInt(100);
			int numberpath = (chance < 40) ? 2 : (chance < 70) ? 1 : (chance < 90) ? 0 : 3;
			int randompath = -1, choice;
			for (int i = 0; i < numberpath; i++) {
				choice = rand.nextInt(4);
				if (randompath != choice) {
					var dir = dirs.get(choice);
					path = generatePath(path, starti + dir.i(), startj + dir.j(), endi, endj, rand);
					randompath = choice;
				} else {
					i--;
				}
			}
		}
		return path;
	}

	private boolean rig(Cell actual, int endi, int endj) {
		if (actual.i() == endi && (actual.j() - 1 == endj || actual.j() + 1 == endj || actual.j() == endj)) {
			return true;
		} else if (actual.j() == endj && (actual.i() - 1 == endi || actual.i() + 1 == endi || actual.i() == endi)) {
			return true;
		}
		return false;
	}

	private ArrayList<Pair> generateStartEnd() {
		var lststartend = new ArrayList<Pair>();
		var rand = new Random();
		var start = rand.nextInt(2);
		int startcase, endcase;
		startcase = (start == 0) ? rand.nextInt(1) : rand.nextInt(9, 11);
		endcase = (start != 0) ? rand.nextInt(1) : rand.nextInt(9, 11);
		lststartend.add(new Pair(rand.nextInt(5), startcase));
		lststartend.add(new Pair(rand.nextInt(5), endcase));
		return lststartend;
	}

	public void setRoom(ArrayList<Room> content) {
		for (var room : content) {
			matrix[room.getI()][room.getJ()] = room;
		}
	}


	public int lines() {
		return matrix.length;
	}

	public int columns() {
		return matrix[0].length;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			sb.append("[ ");
			for (int j = 0; j < matrix[0].length; j++) {
				sb.append("[");
				sb.append(matrix[i][j]);
				sb.append("] ");
			}
			sb.append("]");
			sb.append("\n");
		}
		return sb.toString();
	}

	public boolean clickOnCell(Hero hero, int i, int j) {
		if (i < 0 || columns() <= i || j < 0 || lines() <= j) {
			return false;
		}
		if (matrix[j][i]!=null && isAcessible(new HashSet<Cell>(), hero.roomi(), hero.roomj(), i, j)) {
			hero.changeRoom(i, j);
			visited.add(new Cell(i, j));
			return true;
		}
		return false;
	}
	
	public int typeRoom(int j, int i) {
		if (matrix[i][j]==null) return 0;
		return matrix[i][j].typeRoom();
	}

	public Cell cellstart() {
		return cellstart;
	}

	public void setcellstart(int i, int j) {
		cellstart = new Cell(i, j);
	}

	public Room getRoom(int i, int j) {
		return matrix[i][j];
	}
}