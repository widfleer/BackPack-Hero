package bph.room;

import java.util.ArrayList;
import java.util.List;

import bph.data.Button;
import bph.data.SimpleGameData;
import bph.map.Roomable;

public record HealerRoom(int i, int j, ArrayList<Roomable> content) implements Room {

	public HealerRoom(int i, int j) {
		this(i, j, new ArrayList<>());
	}

	@Override
	public void clearRoom() {
		content.clear();
	}

	@Override
	public List<Button> getButtons() {
		var buttonlist = new ArrayList<Button>();
		if (!SimpleGameData.printHealer()) {
			return buttonlist;
		}
		float width = SimpleGameData.getWidth();
		float height = SimpleGameData.getHeight();
		var gap = SimpleGameData.getSquareSize() * 1.65;
		buttonlist.add(new Button("(3 Gold) : Remove all curses", (float) (width * 0.385), (float) (width * 0.24),
				(float) (height * 0.33), (float) (SimpleGameData.getSquareSize() * 1.5), null));
		buttonlist.add(new Button("(5 Gold) : Heals 25HP", (float) (width * 0.385), (float) (width * 0.24),
				(float) (height * 0.33 + gap), (float) (SimpleGameData.getSquareSize() * 1.5), null));
		buttonlist.add(new Button("(8 Gold) : Gain 5 max health", (float) (width * 0.385), (float) (width * 0.24),
				(float) (height * 0.33 + gap * 2), (float) (SimpleGameData.getSquareSize() * 1.5), null));
		buttonlist.add(new Button("Nothing for me", (float) (width * 0.385), (float) (width * 0.24),
				(float) (height * 0.33 + gap * 3), (float) (SimpleGameData.getSquareSize() * 1.5), null));
		return buttonlist;
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
		return 4;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		Button clicked = buttonClicked(x, y);
		if (clicked == null)
			return false;
		switch (clicked.legend()) {
		case "(3 Gold) : Remove all curses":
			if (data.getBackpack().getGold() >= 3) {
				if (data.getBackpack().removeCurse()) {
					data.getFightData().useCost("gold", 3);
					SimpleGameData.setPrintHealer(false);
					return true;
				}
			}
			break;
		case "(5 Gold) : Heals 25HP":
			if (data.getFightData().useCost("gold", 5)) {
				data.getHero().heal(25);
				SimpleGameData.setPrintHealer(false);
				return true;
			}
			break;
		case "(8 Gold) : Gain 5 max health":
			if (data.getFightData().useCost("gold", 8)) {
				data.getHero().addMaxhealth(5);
				SimpleGameData.setPrintHealer(false);
				return true;
			}
			break;
		case "Nothing for me":
			SimpleGameData.setPrintHealer(false);
			return true;
		}
		return false;
	}

	@Override
	public void prepareRoom(SimpleGameData data) {
		SimpleGameData.setPrintHealer(true);
		return;
	}

}