package bph.data;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import bph.items.BackPack;
import bph.items.Gold;
import bph.items.Item;
import bph.map.Roomable;
import bph.room.Room;

/**
 * This class deals with the data related to backpack interaction or trash
 * 
 * @author HOUANGKEO COUTELLIER
 */
public record SimpleBackpackData(BackPack backpack, BackPack trash) {
	
	/**
     * Organizes the backpack by moving the selected object to the specified coordinates.
     *
     * @param i The horizontal index of the target square.
     * @param j The vertical index of the target square.
     * @param selected The selected object to be moved.
     * @param actualroom The current room.
     * @return The object selected after organizing the backpack.
     */
	public Item organizeBackpack(int i, int j, Item selected, Room actualroom) {
		if (isInValidRange(i, j)) {
			if (i >= backpack.columns()) {
				selected = organize(trash, backpack, backpack.columns() + 2, i, j, selected, actualroom);
			} else {
				selected = organize(backpack, trash, 0, i, j, selected, actualroom);
			}

		}
		return selected;
	}
	
	/**
     * Organize the backpack by moving the selected object.
     *
     * @param mat The source backpack.
     * @param other The target backpack.
     * @param lesscol The column offset for the source backpack.
     * @param i The horizontal index of the target square.
     * @param j The vertical index of the target square.
     * @param selected The selected object to be moved.
     * @param actualroom The current room.
     * @return The object selected after the backpacks have been organized.
     */
	private Item organize(BackPack mat, BackPack other, int lesscol, int i, int j, Item selected, Room actualroom) {
		if (selected == null) {
			selected = mat.getItem(mat.indexBackpack(j, i - lesscol));
		} else {
			if (mat.getIndex(selected) == 0 && selected.isMovable()) {
				moveFromBackpacks(mat, other, lesscol, i, j, selected, actualroom);
			} else {
				mat.move(selected, j, i - lesscol);
			}
			selected = null;
		}
		return selected;
	}
	
	/**
     * Moves selected object between backpacks.
     *
     * @param mat The source backpack.
     * @param other The target backpack.
     * @param lesscol The column offset for the source backpack.
     * @param i The horizontal index of the target square.
     * @param j The vertical index of the target square
     * @param selected The selected object to be moved.
     * @param actualroom The actual room.
     */
	private void moveFromBackpacks(BackPack mat, BackPack other, int lesscol, int i, int j, Item selected,
			Room actualroom) {
		if (lesscol != 0 || actualroom.canAfford(selected, this)) {
			if (mat.add(selected, j, i - lesscol, other.getRotation(selected))) {
				var cells = other.getCells(other.getIndex(selected));
				other.remove(selected);
				if (!actualroom.tradeItem(selected, lesscol != 0, this)) {
					other.add(new Gold(selected.getPrice()), cells.get(0).i(), cells.get(0).j());
				}
			}
		}
	}

	private boolean isInValidRange(int i, int j) {
        boolean isInBackpackRange = (i >= 0 && i < backpack.columns())
                || (i >= backpack.columns() + 2 && i < trash.columns() +backpack.columns() + 2);
        boolean isInTrashRange = j >= 0 && j < trash.lines();
        return isInBackpackRange && isInTrashRange;
    }
	
	/**
     * Rotates the selected object in the backpack.
     *
     * @param selected The selected object to rotate.
     */
	public void rotate(Item selected) {
		if (selected != null) {
			var mat = trash;
			if (backpack.getInventory().contains(selected)) {
				mat = backpack;
			}
			mat.rotate(selected);
		}
	}
	
	/**
     * Unlocks the cells at the specified coordinates.
     *
     * @param i The horizontal index of the target square.
     * @param j The vertical index of the target square.
     * @param levelingup The current level.
     * @return The number of square unlocked since leveling up.
     */
	public int levelup(int i, int j, int levelingup) {
		if (levelingup >= new Random().nextInt(3, 5)) {
			return -1;
		}
		if (backpack.unlock(j, i)) {
			return levelingup + 1;
		}
		return levelingup;
	}

	/**
     * Generates a list of drop objects according to specified rarities.
     *
     * @param rarities Lists objects by rarity.
     * @param Merchantdrop Indicates whether the drop is for a merchant.
     * @return The object list generated.
     */
	public static List<Item> generatedrop(HashMap<String, ArrayList<Item>> rarities, boolean Merchantdrop) {
		Objects.requireNonNull(rarities);
		var listitems = new ArrayList<Item>();
		var rand = new Random();
		int more = (Merchantdrop) ? 3 : 0;
		for (int i = 0; i < rand.nextInt(2+more, 6); i++) {
			String rar = randomRarity(rand);
			while (rarities.get(rar).size() < 1) {
				rar = randomRarity(rand);
			}
			Item getRandomItem = rarities.get(rar).get(rand.nextInt(rarities.get(rar).size()));
			if (Merchantdrop && getRandomItem.getName().equals("Gold")) {
				i--;
				continue;
			}
			Item item = getRandomItem.copy();
			listitems.add(item);
		}
		return listitems;
	}

	public void addtoTrash(List<Roomable> items) {
		Objects.requireNonNull(items);
		for (Roomable item : items) {
			addtoTrash((Item) item);
		}
	}
	
	/**
     * Adds an object to the trash can where there is space.
     *
     * @param item The object to add.
     * 
     */
	public void addtoTrash(Item item) {
		Objects.requireNonNull(item);
		int j = 0;
		int k = 0;
		while (!trash.add(item, j, k)) {
			if (k > trash.columns()) {
				j++;
				k = 0;
			} else {
				k++;
			}
		}
	}

	private static String randomRarity(Random rand) {
		var rarity = rand.nextInt(1000);
		String rar = "Legendary";
		if (rarity < 800) {
			rar = "Common";
		} else if (rarity < 900) {
			rar = "Uncommon";
		} else if (rarity < 990) {
			rar = "Rare";
		}
		return rar;
	}

	public void addBackpack(Item item, int i, int j) {
		backpack.add(item, i, j);
	}

	public void addTrash(Item item, int i, int j) {
		trash.add(item, i, j);
	}

	public void cleartrash() {
		trash.clear();
	}

	public BackPack getBackpack() {
		return backpack;
	}

	public BackPack getTrash() {
		return trash;
	}

}
