package bph.controller;

import java.awt.Color;
import java.util.Objects;

import bph.data.SimpleGameData;
import bph.view.SimpleGameView;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class SimpleGameController {

	private static boolean gameLoop(ApplicationContext context, SimpleGameView view, SimpleGameData data) {
		if (data.getHero().isDead() && !data.endGame()) {
			data.setInGame(false);
			data.setEndGame(true);
		}
		var event = context.pollOrWaitEvent(10);

		if (event == null) {
			return true;
		}
		var action = event.getAction();
		if (!data.isInGame()) {
			if ((data.endGame() || SimpleGameData.isHome()) && action == Action.KEY_PRESSED && event.getKey() == KeyboardKey.Q) {
				return false;
			} else if (action == Action.KEY_PRESSED) {
				String entry = event.getKey().toString();
				data.getHero().setName(entry);
			}
			else if (action == Action.POINTER_DOWN){
				var location = event.getLocation();
				data.handleclick(location.x, location.y,view);
			}
		} else {
			if (action == Action.KEY_PRESSED && event.getKey() == KeyboardKey.Q) {
				return false;
			}
			if(action == Action.KEY_PRESSED && event.getKey() == KeyboardKey.R){
				data.rotate();
			}
			if (action != Action.POINTER_DOWN) {
				return true;
			}
			var location = event.getLocation();
			
			data.handleclick(location.x, location.y,view);
		}
		
		SimpleGameView.draw(context, view, data);
		return true;

	}

	public static void BackpackHero(ApplicationContext context) {
		Objects.requireNonNull(context);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();

		var data = new SimpleGameData(width, height, (int) (width / 30));

		var view = new SimpleGameView(height, width);
		SimpleGameView.draw(context, view, data);

		while (true) {
			if (!gameLoop(context, view, data)) {
				System.out.println("Thank you for quitting!");
				context.exit(0);
				return;
			}
		}
	}

	public static void main(String[] args) {
		Application.run(Color.WHITE, SimpleGameController::BackpackHero);
	}

}
