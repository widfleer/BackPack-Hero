package bph.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import bph.map.Pair;

public class Dart implements RangeWeapon, Stackable {
	private final String name, rarity,description;
	private final int price;
	private int quantity,damage;
	private final UUID id;
	private final ArrayList<Cost> cost;
	private final ArrayList<InteractionAction> interaction;
	
	public Dart(String name, int damage, String rarity, String description, int price) {
		this(name,damage,rarity,(new Random()).nextInt(8)+1,new ArrayList<>(),description,price);
	}
	
	public Dart(String name, int damage, String rarity, int quantity,ArrayList<InteractionAction> interaction, String description, int price) {
		this.name = Objects.requireNonNull(name);
		this.damage = damage;
		this.id=UUID.randomUUID();
		this.rarity = Objects.requireNonNull(rarity);
		this.cost = new ArrayList<Cost>(Arrays.asList(new Cost(1, "itself")));
		this.quantity=quantity;
		this.interaction = Objects.requireNonNull(interaction);
		this.description = Objects.requireNonNull(description);
		this.price=price;
	}
	
	
	@Override
	public Item copy() {
		return new Dart(name,damage,rarity,description,price);
	}
	
	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public List<Pair> getCoordinates() {
		return Arrays.asList(new Pair(0, 0));
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
	public int getQuantity() {
		return quantity;
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
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public void setDamage(int damage) {
		this.damage=damage;
	}
	
	@Override
	public void addInteractionAction(InteractionAction interaction) {
		Objects.requireNonNull(interaction);
		this.interaction.add(interaction);
	}
	
}











































































































