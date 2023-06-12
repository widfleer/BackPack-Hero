package bph.data;

import java.util.ArrayList;
import java.util.Objects;

import bph.character.Bestiary;
import bph.character.Characters;
import bph.character.Enemy;
import bph.character.Hero;
import bph.items.Armor;
import bph.items.Cost;
import bph.items.Curse;
import bph.items.Item;
import bph.items.Mana;
import bph.items.RepertoryCurse;
import bph.items.Stackable;
import bph.items.Stringint;
import bph.room.CurseRoom;

/**
 * This class represents combat data in the game.
 * 
 * @author HOUANGKEO COUTELLIER
 */
public class SimpleFightData {
	private boolean heroTurn;
	private ArrayList<Enemy> enemyinfight;
	private ArrayList<Stringint> predictions;
	private int selectedenemy;
	private final SimpleBackpackData backpackdata;
	private final Hero hero;
	private final SimpleGameData data;

	/**
	 * SimpleFightData class constructor.
	 *
	 * @param data The global game data.
	 * @param hero The hero participating in the fight.
	 */
	public SimpleFightData(SimpleGameData data, Hero hero) {
		this.data = Objects.requireNonNull(data);
		enemyinfight = new ArrayList<>();
		selectedenemy = 0;
		predictions = new ArrayList<Stringint>();
		this.backpackdata = data.getBackpackdata();
		this.hero = Objects.requireNonNull(hero);
	}

	/* FIGHT */

	/**
	 * Prepares for combat by initializing enemies, updating the hero's resources,
	 * applying initial effects and preparing the hero's turn.
	 *
	 * @param hero    The hero taking part in the fight.
	 * @param enemies Enemies present in the fight.
	 */
	void preparetoFight(Hero hero, ArrayList<Enemy> enemies) {
		Objects.requireNonNull(hero);
		Objects.requireNonNull(enemies);
		setEnemyinfight(enemies);
		updateMana();
		hero.putInFight();
		hero.resetEffects();
		predictions = enemiesPredictions();
		prepareHeroTurn();
	}

	/**
	 * Prepares the enemy's turn by applying the end effects of the hero's turn,
	 * executing the enemy's turn and preparing the next hero's turn.
	 *
	 * @return Boolean indicating whether the enemy's turn has been executed.
	 */
	public boolean prepareenemyturn() {
		if (!heroTurn) {
			applyEndEffect(hero);
			SimpleItemData.forInteraction("each", data);
			boolean enemyturn = enemyTurn();
			if (enemyturn) {
				predictions = enemiesPredictions();
				heroTurn = true;
			}
			return enemyturn;
		}
		return true;
	}

	private boolean enemyTurn() {
		if (enemyinfight.size() == 0 && !hero.isDead()) {
			return false;
		}
		for (var i = 0; i < predictions.size(); i++) {
			var enemy = enemyinfight.get(i);
			applyStartEffect(enemy);
			enemy.setProtection(0);
			var action = predictions.get(i);
			choosefight(action.choice(), action.value(), enemy, hero);
		}
		endEnemyTurn();
		prepareHeroTurn();
		return true;
	}

	/**
	 * Executes enemy turns, iterating over each enemy, applying start-of-turn
	 * effects, performing combat actions and managing the end of the enemy's turn.
	 *
	 * @return Boolean indicating whether the enemy turn has been executed.
	 */
	private void endEnemyTurn() {
		for (int i = 0; i < enemyinfight.size(); i++) {
			Enemy enemy = enemyinfight.get(i);
			applyEndEffect(enemy);
			if (enemy.isDead()) {
				enemyinfight.remove(i);
				enemyDeath(enemy);
				i--;
			}
		}
	}

	/**
	 * Prepares the hero's turn by updating the hero's resources.
	 */
	public void prepareHeroTurn() {
		hero.startRound();
		heroTurn = true;
		var armors = heroProtections();
		updateHeroProtection(armors);
		applyStartEffect(hero);
		SimpleItemData.forInteraction("startround", data);
	}

	/**
	 * Applies end-of-turn effects on the given target.
	 *
	 * @param target The target to be affected by end-of-turn effects.
	 */
	public void applyEndEffect(Characters target) {
		Objects.requireNonNull(target);
		var effects = target.getEffects();
		if (effects.get("poison") > 0) {
			if (effects.get("zombie") == 0) {
				target.takeUnprotectedHit(effects.get("poison"));
			} else {
				target.heal(effects.get("poison"));
			}
		}
		target.decayEffects();
	}

	/**
	 * Applies the start-of-turn effects on the given target.
	 *
	 * @param target The target to be affected by start-of-turn effects.
	 */
	public void applyStartEffect(Characters target) {
		Objects.requireNonNull(target);
		var effects = target.getEffects();
		if (effects.get("burn") > 0) {
			target.takeHit(effects.get("burn"));
		}
		if (effects.get("regen") > 0) {
			target.heal(effects.get("regen"));
		}
		if (effects.get("exhaust") > 0) {
			useCost("energy", effects.get("exhaust"));
		}
	}

	/**
	 * Uses an object selected during combat.
	 *
	 * @param selected The selected object to use.
	 */
	public void useObject(Item selected) {
		if (selected != null) {
			if (useCost(selected)) {
				calculateUseObjects(selected);
				SimpleItemData.forItemInteraction("on use", selected, data);
			}
		}
	}

	/**
	 * Calculates the actions to be performed when using the selected object.
	 *
	 * @param selected The selected object to use.
	 */
	private void calculateUseObjects(Item selected) {
		if(selectedenemy>=enemyinfight.size()) selectedenemy =0;
		Enemy enemy = enemyinfight.get(selectedenemy);
		for (var action : selected.usethis()) {

			if (action.choice().equals("destroy")) {
				data.getBackpack().remove(selected);
				return;
			}
			int debuff = 0;
			if (action.choice().equals("attack")) {
				if (!selected.isProjectile()) {
					if (enemy.getEffects().get("spikes") > 0) {
						choosefight("spikes", enemy.getEffects().get("spikes"), enemy, hero);
					}
				} else if (enemy.getEffects().get("rough hide") > 0) {
					debuff = action.value() / 2;
				}
			}
			choosefight(action.choice(), action.value() - debuff, hero, enemy);
		}
	}

	/**
	 * Selects the combat action to be performed according to the given choice.
	 *
	 * @param choice The choice of combat action.
	 * @param value  The value associated with the action.
	 * @param me     The character performing the action.
	 * @param you    The target of the action.
	 */
	public void choosefight(String choice, int value, Characters me, Characters you) {
		Objects.requireNonNull(choice);
		Objects.requireNonNull(me);
		Objects.requireNonNull(you);
		var effects = me.getEffects();
		if (effects.get("sleep") > 0 || choice.equals("pass"))
			return;
		switch (choice) {
		case "attack":
			doAttack(value + effects.get("rage") - effects.get("weak"), me, you);
			break;
		case "protect":
			me.protect(value + effects.get("haste") - effects.get("slow"));
			break;
		case "heal":
			me.heal(value);
			break;
		case "addenergy":
			((Hero) me).setEnergy(((Hero) me).getEnergy() + value);
			break;
		case "addmaxhp":
			((Hero) me).addMaxhealth(value);
			break;
		case "spikes":
			you.takeHit(value);
			break;
		case "reseteffect":
			me.resetEffects();
			break;
		case "":
			break;
		default:
			chooseSpecialFight(choice, value, me, you);
			break;
		}
	}

	/**
	 * Performs a special combat action according to the choice given.
	 *
	 * @param choice The choice of special combat action.
	 * @param value  The value associated with the action.
	 * @param me     The character performing the action.
	 * @param you    The target of the action.
	 */
	private void chooseSpecialFight(String choice, int value, Characters me, Characters you) {
		if (choice.contains("Curse")) {
			takeCurse(choice);
			return;
		}
		if (choice.contains("summon")) {
			var newchoice = choice.replace("summon ", "");
			enemyinfight.add(Bestiary.getEnemyByName(newchoice));
			return;
		}
		var effects = you.getEffects();
		var newchoice = choice.split(" ");
		if (newchoice[0].contains("rage") || newchoice[0].contains("regen")) {
			int add = newchoice[0].contains("regen") ? 1 : 0;
			me.getEffects().put(newchoice[0], me.getEffects().get(newchoice[0]) + Integer.decode(newchoice[1]) + add);
		} else {
			effects.put(newchoice[0], effects.get(newchoice[0]) + Integer.decode(newchoice[1]));
		}
		you.setEffects(effects);
	}

	private void takeCurse(String curse) {
		Curse curs = RepertoryCurse.getCurseByName(curse);
		data.setPlacingCurse(true, new CurseRoom(1, 2, curs));
		data.setSelectedItem(curs);
	}

	/**
	 * Apply an attack between two characters.
	 *
	 * @param value The value of the attack.
	 * @param me    The attacking character.
	 * @param you   The target character.
	 */
	private void doAttack(int value, Characters me, Characters you) {
		var lstaction = you.takeHit(value);
		for (Stringint action : lstaction) {
			choosefight(action.choice(), action.value(), you, me);
		}
		if (you.isDead()) {
			enemyDeath(you);
		}
	}

	/**
	 * Performs the necessary actions when an enemy dies.
	 *
	 * @param enemy The enemy who died.
	 */
	private void enemyDeath(Characters enemy) {
		int index = enemyinfight.indexOf(enemy);
		if (index != -1) {
			enemyinfight.remove(enemy);
			predictions.remove(index);
			this.selectedenemy = 0;
		}
	}

	/**
	 * Obtains predictions of enemy actions.
	 *
	 * @return Predictions of enemy actions.
	 */
	public ArrayList<Stringint> enemiesPredictions() {
		var predictions = new ArrayList<Stringint>();
		for (var enemy : enemyinfight) {
			predictions.add(enemy.choiceEnemy());
		}
		return predictions;
	}

	/**
	 * Checks and uses the costs required to use an object.
	 *
	 * @param item The object to use.
	 * @return true if the costs have been used successfully, otherwise false.
	 */
	public boolean useCost(Item item) {
		Objects.requireNonNull(item);
		var lstcost = item.getCost();
		for (Cost cost : lstcost) {
			if (cost.material().contains("itself") || cost.material().contains("use")) {
				if (((Stackable) item).updateQuantity(-cost.value())) {
					if (((Stackable) item).getQuantity() < 1 && cost.material().contains("itself")) {
						backpackdata.getBackpack().remove(item);
					}
				} else {
					return false;
				}
			} else if (cost.material().equals("mana")) {
				if (!useMana(item))
					return false;
			}
			if (!useCost(cost.material(), cost.value()))
				return false;
		}
		return true;
	}

	/**
	 * Checks and uses the mana cost required to use an object.
	 *
	 * @param item The object to use.
	 * @return true if the mana cost has been used successfully, otherwise false.
	 */
	private boolean useMana(Item item) {
		var itemsaround = backpackdata.getBackpack().itemsAround("around", item, true);
		int costing = getManaCost(item);
		var usedmana = new ArrayList<Mana>();
		for (Item itemaround : itemsaround) {
			if (itemaround.getName().equals("Mana")) {
				for (int i = costing; i > 0; i--) {
					if (((Mana) itemaround).use(i)) {
						costing -= i;
						if (costing == 0)
							return true;
						usedmana.add((Mana) itemaround);
					}
				}
			}
		}
		rechargeMana(usedmana);
		return false;
	}

	private int getManaCost(Item item) {
		int costing = 0;
		for (Cost cost : item.getCost()) {
			if (cost.material().equals("mana")) {
				costing = cost.value();
			}
		}
		return costing;
	}

	private void rechargeMana(ArrayList<Mana> manas) {
		for (Mana mana : manas) {
			mana.recharge();
		}
	}

	/**
	 * Checks and uses the cost specified for an action.
	 *
	 * @param material The material representing the cost.
	 * @param value    The value of the cost.
	 * @return true if the cost has been used successfully, otherwise false.
	 */
	public boolean useCost(String material, int value) {
		Objects.requireNonNull(material);
		if (material.contains("energy")) {
			return hero.useEnergy(value);
		}
		if (material.contains("gold")) {
			return backpackdata.getBackpack().updateStackable("Gold", -value);
		}
		return true;
	}

	/**
	 * Obtains the hero's armor protections.
	 *
	 * @return Hero's armor.
	 */
	public ArrayList<Armor> heroProtections() {
		var armors = new ArrayList<Armor>();
		for (var item : backpackdata.getBackpack().getInventory()) {
			if (item.isArmor()) {
				armors.add((Armor) item);
			}
		}
		return armors;
	}
	
	/**
 	* Updates the hero's protection according to the armor equipped.
 	*
 	* @param armors Armor equipped by the hero.
 	*/
	public void updateHeroProtection(ArrayList<Armor> armors) {
		Objects.requireNonNull(armors);
		var effects = hero.getEffects();
		if (!armors.isEmpty()) {
			for (var armor : armors) {
				if (armor.getProtection() > 0) {
					hero.protect(armor.getProtection() + effects.get("haste") - effects.get("slow"));
				}
			}
		}
	}
	
	/**
 	* Updates the mana in the backpack.
 	*/
	public void updateMana() {
		for (var item : backpackdata.getBackpack().getInventory()) {
			if (item.getName().equals("Mana")) {
				Mana item2 = (Mana) item;
				item2.recharge();
			}
		}
	}
	
	/**
 	* Updates the prediction of the enemies in fight.
 	*/
	public ArrayList<String> updatePrediction() {
		ArrayList<String> prediction = new ArrayList<String>();
		for (var action : predictions) {
			String val = (action.value() != 0) ? " for " + action.value() : "";
			prediction.add("I will " + action.choice() + val + " ! hehe");
		}
		return prediction;
	}
	
	/* SETTER AND GETTER */ 
	public void setEnemyinfight(ArrayList<Enemy> enemyinfight) {
		Objects.requireNonNull(enemyinfight);
		this.enemyinfight = enemyinfight;
	}

	public void setheroTurn(boolean heroturn) {
		this.heroTurn = heroturn;
	}

	public void setselectedenemy(int enemy) {
		this.selectedenemy = enemy;
	}

	public int getselectedenemy() {
		return selectedenemy;
	}

	public boolean getheroTurn() {
		return heroTurn;
	}

	public ArrayList<Enemy> getEnemyinfight() {
		return enemyinfight;
	}
}