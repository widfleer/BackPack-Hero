package bph.room;

import java.util.ArrayList;
import bph.data.SimpleGameData;
import bph.map.Roomable;

public record BasicRoom(int i, int j, ArrayList<Roomable> content) implements Room {

	public BasicRoom(int i, int j){
		this(i, j, null);
	}

	@Override
	public void clearRoom() {
		content.clear();		
	}

	
	@Override
	public int getI() {
		return i;
	}

	@Override
	public int getJ() {
		return j;
	}

	@Override
	public int typeRoom() {
		return 1;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		return false;		
	}

}
