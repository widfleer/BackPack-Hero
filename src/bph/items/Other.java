package bph.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import bph.map.Pair;

public class Other implements Item {
	private final String name, rarity, description;
	private final UUID id;
	private final ArrayList<Cost> cost;
	private final ArrayList<InteractionAction> interaction;
	private final  ArrayList<Pair> coord;
	private final ArrayList<Stringint> use;
	private final int price;
	

	
	public Other(String name, String rarity, ArrayList<Cost> cost,ArrayList<Pair> coord,String description,int price) {
		this(name,rarity, cost,coord, (ArrayList<Stringint>)Arrays.asList(new Stringint("", 0)), new ArrayList<>(),description,price);
	}
	
	public Other(String name, String rarity, ArrayList<Cost> cost,ArrayList<Pair> coord, ArrayList<Stringint> use,String description,int price ) {
		this(name,rarity,cost,coord, use, new ArrayList<>(),description,price);
	}
	
	public Other(String name, String rarity, ArrayList<Cost> cost,ArrayList<Pair> coord, ArrayList<Stringint> use, ArrayList<InteractionAction> interaction,String description,int price) {
		this.name = Objects.requireNonNull(name);
		this.id=UUID.randomUUID();
		this.rarity = Objects.requireNonNull(rarity);
		this.cost = cost;
		this.interaction = Objects.requireNonNull(interaction);
		this.coord=Objects.requireNonNull(coord);
		this.use= Objects.requireNonNull(use);
		this.description = Objects.requireNonNull(description);
		this.price = price;
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
		return new Other(name, rarity, cost, new ArrayList<>(copie), use, interaction,description,price);
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
