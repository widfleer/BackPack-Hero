package bph.items;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import bph.map.Pair;

public class Mana implements Item {
	private final int maxUses;
	private int uses;
	private final UUID id;
	private final String rarity;

	public Mana(int maxUses, String rarity) {
		this.maxUses = maxUses;
		this.uses = this.maxUses;
		this.id = UUID.randomUUID();
		this.rarity = Objects.requireNonNull(rarity);
	}

	public Mana(String rarity) {
		this(new Random().nextInt(1, 3), rarity);
	}

	@Override
	public Item copy() {
		return new Mana(rarity);
	}

	@Override
	public List<Pair> getCoordinates() {
		return Arrays.asList(new Pair(0, 0));
	}

	@Override
	public String getName() {
		return "Mana";
	}

	@Override
	public ArrayList<Cost> getCost() {
		return new ArrayList<Cost>(Arrays.asList(new Cost(0, "")));
	}

	public void recharge() {
		this.uses = maxUses;
	}

	public boolean use(int quantity) {
		if (uses - quantity < 0) {
			return false;
		}
		uses -= quantity;
		return true;
	}

	public int getUses() {
		return uses;
	}

	public int maxUses() {
		return maxUses;
	}

	@Override
	public ArrayList<Stringint> usethis() {
		return new ArrayList<Stringint>(Arrays.asList(new Stringint("", 0)));
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public String getrarity() {
		return rarity;
	}

	@Override
	public ArrayList<InteractionAction> getinteractionAction() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return "Frogs summon this resource\n"+"from other realms deep in\n"+"the depths of the dungeon";
	}

	@Override
	public int getPrice() {
		return 3;
	}
	
	@Override
	public boolean isConductive() {
		return true;
	}
}
