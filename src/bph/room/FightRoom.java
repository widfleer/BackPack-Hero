package bph.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bph.character.Bestiary;
import bph.character.Enemy;
import bph.data.Button;
import bph.data.SimpleFightData;
import bph.data.SimpleGameData;
import bph.map.Roomable;

public record FightRoom(int i, int j, ArrayList<Roomable> content) implements Room {

	public FightRoom(int i, int j) {
		this(i, j, generaterandomEnemy());
	}

	private static ArrayList<Roomable> generaterandomEnemy() {
		var salle = new ArrayList<Roomable>();
		var rand = new Random();
		int maxenemy = rand.nextInt(1, 3);
		int size = (SimpleGameData.getDungeon()==null) ? 1 : (SimpleGameData.getDungeon().size()+1);
		if (size>3) return salle;
		for (int enemy = 0; enemy < maxenemy; enemy++) {
			var newenemy = Enemy.createEnemy(Bestiary.values()[rand.nextInt(Bestiary.values().length)].getName());
			if(newenemy.getDifficulty()!=size) {
				enemy--;
			}else {
				if(size==3) {
					if(newenemy.getName().equals("Living Shadow")) {
						salle.add(Enemy.createEnemy(newenemy.getName()));
					}
					enemy = maxenemy;
				}
				salle.add(newenemy);
			}
		}
		return salle;
	}

	@Override
	public void clearRoom() {
		content.clear();
	}

	@Override
	public List<Button> getButtons() {
		var buttonlist = new ArrayList<Button>();
		if (!content.isEmpty()) {
			float buttonwidth = (float) (SimpleGameData.getWidth() * 0.14);
			float height = SimpleGameData.getHeight();
			float buttonheight = (float) (SimpleGameData.getSquareSize() * 1.5);
			float gap = (float) (SimpleGameData.getSquareSize() * 1.7);
			String orga = (SimpleGameData.isOrganizing()) ? "Finish" : "Reorganize";
			buttonlist.add(new Button(orga, 0, buttonwidth, (float) (height * 0.4), buttonheight, null));
			if (!SimpleGameData.isOrganizing()) {
				buttonlist.add(new Button("Scratch", 0, buttonwidth, (float) (height * 0.4) + gap, buttonheight, null));
				buttonlist.add(
						new Button("End Turn", 0, buttonwidth, (float) (height * 0.4) + gap * 2, buttonheight, null));
			}
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
		return 2;
	}

	public boolean handleclick(SimpleGameData data, float x, float y) {
		if (content.isEmpty()) return false;
		Button clicked = buttonClicked(x, y);
		SimpleFightData fightdata = data.getFightData();
		if (clicked != null) {
			switchClicked(clicked.legend(), data, fightdata);
		}
		float width = SimpleGameData.getWidth();
		float height = SimpleGameData.getHeight();
		float squareSize = SimpleGameData.getSquareSize();
		if ((width / 2 < x) && (x < (width / 2) + (squareSize * 3 * fightdata.getEnemyinfight().size()))
				&& (height * 2 / 3 < y) && (y < height * 2 / 3 + squareSize * 2.5)) {
			fightdata.setselectedenemy((int) ((x - width / 2) / (squareSize * 3)));
			return true;
		}
		if (!fightdata.prepareenemyturn()) {
			data.winFight();
		}
		return clicked != null;
	}

	private void switchClicked(String legend, SimpleGameData data, SimpleFightData fightdata) {
		switch (legend) {
		case "Reorganize":
			if (fightdata.useCost("energy", 3)) {
				data.setOrganize(true);
			}
			break;
		case "Finish":
			data.setOrganize(false);
			break;
		case "Scratch":
			if (fightdata.useCost("energy", 1)) {
				fightdata.choosefight("attack", 3, data.getHero(),
						fightdata.getEnemyinfight().get(fightdata.getselectedenemy()));
				break;
			}
		case "End Turn":
			fightdata.setheroTurn(false);
			break;
		}
	}

	@Override
	public void prepareRoom(SimpleGameData data) {
		data.preparetoFight();
	}
}