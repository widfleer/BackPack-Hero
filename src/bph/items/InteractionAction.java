package bph.items;

import java.util.Objects;

public record InteractionAction(String timeofInteraction, String what, String on, int quantity) implements Action {
	
	public InteractionAction(String timeofInteraction, String what, String on, int quantity) {
		this.timeofInteraction = Objects.requireNonNull(timeofInteraction);
		this.what = Objects.requireNonNull(what);
		this.on = Objects.requireNonNull(on);
		this.quantity = quantity;
	}
	
	public InteractionAction(String timeofInteraction, String what, String on) {
		this(timeofInteraction,what,on,0);
	}
	
	public InteractionAction() {
		this("none","none","none",0);
	}
}
