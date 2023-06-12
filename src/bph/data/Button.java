package bph.data;

import bph.items.Action;

public record Button(String legend, float xOrigin,float dimX,float yOrigin,float dimY, Action action) {
	public boolean isClicked(float x, float y) {
		if (x>xOrigin && x<xOrigin+dimX && y>yOrigin && y<yOrigin+dimY) {
			return true;
		}
		return false;
	}
	
	public Action getAction() {
		return action;
	}
}
