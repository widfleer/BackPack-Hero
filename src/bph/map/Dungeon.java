package bph.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public record Dungeon(ArrayList<Floor> floors) {

	public Dungeon(ArrayList<Floor> floors) {
		Objects.requireNonNull(floors);
		this.floors = floors;
	}

	public Dungeon() {
		this(new ArrayList<Floor>(Arrays.asList(new Floor())));
	}

	public void add(Floor floor) {
		Objects.requireNonNull(floors);
		floors.add(floor);
	}

	public Floor get(int i) {
		return floors.get(i);
	}

	public int size() {
		return floors.size();
	}
}
