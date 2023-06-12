package bph.items;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import bph.map.Pair;

public class Gold implements Item, Stackable {
	private int quantity;
	private final UUID id = UUID.randomUUID();
	
	public Gold(int quantity) {
		this.quantity=quantity;
	}
	
	
	public Gold() {
		this((new Random().nextInt(4)+1));
	}


	@Override
	public Item copy() {
		return new Gold();
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public List<Pair> getCoordinates() {
		return Arrays.asList(new Pair(0, 0));
	}

	@Override
	public String getName() {
		return "Gold";
	}

	@Override
	public ArrayList<Cost> getCost() {
		return new ArrayList<Cost>(Arrays.asList(new Cost(0, "")));
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
		return "Common";
	}
	
	@Override
	public ArrayList<InteractionAction> getinteractionAction() {
		return new ArrayList<>();
	}


	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	@Override
	public String getDescription() {
		return "The root of all evil.\n"+"But also very shiny";
	}


	@Override
	public int getPrice() {
		return 1;
	}
}
