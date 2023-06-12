package bph.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import bph.items.Stringint;

public interface Characters {
	void protect(int protection);

	default void attack(Characters enemy, int damage) {
		Objects.requireNonNull(enemy);
		enemy.takeHit(damage);
	};

	default boolean isDead() {
		return this.getPv() <= 0;
	}

	HashMap<String, Integer> getEffects();

	int getPv();

	int getProtection();

	int getXP();

	String getName();

	void heal(int healing);

	void setPv(int pv);

	void setProtection(int protection);

	public default void takeUnprotectedHit(int damage) {
		damage += getEffects().get("freeze");
		if (getPv() <= damage) {
			setPv(0);
		} else {
			setPv(getPv() - damage);
		}
	}

	public default List<Stringint> takeHit(int damage) {
		if (getEffects().get("dodge") == 0) {
			if (this.getProtection() > 0) {;
				if (this.getProtection() >= damage) {
					setProtection(getProtection() - damage);
				} else {
					var damageLeft = damage - this.getProtection();
					setProtection(0);
					takeUnprotectedHit(damageLeft);
				}
			} else {
				takeUnprotectedHit(damage);
			}
			return verifypossibility();
		} else {
			getEffects().put("dodge", getEffects().get("dodge") - 1);
			return new ArrayList<Stringint>();
		}
	}

	List<Stringint> verifypossibility();

	public void setEffects(HashMap<String, Integer> effects);

	default void resetEffects() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("haste", 0);
		map.put("rage", 0);
		map.put("slow", 0);
		map.put("weak", 0);
		map.put("spikes", 0);
		map.put("poison", 0);
		map.put("burn", 0);
		map.put("freeze", 0);
		map.put("dodge", 0);
		map.put("regen", 0);
		map.put("rough hide", 0);
		map.put("sleep", 0);
		map.put("zombie", 0);
		map.put("exhaust", 0);
		setEffects(map);
	}

	default void decayEffects() {
		var effects = getEffects();
		for (var effect : effects.keySet()) {
			if (!effect.equals("rough hide") && !effect.equals("zombie") && !effect.equals("exhaust")
					&& effects.get(effect) > 0) {
				effects.put(effect, effects.get(effect) - 1);
			}
		}
	}
}
