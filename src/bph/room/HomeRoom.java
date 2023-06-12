package bph.room;

import java.util.ArrayList;
import java.util.List;

import bph.data.Button;
import bph.data.SimpleGameData;
import bph.map.Roomable;

public record HomeRoom(int i, int j, ArrayList<Roomable> content) implements Room {

	public HomeRoom(int i, int j) {
		this(i, j, new ArrayList<>());
	}

	@Override
	public void clearRoom() {
		content.clear();
	}

	@Override
	public List<Button> getButtons() {
		var buttonlist = new ArrayList<Button>();
		float buttonwidth = (float) (500);
		float width = SimpleGameData.getWidth();
		float height = SimpleGameData.getHeight();
		float buttonheight = (float) (width/20);
		if (!SimpleGameData.isHome()) {
			buttonlist.add(new Button("CLEAR", (float) ((width * 1 / 3) - 245), buttonwidth, (float) (height * 6.5 / 8),
					buttonheight, null));
			buttonlist.add(new Button("PLAY", (float) ((width * 2 / 3) - 245), buttonwidth, (float) (height * 6.5 / 8),
					buttonheight, null));
		} else {
			buttonlist.add(new Button("START GAME", (float) ((width * 1 / 2) - 245), buttonwidth,
					(float) (height * 5 / 8), buttonheight, null));
		}
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
		// TODO Auto-generated method stub
		return 9;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		Button clicked = buttonClicked(x, y);
		if (clicked != null) {
			switch (clicked.legend()) {
			case "START GAME":
				data.setHome(false);
				break;
			case "CLEAR":
				data.getHero().resetName();
				break;
			case "PLAY":
				if (!data.getHero().getName().equals("")) {
					data.setInGame(true);
				}
				break;

			}
			return clicked != null;
		}
		return false;

	}
}