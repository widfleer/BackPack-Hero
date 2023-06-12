package bph.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bph.data.Button;
import bph.data.SimpleGameData;
import bph.items.Curse;
import bph.map.Roomable;

public class CurseRoom implements Room {
	private int selecti, selectj;
	private final Curse curse;

	public CurseRoom(int selecti, int selectj, Curse curse) {
		this.setSelecti(selecti);
		this.setSelectj(selectj);
		this.curse = Objects.requireNonNull(curse);
	}

	@Override
	public ArrayList<Roomable> content() {
		return null;
	}

	@Override
	public int getI() {
		return 0;
	}

	@Override
	public int getJ() {
		return 0;
	}

	@Override
	public void clearRoom() {
	}

	@Override
	public int typeRoom() {
		return 2;
	}

	@Override
	public List<Button> getButtons() {
		var buttonlist = new ArrayList<Button>();
		float width = SimpleGameData.getWidth();
		float height = SimpleGameData.getHeight();
		var gap = SimpleGameData.getSquareSize() * 2;
		buttonlist.add(new Button("You have been cursed!", (float) (width * 0.27), (float) (width * 0.5),
				(float) (height * 0.45), (float) (SimpleGameData.getSquareSize() * 2), null));
		buttonlist.add(new Button("Skip curse, take " + curse.getDamage() + " damage", (float) (width * 0.385),
				(float) (width * 0.24), (float) (height * 0.50 + gap), (float) (SimpleGameData.getSquareSize() * 2),
				null));
		buttonlist.add(new Button("Finished reorganizing", (float) (width * 0.385), (float) (width * 0.24),
				(float) (height * 0.50 + gap * 2), (float) (SimpleGameData.getSquareSize() * 2), null));
		return buttonlist;

	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		Button clicked = buttonClicked(x, y);
		if (clicked != null) {
			if (clicked.legend().contains("Finished")) {
				if (!data.getBackpack().addCurse(curse, selecti, selectj)) {
					return false;
				}
			} else {
				data.getHero().takeUnprotectedHit(curse.getDamage());
				Curse.incrementDamage();
			}
			data.setPlacingCurse(false);
			data.resetActualRoom();
			data.setSelectedItem(null);
			return true;
		}
		return false;
	}

	public int getSelecti() {
		return selecti;
	}

	public void setSelecti(int selecti) {
		this.selecti = selecti;
	}

	public int getSelectj() {
		return selectj;
	}

	public void setSelectj(int selectj) {
		this.selectj = selectj;
	}

	public Curse getCurse() {
		return curse;
	}

}
