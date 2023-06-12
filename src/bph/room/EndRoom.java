package bph.room;

import java.util.ArrayList;
import bph.data.SimpleGameData;
import bph.map.Roomable;

public record EndRoom(int i, int j, ArrayList<Roomable> content)  implements Room {

	public EndRoom(int i, int j){
		this(i, j, new ArrayList<>());
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
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		return false;		
	}
	
}