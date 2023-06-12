package bph.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import bph.map.Pair;

public class Curse implements Item, Stackable {
	private final UUID id = UUID.randomUUID();
	private int uses = 1;
	private final String name, description;
	private final ArrayList<InteractionAction> interaction;
	private final ArrayList<Pair> coord;
	private static int damage = 2;
	private final ArrayList<Stringint> usethis;
	private final ArrayList<Cost> cost;

	public Curse(String name, ArrayList<Cost> cost, ArrayList<Pair> coord, ArrayList<Stringint> usethis, ArrayList<InteractionAction> interaction,
			String description) {
		this.name = Objects.requireNonNull(name);
		this.coord = Objects.requireNonNull(coord);
		this.usethis = Objects.requireNonNull(usethis);
		this.interaction = Objects.requireNonNull(interaction);
		this.description = Objects.requireNonNull(description);
		this.cost = Objects.requireNonNull(cost);
	}

	@Override
	public List<Pair> getCoordinates() {
		return coord;
	}

	public static ArrayList<Pair> generateCoord() {
		var lstcoord = new ArrayList<Pair>();
		lstcoord.add(new Pair(1, 1));
		var rand = new Random();
		for (int i = 0; i < 3; i++) {
			var pair = new Pair(rand.nextInt(2), rand.nextInt(2));
			if (!lstcoord.contains(pair)) {
				lstcoord.add(pair);
			}
		}
		return lstcoord;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getPrice() {
		return -999;
	}

	@Override
	public ArrayList<Cost> getCost() {
		return cost;
	}

	@Override
	public ArrayList<Stringint> usethis() {
		return usethis;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public String getrarity() {
		return "Common";
	}

	@Override
	public Item copy() {
		var copie = new ArrayList<Pair>();
		for (Pair coo : coord)
			copie.add(coo.copy());
		return new Curse(name, cost, new ArrayList<>(copie), usethis, interaction, description);
	}

	@Override
	public ArrayList<InteractionAction> getinteractionAction() {
		return interaction;
	}

	@Override
	public boolean rotate() {
		return false;
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public int getQuantity() {
		return uses;
	}

	@Override
	public void setQuantity(int quantity) {
		this.uses = 0;
	}

	@Override
	public boolean isStackable() {
		return false;
	}

	public int getDamage() {
		return damage;
	}

	public static void incrementDamage() {
		damage += 1;
	}
	
	@Override
	public void addInteractionAction(InteractionAction interaction) {
		Objects.requireNonNull(interaction);
		this.interaction.add(interaction);
	}
}
