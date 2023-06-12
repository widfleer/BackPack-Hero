package bph.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public enum Bestiary {

	 	SMALLRATWOLF("Small Ratwolf", 32, new int[] { 7, 9 }, new int[] { 14 }, 6, 1, new ArrayList<String>(Arrays.asList("attack", "protect"))),
	    RATWOLF("Ratwolf", 45, new int[] { 7, 9 }, new int[] { 13, 16 }, 6, 2, new ArrayList<String>(Arrays.asList("attack", "protect"))),
	    QUEENBEE("Queen Bee", 74, new int[] { 15 }, new int[] { }, 20, 3, new ArrayList<String>(Arrays.asList("attack", "summon Lil Bee", "poison 1"))),
	    FROGWIZARD("Frog Wizard", 45, new int[] { }, new int[] { }, 8, 2, new ArrayList<String>(Arrays.asList("poison 4", "Slime Curse", "slow 1"))),
	    LIVINGSHADOW("Living Shadow", 100, new int[] {10}, new int[] { }, 25, 3, new ArrayList<String>(Arrays.asList("pass", "Big Curse", "attack"))),
	    LILBEE("Lil Bee", 16, new int[] { 7, 14 }, new int[] { 14 }, 4, 1, new ArrayList<String>(Arrays.asList("attack", "Honey Curse", "on damage rage 1"))),
		COBRA("Cobra", 52, new int[] { 11, 14 }, new int[] {  }, 11, 2, new ArrayList<String>(Arrays.asList("attack", "burn 6", "poison 3"))),
		CROWBANDIT("Crow Bandit",40,new int[] { 6, 9 },new int[] {  },8,1, new ArrayList<String>(Arrays.asList("attack", "regen 2")));
	
	private String name;
	private int pv, xp, difficulty;
	private int[] attack, shield;
	private ArrayList<String> possibilities;

	Bestiary(String name, int pv, int[] attack, int[] shield, int xp, int difficulty, ArrayList<String> possibilities) {
		this.name = Objects.requireNonNull(name);
		this.pv = pv;
		this.attack = attack;
		this.shield = shield;
		this.xp = xp;
		this.difficulty = difficulty;
		this.possibilities = Objects.requireNonNull(possibilities);
	}
	
	public static Enemy getEnemyByName(String name) {
		Objects.requireNonNull(name);
	    for (Bestiary enemy : Bestiary.values()) {
	        if (enemy.getName().equals(name)) {
	            return new Enemy(enemy.getName(),enemy.pv(),enemy.attack(),enemy.shield(),enemy.xp(),enemy.difficulty(),enemy.possibilities());
	        }
	    }
	    return null; 
	}
	
	public String getName() {
		return name;
	}

	public int pv() {
		return pv;
	}

	public int xp() {
		return xp;
	}
	
	public ArrayList<String> possibilities() {
		return possibilities;
	}
	
	public int difficulty() {
		return difficulty;
	}

	public int[] attack() {
		return attack;
	}

	public int[] shield() {
		return shield;
	}
}
