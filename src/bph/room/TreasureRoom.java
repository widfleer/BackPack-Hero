package bph.room;

import java.util.ArrayList;
import bph.data.SimpleBackpackData;
import bph.data.SimpleGameData;
import bph.map.Roomable;

public record TreasureRoom(int i, int j, ArrayList<Roomable> content) implements Room  {

	public TreasureRoom(int i, int j){
		this(i, j,new ArrayList<Roomable>(SimpleBackpackData.generatedrop(SimpleGameData.getrarities(),false)));
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
		return 5;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		return false;		
	}

	@Override
	public void prepareRoom(SimpleGameData data) {
		data.openBackpack();
		data.getBackpackdata().addtoTrash(this.content());
		this.clearRoom();
	}
}