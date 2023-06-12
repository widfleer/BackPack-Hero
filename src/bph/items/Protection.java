package bph.items;

import java.util.Arrays;
import java.util.ArrayList;

public interface Protection extends Item {
	int getProtection();
	@Override
	default ArrayList<Stringint> usethis() {
		return new ArrayList<Stringint>(Arrays.asList(new Stringint("protect",getProtection())));
	}
}
