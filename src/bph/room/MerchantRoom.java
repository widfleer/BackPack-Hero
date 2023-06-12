package bph.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bph.data.Button;
import bph.data.SimpleBackpackData;
import bph.data.SimpleGameData;
import bph.items.BackPack;
import bph.items.Item;
import bph.map.Roomable;

public record MerchantRoom(int i, int j, ArrayList<Roomable> content) implements Room  {

	public MerchantRoom(int i, int j){
		this(i, j,new ArrayList<Roomable>(SimpleBackpackData.generatedrop(SimpleGameData.getrarities(),true)));
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
		return 3;
	}
	
	@Override
	public List<Button> getButtons() {
		var buttonlist = new ArrayList<Button>();
		if (!SimpleGameData.printMerchant()) {
			return buttonlist;
		}
		float buttonwidth = (float) (SimpleGameData.getWidth() * 0.14);
		float height = SimpleGameData.getHeight();
		float width = SimpleGameData.getWidth();
		float buttonheight = (float) (SimpleGameData.getSquareSize() * 1.5);
		buttonlist.add(new Button("NO THANKS ^^", (float) (width/2-buttonwidth/2), buttonwidth, (float) (height * 0.66), buttonheight, null));
		return buttonlist;
	}
	
	@Override
	public boolean canAfford(Item item, SimpleBackpackData backpackdata) {
		Objects.requireNonNull(item);
		Objects.requireNonNull(backpackdata);
		if (item.getName().equals("Gold")) return true;
		return (backpackdata.getBackpack().getGold()>=item.getPrice());
	}
	
	@Override
	public boolean tradeItem(Item item, boolean sell,SimpleBackpackData backpackdata) {
		Objects.requireNonNull(item);
		Objects.requireNonNull(backpackdata);
		BackPack backpack = backpackdata.getBackpack();
		if (item.getName().equals("Gold")) return true;
		if(!sell){
			backpack.updateStackable("Gold", -item.getPrice());
			content.remove(item);
		}else{
			if(backpack.getGold()>0){
				backpack.updateStackable("Gold", item.getPrice());
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean handleclick(SimpleGameData data, float x, float y) {
		Button clicked = buttonClicked(x, y);
		if (clicked == null)
			return false;
		switch (clicked.legend()) {
		case "NO THANKS ^^":
			SimpleGameData.setPrintMerchant(false);
			data.getBackpackdata().cleartrash();
			return true;
		}
		return false;
	}

	@Override
	public void prepareRoom(SimpleGameData data) {
		SimpleGameData.setPrintMerchant(true);
		data.openBackpack();
		data.getBackpackdata().addtoTrash(this.content());
	}
	
}