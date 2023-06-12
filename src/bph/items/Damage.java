package bph.items;

import java.util.ArrayList;
import java.util.Arrays;

public interface Damage extends Item {
	int getDamage();
	@Override
	default ArrayList<Stringint> usethis() {
		return new ArrayList<Stringint>(Arrays.asList(new Stringint("attack",getDamage())));
	}
	void setDamage(int damage);
	default void updateDamage(int update){
		setDamage(getDamage()+update);
	}

}
