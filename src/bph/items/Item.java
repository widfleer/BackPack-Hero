package bph.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import bph.map.Pair;
import bph.map.Roomable;

public interface Item extends Roomable{
	public List<Pair> getCoordinates();
	public String getName();
	public String getDescription();
	public int getPrice();
	ArrayList<Cost> getCost();
	ArrayList<Stringint> usethis();
	UUID getId();
	String getrarity();
	Item copy();
	default boolean isArmor(){
		return false;
	};
	default boolean isStackable(){
		return false;
	}
	
	default boolean isQuantifiable(){
		return false;
	}
	
	default boolean isConductive() {
		return false;
	}
	
	default boolean isMovable() {
		return true;
	}
	default boolean isMeleeWeapon() {
		return false;
	}
	
	ArrayList<InteractionAction> getinteractionAction();
	
	default boolean rotate(){
		int n = getCoordinates().size();
		for (Pair coo : getCoordinates()) {
			int res = coo.last();
			coo.changeLast(n-coo.first()-1);
			coo.changeFirst(res);
		}
		 int minI = Integer.MAX_VALUE;
	     int minJ = Integer.MAX_VALUE;
	     for (Pair coordinate : getCoordinates()) {
	            minI = Math.min(minI, coordinate.first());
	            minJ = Math.min(minJ, coordinate.last());
	        }
	     for (Pair coordinate : getCoordinates()) {
	            coordinate.changeFirst(coordinate.first() - minI);
	            coordinate.changeLast(coordinate.last() - minJ);		
	        }
	     return true;
	}
	
	default boolean isProjectile() {
		return false;
	}
	
	public static Item getItemByNameAndRarity(String name,String rarity,HashMap<String, ArrayList<Item>> rarities) {
		var searchlist = rarities.get(rarity);
		for (Item item : searchlist) {
			if (item.getName().equals(name)) {
				return item.copy();
			}
		}
		return null;
	}
	
	public default void addInteractionAction(InteractionAction interaction) {};
}

































































