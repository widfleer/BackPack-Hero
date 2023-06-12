package bph.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import bph.items.Stringint;
import bph.map.Roomable;

public class Enemy implements Characters, Roomable {
	private final String name;
	private int pv;
	private final int[] attack;
	private final int[] shield;
	private int protection;
	private final int xp, difficulty, maxpv;
	private final ArrayList<String> possibilities;
	private HashMap<String, Integer> effects;

	public Enemy(String name, int pv, int[] attack, int[] shield, int xp, int difficulty,
			ArrayList<String> possibilities) {
		this.name = Objects.requireNonNull(name);
		this.pv = this.maxpv = pv;
		this.protection = 0;
		this.attack = attack;
		this.shield = shield;
		this.xp = xp;
		this.difficulty = difficulty;
		this.possibilities = Objects.requireNonNull(possibilities);
		this.effects = new HashMap<>();
		resetEffects();
	}

	public static Enemy createEnemy(String name) {
		return Bestiary.getEnemyByName(name);
	}

	public String choiceEnemyAction() {
		Random rand = new Random();
		int randomNumber = rand.nextInt(possibilities.size());
		String action = possibilities.get(randomNumber);
		while (action.contains("on damage")) {
			action = possibilities.get(rand.nextInt(possibilities.size()));
		}
		return action;
	}

	public int choiceEnemyValue(String random) {
		Random rand = new Random();
		int randomNumber = 0;
		if (random.equals("attack")) {
			var len = getAttack().length;
			if (len == 1) {
				randomNumber = getAttack()[0];
			} else if (len > 0) {
				randomNumber = rand.nextInt(getAttack()[len - 1] + 1 - getAttack()[0]) + getAttack()[0];
			}
		} else if (random.equals("protect")) {
			var len = getShield().length;
			if (len == 1) {
				randomNumber = getShield()[0];
			} else if (len > 0) {
				randomNumber = rand.nextInt(getShield()[len - 1] + 1 - getShield()[0]) + getShield()[0];
			}
		} else if (random.equals("rage")) return 1;
		return randomNumber;
	}

	public Stringint choiceEnemy() {
		var random = choiceEnemyAction();
		return new Stringint(random, choiceEnemyValue(random));
	}

	@Override
	public int getPv() {
		return pv;
	}

	public int[] getAttack() {
		return attack;
	}

	public int[] getShield() {
		return shield;
	}

	@Override
	public int getProtection() {
		return protection;
	}

	@Override
	public void protect(int protection) {
		this.protection += protection;
	}
	@Override
	public List<Stringint> verifypossibility() {
		var listpossible = new ArrayList<Stringint>();
		for (String possibility : possibilities) {
			if (possibility.contains("on damage")) {
				String possible = possibility.replaceFirst("on damage ", "");
				listpossible.add(new Stringint(possible,choiceEnemyValue(possible)));
			}
		}
		return listpossible;
	}

	@Override
	public String toString() {
		return name + ", pv=" + pv + ", protection=" + protection;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getXP() {
		return xp;
	}

	@Override
	public void heal(int healing) {
		if ((pv + healing) > maxpv) {
			this.pv = maxpv;
		} else {
			this.pv += healing;
		}
	}

	@Override
	public HashMap<String, Integer> getEffects() {
		return effects;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public ArrayList<String> getPossibilities() {
		return possibilities;
	}

	@Override
	public void setPv(int pv) {
		this.pv = pv;
	}

	@Override
	public void setProtection(int protection) {
		this.protection = protection;
	}

	@Override
	public void setEffects(HashMap<String, Integer> effects) {
		this.effects=effects;
	}

}
