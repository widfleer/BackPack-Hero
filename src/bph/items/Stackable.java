package bph.items;

public interface Stackable extends Item{
	@Override
	default boolean isStackable(){
		return true;
	};
	
	@Override
	default boolean isQuantifiable() {
		return true;
	}
	
	int getQuantity();
	void setQuantity(int quantity);
	default boolean updateQuantity(int update) {
			if (getQuantity() + update < 0) {
				return false;
			}
			setQuantity (getQuantity()+update);
			return true;
		
	}
}
