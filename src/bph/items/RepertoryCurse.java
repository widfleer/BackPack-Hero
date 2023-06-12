package bph.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import bph.map.Pair;

public enum RepertoryCurse {
	BIGCURSE("Big Curse", new ArrayList<Cost>(Arrays.asList(new Cost(0, ""))),
			new ArrayList<Pair>(Arrays.asList(new Pair(0, 0), new Pair(1, 0), new Pair(1, 1), new Pair(0, 1))),
			new ArrayList<Stringint>(Arrays.asList(new Stringint("", 0))), new ArrayList<>(), "Hehehe"),
	SLIMECURSE("Slime Curse", new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))), new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))),
			new ArrayList<Stringint>(Arrays.asList(new Stringint("destroy", 1))), new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("end", "remove", "pv", 2))), "Each Turn, lose 2HP"),
	HONEYCURSE("Honey Curse", new ArrayList<Cost>(Arrays.asList(new Cost(1, "energy"))), new ArrayList<Pair>(Arrays.asList(new Pair(0, 0))),
			new ArrayList<Stringint>(Arrays.asList(new Stringint("destroy", 1))), new ArrayList<InteractionAction>(Arrays.asList(new InteractionAction("each", "add slow", "hero", 2))), "Hehe");

	private final String name, description;
	private final ArrayList<InteractionAction> interaction;
	private final ArrayList<Pair> coord;
	private final ArrayList<Stringint> usethis;
	private final ArrayList<Cost> cost;

	RepertoryCurse(String name, ArrayList<Cost> cost, ArrayList<Pair> coord, ArrayList<Stringint> usethis, ArrayList<InteractionAction> interaction,
			String description) {
		this.name = Objects.requireNonNull(name);
		this.coord = Objects.requireNonNull(coord);
		this.usethis = Objects.requireNonNull(usethis);
		this.interaction = Objects.requireNonNull(interaction);
		this.description = Objects.requireNonNull(description);
		this.cost = Objects.requireNonNull(cost);
	}

	public static Curse getCurseByName(String name) {
		Objects.requireNonNull(name);
		for (RepertoryCurse curse : RepertoryCurse.values()) {
			if (curse.getName().equals(name)) {
				return new Curse(curse.getName(), curse.getCost(), curse.getCoord(), curse.getUsethis(),
						curse.getInteraction(), curse.getDescription());
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public ArrayList<InteractionAction> getInteraction() {
		return interaction;
	}

	public ArrayList<Pair> getCoord() {
		return coord;
	}

	public ArrayList<Stringint> getUsethis() {
		return usethis;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<Cost> getCost() {
		return cost;
	}

}
