package bph.items;

public interface MeleeWeapon extends Damage {
	@Override
	default boolean isMeleeWeapon(){
		return true;
	}
}
