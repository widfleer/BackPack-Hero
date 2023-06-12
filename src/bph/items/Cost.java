package bph.items;

import java.util.Objects;

public record Cost(int value, String material) {
	public Cost(int value, String material) {
		this.material = Objects.requireNonNull(material);
		this.value = value;
	}
}
