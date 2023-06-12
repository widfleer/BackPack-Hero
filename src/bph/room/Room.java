package bph.room;

import java.util.ArrayList;
import java.util.List;
import bph.data.Button;
import bph.data.SimpleBackpackData;
import bph.data.SimpleGameData;
import bph.items.Item;
import bph.map.Roomable;

public interface Room {

	ArrayList<Roomable> content();
	
	public int getI();
	public int getJ();

	public default boolean isEmpty() {
		return content().size() == 0;
	}

	public default boolean isAccessible() {
		return content() != null;
	}

	public void clearRoom();

	public default List<Button> getButtons(){
		return new ArrayList<Button>();
	}

	public default Button buttonClicked(float x, float y) {
		if (getButtons() != null) {
			for (Button button : getButtons()) {
				if (button.isClicked(x, y))
					return button;
			}
		}
		return null;
	}
	
	public int typeRoom(); 
	
	public boolean handleclick(SimpleGameData data, float x,float y);
	
	public default void prepareRoom(SimpleGameData data) {};
	
	public default boolean canAfford(Item item, SimpleBackpackData backpackdata) {return true;}

	public default boolean tradeItem(Item item, boolean sell,SimpleBackpackData backpackdata) {return true;};
}