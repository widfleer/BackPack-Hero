package bph.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import bph.items.Stringint;

public class Hero implements Characters {
	private String name;
	private int pv,energy,protection,xp,floor,roomi,roomj,xpjauge,level;
	private boolean inFight = false;
	private static int maxhealth = 40;
	private HashMap<String, Integer> effects;

	public Hero(String name) {
		this.name = Objects.requireNonNull(name);
		this.pv = 40;
		this.energy = 0;
		this.protection = 0;
		this.xp=0;
		this.floor = 0;
		this.roomi = 0;
		this.roomj = 0;
		this.level=1;
		xpjauge = 6;
		this.effects = new HashMap<>();
		resetEffects();
	}

	@Override
	public int getPv() {
		return pv;
	}

	@Override
	public int getProtection() {
		return protection;
	}
	
	public int getXP() {
		return xp;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void resetName() {
	    this.name = "";
	}
	
	public boolean setName(String entry) {
		if (name.length()>=12) {
			return false;
		}
		if (entry.length() != 1) {
	        return false;
	    }
		char ch = entry.charAt(0);
	    if (!Character.isLetter(ch)) {
	        return false;
	    }
	    this.name = name+Objects.requireNonNull(entry);
	    return true;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public boolean useEnergy(int energy) {
		if (this.energy - energy <0) {
			return false;
		}
		this.energy -= energy;
		return true;
	}

	public void setProtection(int protection) {
		this.protection = protection;
	}

	@Override
	public void protect(int protection) {
		this.protection += protection;
	}
	
	public boolean gainXp(int xp) {
		if (this.xp+xp >=xpjauge) {
			level+=1;
			this.xp = (this.xp+xp)-xpjauge;
			xpjauge+=(xpjauge/2);
			return true;	
		}
		this.xp+=xp;
		return false;
	}
	
	@Override
	public List<Stringint> verifypossibility() {
		var listpossible = new ArrayList<Stringint>();
		if (effects.get("spikes")!=null) listpossible.add(new Stringint("spikes",effects.get("spikes")));
		return listpossible;
	}

	public void heal(int healing) {
		if ((pv +healing) > maxhealth) {
			this.pv = maxhealth;
		}else {
		this.pv += healing;
		}
	}
	
	public void startRound() {
		this.setEnergy(3);
		this.setProtection(0);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, pv, energy, protection, xp);
	}

	@Override
	public boolean equals(Object obj) {
		Objects.requireNonNull(obj);
		return obj instanceof Hero hero && name.equals(hero.name) && pv == hero.pv && energy == hero.energy
				&& protection == hero.protection && xp == hero.xp;
	}

	@Override
	public String toString() {
		return name + ", pv=" + pv + ", énergie=" + energy + ", protection=" + protection + ", xp=" + xp;
	}

	public int floor() {
		return floor;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public int roomi() {
		return roomi;
	}

	public int roomj() {
		return roomj;
	}

	public void changeRoom(int i, int j) {
		roomi = i;
		roomj = j;
	}
	
	public void floorUp() {
		floor+=1;
	}

	public boolean isInFight() {
		return inFight;
	}

	public void putInFight() {
		this.inFight = true;
	}
	
	public void outOfFight(){
		this.inFight = false;
	}

	public int getMaxhealth() {
		return maxhealth;
	}
	
	public void addMaxhealth(int value) {
		maxhealth+=value;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public HashMap<String, Integer> getEffects() {
		return effects;
	}
	
	@Override
	public void setPv(int pv) {
		this.pv = pv;
	}
	
	@Override
	public void setEffects(HashMap<String, Integer> effects) {
		this.effects=effects;
	}

}