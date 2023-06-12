package bph.items;

public interface RangeWeapon extends Damage {
	@Override
	default boolean isProjectile() {
		return true;
	}
}
