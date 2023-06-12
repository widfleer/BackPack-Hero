package bph.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import bph.items.Clothing;
import bph.items.Consumable;
import bph.items.Cost;
import bph.items.Damage;
import bph.items.Dart;
import bph.items.Gold;
import bph.items.InteractionAction;
import bph.items.Item;
import bph.items.Mana;
import bph.items.Other;
import bph.items.Shield;
import bph.items.Stackable;
import bph.items.Stringint;
import bph.items.Sword;
import bph.items.Wand;
import bph.map.Pair;

public class SimpleItemData {

	public static HashMap<String, ArrayList<Item>> generateRarity() {
		var rarities = new HashMap<String, ArrayList<Item>>();
		var common = generateCommon();
		var uncommon = generateUncommon();
		var rare = generateRare();
		var legendary = new ArrayList<Item>();
		legendary.add(new Sword("Lizard King Sword", 25,
				new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"), new Cost(1, "use"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(2, 0))), 1, "Legendary",
				new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("each", "resetUse", "this", 1))),
				"Can be used 1 time each\n" + "turn. On use, Deals\n" + "25 damage", 35));

		rarities.put("Common", common);
		rarities.put("Uncommon", uncommon);
		rarities.put("Rare", rare);
		rarities.put("Legendary", legendary);
		return rarities;
	}

	private static ArrayList<Item> generateCommon() {
		var common = new ArrayList<Item>();
		common.add(new Shield("Rough Buckler", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(1, 1), new Pair(0, 1))),
				"Common", "On use, Adds 7 block", 3));
		common.add(new Clothing("Bronze Breastplate", 2, new ArrayList<Cost>(Arrays.asList(new Cost(0, ""))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(1, 1), new Pair(0, 1))),
				"Common",
				new ArrayList<InteractionAction>(
						Arrays.asList(new InteractionAction("startround", "add damage", "weapons adjacent", 1))),
				"Each turn, Adds 2 block\n" + "adjacent Weapons\n" + "get +1 damage", 3));
		common.add(new Sword("Wooden Sword", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(2, 0))), "Common",
				"On use, Deals 7 damage", 3));
		common.add(new Dart("Short Arrow", 2, "Common", "On use, Deals 1 damage\n" + " this item gets destroyed", 3));
		common.add(new Gold());
		common.add(new Mana("Common"));
		common.add(new Consumable("Meal", "Common", new ArrayList<Cost>(Arrays.asList(new Cost(1, "itself"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(0, 1))), 2,
				new ArrayList<Stringint>(Arrays.asList(new Stringint("addenergy", 2))),
				"2 uses\n" + "On use, Adds 2 Energy\n" + "this item gets destroyed", 6));
		common.add(new Sword("Cleaver", 4, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))), "Common", "On use, Deals 4 damage", 6));
		common.add(new Shield("Slats Shield", 8, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0))), "Common", "On use, Adds 8 block",
				4));
		common.add(new Sword("Shiv", 2, new ArrayList<Cost>(Arrays.asList(new Cost(1, "use"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))), 1, "Common",
				new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("each", "resetUse", "this", 1))),
				"Can be used 1 time each\n" + "turn. On use,\n" + "Deals 2 damage", 3));
		return common;
	}

	private static ArrayList<Item> generateUncommon() {
		var uncommon = new ArrayList<Item>();
		uncommon.add(new Wand("Electric Wand", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "mana"))),
				new ArrayList<Stringint>(Arrays.asList(new Stringint("attack", 7))), "Uncommon",
				"This item is conductive\n" + "On use, Deals 7 damage", 6));
		uncommon.add(new Other("Piggybank", "Uncommon", new ArrayList<Cost>(Arrays.asList(new Cost(0, ""))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))),
				new ArrayList<Stringint>(Arrays.asList(new Stringint("", 0))),
				new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("end", "add", "Gold", 2))),
				"Free money! And it smells \n " + "like bacon! +2 Gold \n" + "when combat ends", 8));
		uncommon.add(new Sword("Brutal Spear", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(2, 0))), 1, "Uncommon",
				new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("each", "reset", "this", 1),
						new InteractionAction("on use", "add", "damage", 1))),
				"On use, Deals 7 damage\n" + "this item gets +1\n" + "damage this turn", 4));
		uncommon.add(new Consumable("Cleansing Potion","Uncommon", new ArrayList<Cost>(Arrays.asList(new Cost(1, "itself"))),new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))),1,new ArrayList<Stringint>(Arrays.asList(new Stringint("reseteffect", 1))),
				"On use, Remove all\n" + "status effect\n"+"this item gets destroyed", 8));
		return uncommon;
	}

	private static ArrayList<Item> generateRare() {
		var rare = new ArrayList<Item>();
		rare.add(new Consumable("Rare Herb", "Rare", new ArrayList<Cost>(Arrays.asList(new Cost(1, "itself"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))), 1,
				new ArrayList<Stringint>(Arrays.asList(new Stringint("addmaxhp", 3))),
				"On use, Adds 3 Max HP\n" + "this item gets destroyed", 8));
		rare.add(new Consumable("Cave Shark", "Rare", new ArrayList<Cost>(Arrays.asList(new Cost(1, "itself"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))), 1,
				new ArrayList<Stringint>(Arrays.asList(new Stringint("addenergy", 3), new Stringint("heal", 5))),
				"On use, Adds 2 Energy\n" + "Adds 5 HP this item\n" + "gets destroyed", 10));
		rare.add(new Sword("Dueling Sword", 10, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy use"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(2, 0))), 1, "Rare",
				new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("each", "resetUse", "this", 1))),
				"Can be used 1 time each\n" + "turn. On use, Deals 10\n" + "damage.", 20));
		rare.add(new Sword("Needler", 7, new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))),
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(0, 1))), 1, "Rare",
				new ArrayList<InteractionAction>(
						Arrays.asList(new InteractionAction("on use", "add poison", "enemy", 2),
								new InteractionAction("on use", "add", "damage", -1),
								new InteractionAction("each", "reset", "this", 1))),
				"Deals 7 damage\n" + "Adds 2 poison to enemy\n" + "-1 Damage for this turn", 20));
		return rare;
	}

	public static void forInteraction(String time, SimpleGameData data) {
		for (Item item : data.getBackpack().getInventory()) {
			forItemInteraction(time, item, data);
		}
	}

	public static void forItemInteraction(String time, Item item, SimpleGameData data) {
		ArrayList<InteractionAction> interactions = new ArrayList<>(item.getinteractionAction());
		for (InteractionAction interaction : interactions) {
			if (interaction.timeofInteraction().equals(time)) {
				interactionProcess(item, interaction, data);
			}
		}
	}

	private static void interactionProcess(Item item, InteractionAction inter, SimpleGameData data) {
		interactionProcessEffect(item, inter, data);
		if (inter.on().equals("Gold")) {
			if (inter.what().equals("add")) {
				if (data.getBackpack().getGold() > 0) {
					data.getBackpack().updateStackable("Gold", inter.quantity());
				}
			}
		}
		if (inter.on().equals("pv")) {
			if (inter.what().equals("remove")) {
				data.getHero().takeUnprotectedHit(inter.quantity());
			}
		}
		interactionProcessThis(item, inter, data);
		if (inter.on().equals("damage")) {
			if (inter.what().equals("add")) {
				((Damage) item).updateDamage(inter.quantity());
			}
		}
		interactionProcessAround(item, inter, data);
	}

	private static void interactionProcessAround(Item item, InteractionAction inter, SimpleGameData data) {
		if (inter.on().contains("adjacent")) {
			for (Item itemaround : data.getBackpack().itemsAround("adjacent", item, false)) {
				if (inter.what().contains("add")) {
					if (inter.on().contains("weapons")) {
						if (inter.what().contains("damage")) {
							if (itemaround.isMeleeWeapon())
								((Damage) (itemaround)).updateDamage(inter.quantity());
							itemaround.addInteractionAction(new InteractionAction("each", "reset", "this"));
						}
					}
				}
			}
		}
	}

	private static void interactionProcessThis(Item item, InteractionAction inter, SimpleGameData data) {
		if (inter.on().equals("this")) {
			if (inter.what().equals("resetUse")) {
				((Stackable) item).setQuantity(inter.quantity());
			}
			if (inter.what().equals("reset")) {
				Item newitem = Item.getItemByNameAndRarity(item.getName(), item.getrarity(),
						SimpleGameData.getRarities());
				if(item.isQuantifiable()) ((Stackable) (item)).setQuantity(((Stackable)(newitem)).getQuantity());
				if(item.isMeleeWeapon()) ((Damage)(item)).setDamage(((Damage)(newitem)).getDamage());
			}
		}
	}

	private static void interactionProcessEffect(Item item, InteractionAction inter, SimpleGameData data) {
		var enemyeffects = (data.getEnemyinfight().isEmpty()) ? new HashMap<String,Integer>() : data.getEnemyinfight().get(data.getSelectedEnemy()).getEffects(); 
		var effects = (inter.on().contains("hero")) ? data.getHero().getEffects()
				: (inter.on().contains("enemy")) ? enemyeffects : new HashMap<String,Integer>();
		for (var effect : effects.keySet()) {
			if (inter.what().contains(effect)) {
				if (inter.what().contains("add")) {
					effects.put(effect, effects.get(effect) + inter.quantity());
				}
			}
		}

	}
}
