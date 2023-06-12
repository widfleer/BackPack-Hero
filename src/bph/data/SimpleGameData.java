package bph.data;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import bph.character.Enemy;
import bph.character.Hero;
import bph.items.BackPack;
import bph.items.Consumable;
import bph.items.Cost;
import bph.items.Item;
import bph.items.Shield;
import bph.items.Stringint;
import bph.items.Sword;
import bph.map.Dungeon;
import bph.map.Floor;
import bph.map.Pair;
import bph.room.Room;
import bph.room.CurseRoom;
import bph.room.HomeRoom;
import bph.view.SimpleGameView;

import java.util.HashMap;
import java.util.List;

/**
 * This class represents all the data stored in the game. It contains
 * information on the hero, game state, game elements such as enemies, objects,
 * rooms, etc. * It also manages game interactions, events and gameplay
 * operations. It also manages game interactions, events and gameplay
 * operations.
 */
public class SimpleGameData {
	private final Hero hero;
	private boolean backpackOpen, endGame, inGame, placingCurse;
	private static boolean organize, isHome, printHealer, printMerchant;
	private final ImageLoader maploader, backgroundloader, characterloader;
	private SimpleBackpackData backpackdata;
	private Item selected;
	private SimpleFightData fightdata;
	private int eventnumber, levelingup;
	private Font dialogue = new Font("Arial", Font.PLAIN, 20);
	private static float width = 0, height = 0, squareSize = 0;
	private static HashMap<String, ArrayList<Item>> rarities = SimpleItemData.generateRarity();
	private static final Dungeon dungeon = new Dungeon();
	private List<PlayerScore> topScores;
	private final HomeRoom home = new HomeRoom(0, 0);
	private static Room actualroom;

	/**
	 * Constructs a new SimpleGameData object with the given parameters. Initializes
	 * set data with specified parameters.
	 *
	 * @param width      The width of the game.
	 * @param height     The height of the game.
	 * @param squareSize The size of a square in the game.
	 */
	public SimpleGameData(float width, float height, float squareSize) {
		backpackdata = new SimpleBackpackData(new BackPack(), new BackPack(true));
		hero = new Hero("");
		isHome = true;
		endGame = false;
		inGame = false;
		hero.changeRoom(dungeon.get(0).cellstart().i(), dungeon.get(0).cellstart().j());
		backpackOpen = true;
		levelingup = -1;
		maploader = new ImageLoader("data", "casemap.png", "Hero Icon.png", "enemyroom.png", "pockets.gif",
				"healericon.png", "treasureicon.png", "exitdoor.png", "map.png");
		backgroundloader = new ImageLoader("data", "back.png", "case.png", "casecopy.png", "bph.png", "button.png",
				"podium.png", "card.png", "card_mana.png", "price.png", "healer_card.png", "backpack.png",
				"pockets_card.png", "home_allies.png", "home_ennemies.png");
		characterloader = new ImageLoader("data", "purse.png", "pockets.gif", "healer.png", "search.png", "chest.png",
				"energy.png");
		SimpleGameData.width = width;
		SimpleGameData.height = height;
		SimpleGameData.squareSize = squareSize;
		fightdata = new SimpleFightData(this, hero);
		temporaryStarterBackpack();
		resetActualRoom();
		topScores = new ArrayList<>();
	}

	private void temporaryStarterBackpack() {
		var sword = new Sword("Wooden Sword", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(2, 0))), "Common",
				"On use, Deals 7 damage", 3);
		var shield = new Shield("Rough Buckler", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(1, 1), new Pair(0, 1))),
				"Common", "On use, Adds 7 block", 3);

		backpackdata.addBackpack(sword, 1, 2);
		backpackdata.addBackpack(shield, 1, 3);
		backpackdata
				.addBackpack(new Consumable("Meal", "Common", new ArrayList<Cost>(Arrays.asList(new Cost(1, "itself"))),
						new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(0, 1))), 2,
						new ArrayList<Stringint>(Arrays.asList(new Stringint("addenergy", 2))),
						"2 uses\n" + "On use, Adds 2 Energy\n" + "this item gets destroyed", 6), 3, 3);

	}

	/*
	 * INTERACTIONS ----------------------------------------------------------------
	 */

	/**
	 * Handles a mouse click in the game. Checks click coordinates and performs
	 * appropriate actions according to game state.
	 *
	 * @param x    The horizontal coordinate of the click.
	 * @param y    The vertical coordinate of the click.
	 * @param view The game view in which the click took place.
	 */
	public void handleclick(float x, float y, SimpleGameView view) {
		Objects.requireNonNull(view);
		if (!isInGame()) {
			home.handleclick(this, x, y); // si le clic est sur l'accueil
		}
		if (!actualroom.handleclick(this, x, y)) {
			if (getWidth() - getSquareSize() * 1.5 < x && x < getWidth() && 80 < y && y < getSquareSize() + 120
					&& (eventnumber < 8)) { // si le clic est sur le switch
				if (!isBackpackOpen()) {
					openBackpack();
				} else if (isBackpackOpen() && !hero.isInFight()) {
					openMap();
				}
			} else if ((0 < x && x < getWidth() && 0 < y && y < getHeight() / 3 + 35) && (eventnumber < 8)) {
				clickOnCell(view.columnFromX(x), view.lineFromY(y));
			}
		}
	}

	/**
	 * Performs actions corresponding to a click on a game cell. If the backpack is
	 * open, checks whether a curse is to be placed in the cell. If not, performs
	 * actions specific to either the game card or the backpack, depending on the
	 * cell clicked.
	 *
	 * @param i The row index of the clicked cell.
	 * @param j The column index of the clicked cell.
	 */
	public void clickOnCell(int i, int j) {
		if (!backpackOpen) {
			clickOnMap(i, j);
		} else {
			if (placingCurse) {
				chooseCell(i, j);
			} else {
				clickOnBackpack(i, j);
			}
		}
	}

	private void clickOnMap(int i, int j) {
		Floor current = dungeon.get(hero.floor());
		if (current.clickOnCell(hero, i, j)) {
			backpackdata.cleartrash();// Delete trash items
			current.getRoom(j, i).prepareRoom(this);// Prepare the corresponding room
			eventnumber = current.typeRoom(i, j);
			resetActualRoom();
		}
	}

	private void clickOnBackpack(int i, int j) {
		BackPack backpack = backpackdata.getBackpack();
		if (levelingup >= 0) {
			levelingup = backpackdata.levelup(i, j, levelingup);
		}
		if (!hero.isInFight()) {
			selected = backpackdata.organizeBackpack(i, j, selected, actualroom);
		} else if (hero.isInFight() && organize) {
			selected = backpackdata.organizeBackpack(i, j, selected, actualroom);// Organizing your backpack during a
																					// fight
		} else if (((i >= 0 && i < backpack.columns()) && j >= 0 && j < backpack.lines())) {
			selected = backpack.getItem(backpack.indexBackpack(j, i));
			if (!fightdata.getEnemyinfight().isEmpty()) {
				fightdata.useObject(selected);// Use the selected object during combat
			}
			selected = null;
		}
	}

	/**
	 * Choose a cell to place the curse.
	 * 
	 * @param i The row index of the clicked cell.
	 * @param j The column index of the clicked cell.
	 */
	public void chooseCell(int i, int j) {
		BackPack backpack = backpackdata.getBackpack();
		if ((i >= 0 && i < backpack.columns()) && j >= 0 && j < backpack.lines()) {
			if (backpack.getcase(j, i) != -1 && backpack.getcase(j, i) != -2) {
				if (placingCurse) {
					((CurseRoom) actualroom).setSelectj(i);
					((CurseRoom) actualroom).setSelecti(j);
				}
			}
		}
	}

	/*
	 * EVENTS
	 * ------------------------------------------------------------------------
	 */

	/**
	 * Prepare the game for battle by collecting the necessary information. Open the
	 * backpack and initialize combat data with the hero and enemies in the room.
	 */
	public void preparetoFight() {
		var currentfloor = dungeon.get(hero.floor());
		if (!currentfloor.getRoom(hero.roomj(), hero.roomi()).isEmpty()) {
			ArrayList<Enemy> enemies = new ArrayList<>();
			for (var enemy : currentfloor.getRoom(hero.roomj(), hero.roomi()).content()) {
				enemies.add((Enemy) enemy);
			}
			openBackpack();
			selected = null;
			organize = false;
			fightdata.preparetoFight(hero, enemies);
		}
	}

	/**
	 * Performs the necessary actions when a fight is won by the hero. Updates the
	 * hero's stats, clears the enemy room and generates items for the drop.
	 */
	public void winFight() {
		fightdata.setheroTurn(false);
		int xp = 0;
		for (var enemy : actualroom.content()) {
			Enemy enemy2 = (Enemy) enemy;
			xp += enemy2.getXP();
		}
		actualroom.clearRoom();

		if (hero.gainXp(xp)) {
			if (!backpackdata.getBackpack().unlocked()) {
				levelingup = 0;
			}
		}
		for (Item item : SimpleBackpackData.generatedrop(rarities, false)) {
			backpackdata.addtoTrash(item);
		}
		SimpleItemData.forInteraction("end", this);
		hero.outOfFight();
	}

	/**
	 * Rotates the selected object in the backpack.
	 */
	public void rotate() {
		backpackdata.rotate(selected);
	}

	/**
	 * Saves the player's score in the high score ranking. Retrieves existing
	 * scores, adds the player's new score and updates the ranking. Returns the high
	 * score list.
	 *
	 * @return The updated high score list.
	 */
	public List<PlayerScore> hof() {
		List<PlayerScore> scores = HallOfFame.readScoresFromFile();
		HallOfFame.addNewPlayerScore(scores, getHero().getName(), score());
		scores = HallOfFame.readScoresFromFile();
		topScores = HallOfFame.getTopScores(scores);
		return topScores;
	}

	/**
	 * Calculates the score according to a recipe we created ourselves, based on the
	 * hero's maximum hit points and the sum of his equipment prices.
	 *
	 * @return The calculated score.
	 */
	public int score() {
		int somme = 0;
		for (var item : backpackdata.backpack().getInventory()) {
			somme += (item.getPrice() == -999) ? 0 : item.getPrice();
		}
		somme += getHero().floor() * 100;
		somme += (getHero().getPv() / getHero().getMaxhealth()) * 50;
		somme += (getHero().getMaxhealth() - 40) * 10;
		return somme;
	}

	/* GETTER AND SETTER -------------------------------------------------- */

	public Hero getHero() {
		return hero;
	}

	public boolean isInGame() {
		return inGame;
	}

	public static boolean isHome() {
		return isHome;
	}

	public void setHome(boolean bool) {
		isHome = bool;
	}

	public void setInGame(boolean bool) {
		inGame = bool;
	}

	public boolean endGame() {
		return endGame;
	}

	public void setEndGame(boolean bool) {
		hof();
		endGame = bool;
	}

	public static void setPrintHealer(boolean bool) {
		printHealer = bool;
	}

	public static boolean printHealer() {
		return printHealer;
	}

	public static void setPrintMerchant(boolean bool) {
		printMerchant = bool;
	}

	public static boolean printMerchant() {
		return printMerchant;
	}

	public int getScore() {
		return score();
	}

	public static Dungeon getDungeon() {
		return dungeon;
	}

	public ImageLoader getMaploader() {
		return maploader;
	}

	public BackPack getBackpack() {
		return backpackdata.getBackpack();
	}

	public BackPack getTrash() {
		return backpackdata.getTrash();
	}

	public ImageLoader getBackgroundloader() {
		return backgroundloader;
	}

	public ArrayList<Enemy> getEnemyinfight() {
		return fightdata.getEnemyinfight();
	}

	public int getEventnumber() {
		return eventnumber;
	}

	public void openMap() {
		backpackOpen = false;
	}

	public void openBackpack() {
		backpackOpen = true;
	}

	public boolean isBackpackOpen() {
		return backpackOpen;
	}

	public ImageLoader getCharacterloader() {
		return characterloader;
	}

	public Font fontdialogue() {
		return dialogue;
	}

	public boolean getHeroTurn() {
		return fightdata.getheroTurn();
	}

	public int getSelectedEnemy() {
		return fightdata.getselectedenemy();
	}

	public Item getSelectedItem() {
		return selected;
	}

	public String getPrediction(int i) {
		return fightdata.updatePrediction().get(i);
	}

	public int isLevelingup() {
		return levelingup;
	}

	public static boolean isOrganizing() {
		return organize;
	}

	public static HashMap<String, ArrayList<Item>> getrarities() {
		return rarities;
	}

	public List<PlayerScore> topScores() {
		return topScores;
	}

	public static float getWidth() {
		return width;
	}

	public static float getHeight() {
		return height;
	}

	public static float getSquareSize() {
		return squareSize;
	}

	public SimpleFightData getFightData() {
		return fightdata;
	}

	public void setOrganize(boolean organ) {
		organize = organ;
	}

	public SimpleBackpackData getBackpackdata() {
		return backpackdata;
	}

	public Room getActualRoom() {
		return actualroom;
	}

	public void resetActualRoom() {
		actualroom = dungeon.get(hero.floor()).getRoom(hero.roomj(), hero.roomi());
	}

	public Room getHomeRoom() {
		return home;
	}

	public boolean isPlacingCurse() {
		return placingCurse;
	}

	public void setPlacingCurse(boolean placingCurse) {
		this.placingCurse = placingCurse;

	}

	public void setPlacingCurse(boolean placingCurse, CurseRoom curseroom) {
		this.placingCurse = placingCurse;
		if (placingCurse) {
			actualroom = curseroom;
		}
	}

	public void setSelectedItem(Item item) {
		this.selected = item;
	}

	public static HashMap<String, ArrayList<Item>> getRarities() {
		return rarities;
	}

}