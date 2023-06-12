package bph.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import bph.map.Pair;

public record Wand(UUID id, String name, int usevalue, ArrayList<Cost> cost, ArrayList<Pair> coord, ArrayList<Stringint> use, String rarity,
		ArrayList<InteractionAction> interaction, String description, int price) implements Magic {

	public Wand(String name, int usevalue, ArrayList<Cost> cost, ArrayList<Stringint> use, String rarity, String description, int price) {
		this(UUID.randomUUID(), name, usevalue, cost,
				new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 1))), use, rarity,
				new ArrayList<>(),description,price);
	}

	public Wand(String name, int usevalue, ArrayList<Cost> cost, ArrayList<Pair> coord, ArrayList<Stringint> use, String rarity,
			ArrayList<InteractionAction> interaction, String description, int price) {
		this(UUID.randomUUID(), name, usevalue, cost, coord, use, rarity, interaction, description,price);
	}

	@Override
	public List<Pair> getCoordinates() {
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
	public ArrayList<Stringint> usethis() {
		return use;
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
	public Item copy() {
		var copie = new ArrayList<Pair>();
		for (Pair coo : coord) copie.add(coo.copy());
		return new Wand(name, usevalue, cost, new ArrayList<>(copie), use, rarity, interaction, description,price);
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
