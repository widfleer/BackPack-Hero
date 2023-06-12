package bph.room;

import java.util.ArrayList;
import bph.data.SimpleGameData;
import bph.map.Floor;
import bph.map.Roomable;

public record DoorRoom(int i, int j, ArrayList<Roomable> content) implements Room {

	public DoorRoom(int i, int j) {
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
		return 6;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		return false;
	}

	@Override
	public void prepareRoom(SimpleGameData data) {
		SimpleGameData.getDungeon().add(new Floor());
		if (data.getHero().floor() == 2) {
			data.setEndGame(true);
			data.setInGame(false);
			return;
		}
		if (data.getHero().floor() < 2) {
			data.getHero().changeRoom(SimpleGameData.getDungeon().get(data.getHero().floor() + 1).cellstart().i(),
					SimpleGameData.getDungeon().get(data.getHero().floor() + 1).cellstart().j());
		}
		data.getHero().floorUp();
		data.resetActualRoom();
	}
}
