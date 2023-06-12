package bph.items;

import java.util.ArrayList;
import java.util.Arrays;

public interface Armor extends Protection {
	@Override
	default boolean isArmor(){
		return true;
	};
	
	@Override
	default ArrayList<Stringint> usethis() {
		return new ArrayList<Stringint>(Arrays.asList(new Stringint("",getProtection())));
	}
	
}
