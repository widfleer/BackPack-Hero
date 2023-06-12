package bph.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import bph.map.Cell;

/**
 * The BackPack class represents a backpack that can contain objects. It
 * contains every method related to the backpack in itself.
 */
public class BackPack {
	private final int[][] matrix;
	private ArrayList<Item> inventory;
	private ArrayList<Integer> rotation;

	/**
	 * BackPack class constructor.
	 * 
	 * @param istrash indicates whether the backpack is a trash can (true) or not
	 *                (false).
	 */
	public BackPack(boolean istrash) {
		int j = (!istrash) ? 7 : 11;
		matrix = new int[5][j];
		if (!istrash) {
			starter();
		}
		inventory = new ArrayList<Item>();
		rotation = new ArrayList<>();
	}

	public BackPack() {
		this(false);
	}

	/**
	 * Initializes the matrix for a classic backpack. (not a trash)
	 */
	private void starter() {
		for (int i = 0; i < matrix[0].length - 1; i++) {
			matrix[0][i] = -1;
			matrix[matrix.length - 1][i] = -1;
			if (i < matrix.length) {
				matrix[i][0] = -2;
				matrix[i][matrix[0].length - 1] = -2;
				matrix[i][1] = -1;
				matrix[i][matrix[0].length - 2] = -1;
			}
		}
	}

	/**
	 * Adds an object to the backpack at a specified position with a given rotation.
	 * 
	 * @param item   the object to be added
	 * @param i      the vertical coordinate of the position
	 * @param j      the horizontal coordinate of the position
	 * @param rotate the rotation of the object
	 * @return true if object added successfully, false otherwise
	 */
	public boolean add(Item item, int i, int j, int rotate) {
		if (item.isStackable()) {
			if (stackUp(item)) {
				return true;
			}
		}
		for (var coord : item.getCoordinates()) {
			if (0 <= i + coord.first() && matrix.length > i + coord.first() && 0 <= j + coord.last()
					&& matrix[0].length > j + coord.last() && (matrix[i + coord.first()][j + coord.last()] == 0)) {
				matrix[i + coord.first()][j + coord.last()] = inventory.size() + 1;
			} else {
				for (int k = 0; k < matrix.length; k++) {
					for (int l = 0; l < matrix[0].length; l++) {
						if (matrix[k][l] == inventory.size() + 1) {
							matrix[k][l] = 0;
						}
					}
				}
				return false;
			}
		}
		inventory.add(item);
		rotation.add(rotate);
		return true;
	}

	/**
	 * Adds a curse to the backpack at a specified position and remove the other
	 * objects.
	 * 
	 * @param item the object to be added
	 * @param i    the vertical coordinate of the position
	 * @param j    the horizontal coordinate of the position
	 * @return true if object added successfully, false otherwise
	 */
	public boolean addCurse(Item item, int i, int j) {
		if (!add(item, i, j, 0)) {
			for (var coord : item.getCoordinates()) {
				if (!(0 <= i + coord.first() && matrix.length > i + coord.first() && 0 <= j + coord.last()
						&& matrix[0].length > j + coord.last())) {
					return false;
				}
				int index = matrix[i + coord.first()][j + coord.last()];
				if (index != -2 && index != -1 && index != 0) {
				}
				if (index == -2 || index == -1 || (index != 0 && getItem(index).getName().contains("Curse"))) {

					return false;
				}
			}
			for (var coord : item.getCoordinates()) {
				if (matrix[i + coord.first()][j + coord.last()] != 0) {
					remove(getItem(matrix[i + coord.first()][j + coord.last()]));
				}
			}
		}
		add(item, i, j, 0);
		return true;
	}

	/**
	 * Checks whether an object can be stacked on top of an existing object in the
	 * backpack.
	 * 
	 * @param item the object to be stacked
	 * @return true if the object could be stacked, false otherwise
	 */
	private boolean stackUp(Item item) {
		Stackable item2 = (Stackable) item;
		for (Item items : inventory) {
			if (items.getName() == item.getName()) {
				((Stackable) items).updateQuantity(item2.getQuantity());
				return true;
			}
		}
		return false;
	}

	public boolean add(Item item, int i, int j) {
		return add(item, i, j, 0);
	}

	/**
	 * Deletes an object from the backpack.
	 * 
	 * @param item the object to delete
	 * @return true if the object has been successfully deleted, false otherwise.
	 */
	public void remove(Item item) {
		Objects.requireNonNull(item);
		if (inventory.contains(item)) {
			for (int k = 0; k < matrix.length; k++) {
				for (int l = 0; l < matrix[0].length; l++) {
					if (matrix[k][l] == inventory.indexOf(item) + 1) {
						matrix[k][l] = 0;
					}
					if (matrix[k][l] > inventory.indexOf(item) + 1) {
						matrix[k][l] -= 1;
					}
				}
			}
		}
		rotation.remove(inventory.indexOf(item));
		inventory.remove(item);
	}

	/**
	 * Deletes all the curses from the backpack.
	 * 
	 * @return true if there was object to remove, false otherwise.
	 */
	public boolean removeCurse() {
		ArrayList<Item> itemsToRemove = new ArrayList<>();
		for (Item item : inventory) {
			if (item.getName().contains("Curse")) {
				itemsToRemove.add(item);
			}
		}
		for (Item item : itemsToRemove) {
			remove(item);
		}
		return (!itemsToRemove.isEmpty());
	}
	
	/**
     * Returns the coordinates of the cells occupied by an object in the backpack.
     * @param item the object
     * @return list of coordinates of cells occupied by the object
     */
	public ArrayList<Cell> getCells(int index) {
		var backpackcells = new ArrayList<Cell>();
		for (int k = 0; k < matrix.length; k++) {
			for (int l = 0; l < matrix[0].length; l++) {
				if (matrix[k][l] == index) {
					backpackcells.add(new Cell(k, l));
				}
			}
		}
		return backpackcells;
	}
	
	/**
     * Empty the backpack completely.
     */
	public void clear() {
		for (int k = 0; k < matrix.length; k++) {
			for (int l = 0; l < matrix[0].length; l++) {
				matrix[k][l] = 0;
			}
		}
		this.inventory = new ArrayList<>();
		this.rotation = new ArrayList<>();
	}
	
	/**
     * Moves an object from the backpack to a new position.
     * @param item the object to move
     * @param newPosition the object's new position
     */
	public boolean move(Item item, int i, int j) {
		Objects.requireNonNull(item);
		if (!item.isMovable())
			return false;
		for (var coord : item.getCoordinates()) {
			if (0 > i + coord.first() || matrix.length <= i + coord.first() || 0 > j + coord.last()
					|| matrix[0].length <= j + coord.last()
					|| (matrix[i + coord.first()][j + coord.last()] != 0
							&& matrix[i + coord.first()][j + coord.last()] != getIndex(item))
					|| matrix[i + coord.first()][j + coord.last()] == -1
					|| matrix[i + coord.first()][j + coord.last()] == -2) {
				return false;
			}
		}
		for (int k = 0; k < matrix.length; k++) {
			for (int l = 0; l < matrix[0].length; l++) {
				if (matrix[k][l] == getIndex(item)) {
					matrix[k][l] = 0;
				}
			}
		}
		for (var coord : item.getCoordinates()) {
			matrix[i + coord.first()][j + coord.last()] = getIndex(item);
		}
		return true;
	}
	
	/**
     * Updates the list of stackable objects.
     */
	public boolean updateStackable(String name, int update) {
		Objects.requireNonNull(name);
		int toremove = -1;
		for (var item : inventory) {
			if (item.isStackable() && item.getName().equals(name)) {
				Stackable stack = (Stackable) item;
				if (stack.updateQuantity(update)) {
					if (stack.getQuantity() < 1) {
						toremove = getIndex(item);
					}
					toremove = 0;
				}
			}
		}
		if (toremove != -1) {
			if (toremove != 0) {
				inventory.remove(toremove);
			}
			return true;
		}
		return false;
	}

	/**
	 * This method rotates the specified object.
	 * If the rotation is successful, the object is moved to the first available cell after the current position.
	 * If the move is successful, the object's rotation is updated in the rotation list.
	 * Otherwise, if the move fails, the object is rotated three more times without being moved.
	 *
	 * @param item The object to rotate.
	 */
	public void rotate(Item item) {
		int index = inventory.indexOf(item);
		var cells = this.getCells(index + 1);
		if (item.rotate()) {
			if (move(item, cells.get(0).i(), cells.get(0).j())) {
				rotation.set(index, (rotation.get(index) + 1) % 4);
			} else {
				for (int i = 0; i < 3; i++)
					item.rotate();
			}
		}
	}
	
	/**
	 * Unlocked the specified cell;
	 *
	 * @param i the vertical coordinate of the position
	 * @param j the horizontal coordinate of the position
	 */
	public boolean unlock(int i, int j) {
		if (matrix[i][j] != -1) {
			return false;
		}
		matrix[i][j] = 0;
		for (int k = -1; k <= 1; k++) {
			for (int l = -1; l <= 1; l++) {
				if (i + k >= 0 && i + k < matrix.length && j + l >= 0 && j + l < matrix[0].length) {
					if (matrix[i + k][j + l] == -2) {
						matrix[i + k][j + l] = -1;
					}
				}
			}
		}
		return true;
	}

	public boolean unlocked() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == -1) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
     * Returns the objects adjacent to a given cell in the backpack.
     * @param cell the cell
     * @return list of objects adjacent to the cell
     */
	public HashSet<Item> itemsAround(String direction, Item item, boolean mana) {
		var dirs = chooseDirection(direction);
		int indexitem = getIndex(item);
		return itemAround(dirs, indexitem, mana, new HashSet<Item>());
	}
	
	/**
     * Returns a list of objects around a given item index.
     * @param dirs the list of directions
     * @param indexitem the index of the item
     * @param mana is the function should look for conductive items
     * @param itemsaound the set of items around
     * @return a list of objects around the given position
     */
	private HashSet<Item> itemAround(ArrayList<Cell> dirs, int indexitem, boolean mana, HashSet<Item> itemsaround) {
		var cells = getCells(indexitem);
		if (itemsaround.contains(getItem(indexitem)))
			return itemsaround;
		itemsaround.add(getItem(indexitem));
		for (Cell cell : cells) {
			for (Cell dir : dirs) {
				if (cell.i() + dir.i() >= 0 && cell.i() + dir.i() < matrix.length && cell.j() + dir.j() >= 0
						&& cell.j() + dir.j() < matrix[0].length) {
					int indexaround = matrix[cell.i() + dir.i()][cell.j() + dir.j()];
					if (indexaround != indexitem && indexaround != 0 && indexaround != -1 && indexaround != -2) {
						if (getItem(indexaround).isConductive() && mana) {
							itemsaround.addAll(itemAround(dirs, indexaround, mana, itemsaround));
						}
					}
				}
			}
		}
		return itemsaround;
	}

	public ArrayList<Cell> chooseDirection(String dir) {
		ArrayList<Cell> dirs = new ArrayList<Cell>(Arrays.asList(new Cell(-1, -1), new Cell(-1, 0), new Cell(-1, 1),
				new Cell(1, -1), new Cell(1, 0), new Cell(1, 1), new Cell(0, -1), new Cell(0, 1)));
		int updown = 2, leftright = 2;
		leftright = (dir.contains("left")) ? -1 : (dir.contains("right")) ? 1 : 2;
		updown = (dir.contains("up")) ? -1 : (dir.contains("down")) ? 1 : 2;
		for (int i = 0; i < dirs.size(); i++) {
			Cell cell = dirs.get(i);
			if ((!(cell.i() == updown)) && (!(updown == 2)) || (!(cell.j() == leftright)) && (!(leftright == 2))
					|| ((dir.contains("adjacent") && cell.i() != 0 && cell.j() != 0))) {
				dirs.remove(cell);
				i--;
			}
		}
		return dirs;
	}

	/**
	 * Returns a string representation of the backpack matrix.
	 * 
	 * @return Backpack matrix representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			sb.append("[ ");
			for (int j = 0; j < matrix[0].length; j++) {
				sb.append("[");
				sb.append(matrix[i][j]);
				sb.append("] ");
			}
			sb.append("]");
			sb.append("\n");
		}
		return sb.toString();
	}

	public int lines() {
		return matrix.length;
	}

	public int columns() {
		return matrix[0].length;
	}

	public int indexBackpack(int i, int j) {
		return matrix[i][j];
	}
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public int getIndex(Item item) {
		return inventory.indexOf(Objects.requireNonNull(item)) + 1;
	}

	public Item getItem(int index) {
		if (index <= 0 || index > inventory.size()) {
			return null;
		}
		return inventory.get(index - 1);
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public int getGold() {
		for (var item : inventory) {
			if (item.getName().equals("Gold")) {
				Gold gold = (Gold) item;
				return gold.getQuantity();
			}
		}
		return 0;
	}
		public int getRotation(Item item) {
		Objects.requireNonNull(item);
		var index = getIndex(item);
		return rotation.get(index - 1);
	}

	public int getcase(int i, int j) {
		return matrix[i][j];
	}

	public boolean isEmpty() {
		return inventory.size() == 0;
	}
}
