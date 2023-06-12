package bph.items;

import java.util.Objects;
import java.util.UUID;

import bph.map.Pair;

import java.util.ArrayList;

public record Shield(UUID id,String name, int protection, ArrayList<Cost> cost, ArrayList<Pair> coord, String rarity, ArrayList<InteractionAction> interaction, String description, int price) implements Protection {

	public Shield(String name, int protection, ArrayList<Cost> cost, ArrayList<Pair> coord, String rarity,String description, int price) {
		this(UUID.randomUUID(),name,protection,cost,coord,rarity, new ArrayList<>(),description, price);
	}
	
	public Shield(UUID id,String name, int protection, ArrayList<Cost> cost, ArrayList<Pair> coord, String rarity,ArrayList<InteractionAction> interaction, String description, int price) {
		this.interaction = Objects.requireNonNull(interaction);
		this.name = Objects.requireNonNull(name);
		this.cost = Objects.requireNonNull(cost);
		this.coord = Objects.requireNonNull(coord);
		this.protection = protection;
		this.id = Objects.requireNonNull(id);
		this.rarity = Objects.requireNonNull(rarity);
		this.description = Objects.requireNonNull(description);
		this.price = price;
	}
	

	@Override
	public Item copy() {
		var copie = new ArrayList<Pair>();
		for (Pair coo : coord) copie.add(coo.copy());
		return new Shield(name,protection,cost,new ArrayList<>(copie),rarity,description, price);
	}

	@Override
	public ArrayList<Cost> getCost() {
		return cost;
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
	public int getProtection() {
		return protection;
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
	public void addInteractionAction(InteractionAction interaction) {
		Objects.requireNonNull(interaction);
		this.interaction.add(interaction);
	}
}
