package bph.items;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import bph.map.Pair;

public class Sword implements MeleeWeapon, Stackable {

	private final String name, rarity, description;
	private final int price;
	private int uses,damage;
	private final UUID id;
	private final ArrayList<Cost> cost;
	private final ArrayList<InteractionAction> interaction;
	private final ArrayList<Pair> coord;

	public Sword(String name, int damage, ArrayList<Cost> cost, ArrayList<Pair> coord, String rarity, String description,
			int price) {
		this(UUID.randomUUID(), name, damage, cost, coord, 1, rarity, new ArrayList<>(), description, price);
	}

	public Sword(String name, int damage, ArrayList<Cost> cost, ArrayList<Pair> coord, int uses, String rarity,
			ArrayList<InteractionAction> interaction, String description, int price) {
		this(UUID.randomUUID(), name, damage, cost, coord, uses, rarity, interaction, description, price);
	}

	public Sword(UUID id, String name, int damage, ArrayList<Cost> cost, ArrayList<Pair> coord, int uses, String rarity,
			ArrayList<InteractionAction> interaction, String description, int price) {
		this.name = Objects.requireNonNull(name);
		this.cost = Objects.requireNonNull(cost);
		this.coord = Objects.requireNonNull(coord);
		this.damage = damage;
		this.id = Objects.requireNonNull(id);
		this.rarity = Objects.requireNonNull(rarity);
		this.interaction = Objects.requireNonNull(interaction);
		this.description = Objects.requireNonNull(description);
		this.price = price;
		this.uses = uses;
	}

	@Override
	public Item copy() {
		var copie = new ArrayList<Pair>();
		for (Pair coo : coord)
			copie.add(coo.copy());
		return new Sword(name, damage, cost, new ArrayList<>(copie), uses, rarity, interaction, description, price);
	}

	@Override
	public ArrayList<Pair> getCoordinates() {
		return coord;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<Cost> getCost() {
		return cost;
	}

	@Override
	public int getDamage() {
		return damage;
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
		return interaction;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public int getQuantity() {
		return uses;
	}

	@Override
	public void setQuantity(int quantity) {
		this.uses = quantity;
	}

	@Override
	public boolean isStackable() {
		return false;
	}

	@Override
	public void setDamage(int damage) {
		this.damage=damage;
	}
	
	@Override
	public void addInteractionAction(InteractionAction interaction) {
		Objects.requireNonNull(interaction);
		this.interaction.add(interaction);
	}
}
