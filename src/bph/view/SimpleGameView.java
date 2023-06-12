package bph.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import bph.character.Hero;
import bph.data.Button;
import bph.data.SimpleGameData;
import bph.items.BackPack;
import bph.items.Curse;
import bph.items.Item;
import bph.items.Mana;
import bph.items.Stackable;

import bph.map.Dungeon;
import bph.character.Characters;
import bph.character.Enemy;
import bph.map.Floor;
import bph.room.CurseRoom;
import fr.umlv.zen5.ApplicationContext;

/**
 * The SimpleGameView class deals with the display of the game the screen, and
 * with the interpretation of which zones were clicked on by the user.
 * 
 * @author HOUANGKEO COUTELLIER ft.vincent
 * @param xOrigin    Abscissa of the left-hand corner of the area displaying the
 *                   cells. Abscissas are counted from left to right.
 * @param yOrigin    Ordinate of the left-hand corner of the area displaying the
 *                   cells. Ordinates are counted from top to bottom.
 * @param height     Height of the (square) area displaying the cells.
 * @param width      Width of the (square) area displaying the cells.
 * @param squareSize Side of the (square) areas displaying individual cells / images.
 * 
 *
 */
public record SimpleGameView(float xOrigin, float yOrigin, float height, float width, int squareSize) {
	
	/**
	 * Create a new GameView
	 * 
	 * @param width      The width of the game.
	 * @param height     The height of the game.
	 * @return SimpleGameView
	 */
	public SimpleGameView(float height, float width) {
		this((float) (width * 0.25), 30, height, width, (int) width / 30);
	}

	private int indexFromRealCoord(float coord, float origin) {
		if (coord < origin) {
			return -1;
		}
		return (int) ((coord - origin) / squareSize);
	}

	public int lineFromY(float y) {
		return indexFromRealCoord(y, yOrigin);
	}

	public int columnFromX(float x) {
		return indexFromRealCoord(x, xOrigin);
	}

	private float realCoordFromIndex(int index, float origin) {
		return origin + index * squareSize;
	}

	private float xFromI(int i) {
		return realCoordFromIndex(i, xOrigin);
	}

	private float yFromJ(int j) {
		return realCoordFromIndex(j, yOrigin);
	}

	private void drawImage(Graphics2D graphics, BufferedImage image, float x, float y, float dimX, float dimY) {
		var width = image.getWidth();
		var height = image.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
		var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
				y + (dimY - scale * height) / 2);
		graphics.drawImage(image, transform, null);
	}
	/*
	 * private void drawCell(Graphics2D graphics, SimpleGameData data, int i, int j)
	 * { var x = xFromI(i); var y = yFromJ(j); var image = loader.image(data.id(i,
	 * j));
	 * 
	 * drawImage(graphics, image, x + 2, y + 2, squareSize - 4, squareSize - 4); }
	 */

	private void drawenemy(Graphics2D graphics, SimpleGameData data) {
		int i = 0;
		for (var enemy : data.getEnemyinfight()) {
			graphics.setFont(data.fontdialogue());
			graphics.setColor(Color.white);
			try (var input = Files.newInputStream(Path.of("data/" + enemy.getName() + ".png"))) {
				var image = ImageIO.read(input);
				drawImage(graphics, image, width / 2 + (squareSize * 3 * i), height * 2 / 3, (float) (squareSize * 2.5),
						(float) (squareSize * 2.5));

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			drawEnemyCharac(graphics,data,enemy,i);
			i++;
		}
	}
	
	private void drawEnemyCharac(Graphics2D graphics,SimpleGameData data, Enemy enemy, int i) {
		graphics.drawString(data.getPrediction(i), (float) (width / 2.5 + (squareSize * 5 * i)),
				(float) (height * 2 / 3 + squareSize * (3.5 + i * 0.5)));

		graphics.setColor(Color.GREEN);
		Font font = new Font("Arial", Font.BOLD, 24);
		graphics.setFont(font);
		graphics.drawString("HP : " + enemy.getPv() + " + " + enemy.getProtection(),
				(float) (width / 2 + (squareSize * 3 * i)), height * 2 / 3 - 10);
		if (i == data.getSelectedEnemy()) {
			graphics.setColor(Color.white);
			graphics.drawRect((int) (width / 2 + (squareSize * 3 * i)), (int) (height * 2 / 3),
					(int) (squareSize * 2.5), (int) (squareSize * 2.5));
		}
		drawEffects(graphics,data,enemy,(float)(width / 2 + (squareSize * 2.8 * i)));
	}

	private void drawButton(Graphics2D graphics, SimpleGameData data, Button button) {
		BufferedImage image;
		if (button.dimX() > button.dimY() * 4) {
			image = data.getBackgroundloader().image(3);
		} else {
			image = data.getBackgroundloader().image(7);
		}
		drawCenteredText(graphics,image,button.xOrigin(),button.yOrigin(),button.dimX(),button.dimY(),button.legend());
	}
	
	private void drawCenteredText(Graphics2D graphics, BufferedImage image, float x, float y, float dimX,float dimY,String legend) {
		var transform = new AffineTransform(dimX / image.getWidth(), 0, 0, dimY / image.getHeight(),
				x, y);
		graphics.drawImage(image, transform, null);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(legend);
		int textHeight = fontMetrics.getHeight();
		int textX = (int) (x + (dimX - textWidth) / 2);
		int textY = (int) (y + (dimY - textHeight) / 2 + fontMetrics.getAscent());
		graphics.drawString(legend, textX, textY);
	}

	private void drawActiveButton(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.WHITE);
		graphics.setFont(data.fontdialogue());
		if (data.getActualRoom() != null) {
			for (Button button : data.getActualRoom().getButtons()) {
				drawButton(graphics, data, button);
			}
		}
	}
	private void drawPlacingCurse(Graphics2D graphics, SimpleGameData data) {
		CurseRoom actual = (CurseRoom) data.getActualRoom();
		Curse curse = actual.getCurse();
		drawSelectedItem(graphics, data, curse, actual.getSelectj(), 0, actual.getSelecti(), actual.getSelectj(),
				actual.getSelecti());
		graphics.drawString(curse.getName(), (float) (width * 0.6), (float) (height * 0.1));
		try (var input = Files.newInputStream(Path.of("data" + "/" + curse.getName() + ".png"))) {
			var image = ImageIO.read(input);
			drawCurse(graphics,data,image,curse);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	private void drawCurse(Graphics2D graphics, SimpleGameData data, BufferedImage image, Curse curse) {
		var pairs = curse.getCoordinates();
		if (pairs.size()<2) {
			drawImage(graphics, image, xFromI(11), yFromJ(3), squareSize, squareSize);
		}else {
			int min_i = Integer.MAX_VALUE;
			int min_j = Integer.MAX_VALUE;
			int max_i = Integer.MIN_VALUE;
			int max_j = Integer.MIN_VALUE;
			for (var pair : pairs) {
				max_i = Math.max(max_i, pair.first());
				max_j = Math.max(max_j, pair.last());
				min_i = Math.min(min_i, pair.first());
				min_j = Math.min(min_j, pair.last());
			}
			drawImage(graphics, image, xFromI(min_j+11), yFromJ(min_i+3), xFromI(max_j+1)-xFromI(min_j),  yFromJ(max_i + 1) - yFromJ(min_i));
		}
	}

	private void drawFight(Graphics2D graphics, SimpleGameData data) {
		drawenemy(graphics, data);
		if (data.isPlacingCurse()) {
			drawPlacingCurse(graphics, data);
		}
	}

	private void drawevent(Graphics2D graphics, SimpleGameData data) {
		if (!data.isInGame()) {
			if (SimpleGameData.isHome()) {
				drawHome(graphics,data);
			}
			else if (data.endGame()) {
				drawHOF(graphics, data);
			} else {
				drawName(graphics, data);
			}
			return;
		}

		switch (data.getEventnumber()) {
		case 2:
			if (data.getHero().isInFight()) {
				drawFight(graphics, data);
			}
			break;
		case 3:
			drawpockets(graphics, data);
			break;
		case 4:
			drawhealer(graphics, data);
			break;
		case 5:
			drawtreasure(graphics, data);
			break;
		case 6:
			if (data.getHero().floor() == 3) {
				drawEnd(graphics, data);
			}
			break;
		}
		drawActiveButton(graphics, data);

	}

	private void drawtreasure(Graphics2D graphics, SimpleGameData data) {
		drawImage(graphics, data.getCharacterloader().image(3), width * 2 / 3, height * 2 / 3,
				(float) (squareSize * 2.5), (float) (squareSize * 2.5));

	}

	private void drawpockets(Graphics2D graphics, SimpleGameData data) {
		if (SimpleGameData.printMerchant()) {
			drawImage(graphics, data.getBackgroundloader().image(10), width * 1/2 -(squareSize * 5), height * 1/2- (squareSize * 2),
					(squareSize * 10), (squareSize * 10));
			drawImage(graphics, data.getCharacterloader().image(0), width * 2 / 3, height * 2 / 3,
					(float) (squareSize * 2.5), (float) (squareSize * 2.5));
		}

	}


	private void drawhealer(Graphics2D graphics, SimpleGameData data) {
		if (SimpleGameData.printHealer()) {
			drawImage(graphics, data.getBackgroundloader().image(8), width * 1/2 -(squareSize * 15/2), height * 1/2- (squareSize * 15/2),
					(squareSize * 15), (squareSize * 15));
			graphics.setColor(Color.white);
			graphics.setFont(new Font("Arial", Font.BOLD, 35));
			graphics.drawString("Mercurochrome, le pansement des héros !", (width * 1/2 -350), (height *5/16));
			drawImage(graphics, data.getCharacterloader().image(1), width * 2 / 3, height * 2 / 3,
					(float) (squareSize * 2.5), (float) (squareSize * 2.5));
		}
	}

	private void drawRoom(Graphics2D graphics, Floor floor, int i, int j, SimpleGameData data) {
		var x = xFromI(i);
		var y = yFromJ(j);
		var hero = data.getHero();
		var loader = data.getMaploader();
		drawImage(graphics, loader.background(), x + 2, y + 2, squareSize, squareSize);

		if (floor.typeRoom(i, j) > 1) {
			drawImage(graphics, loader.image(floor.typeRoom(i, j) - 1), x + 5, y + 7, squareSize - 10, squareSize - 10);
		}
		if (hero.roomi() == i && hero.roomj() == j) {
			drawImage(graphics, loader.image(0), x + 5, y + 7, squareSize - 10, squareSize - 10);
		}
		/*
		 * var image = loader.image(floor.id(i, j));
		 * 
		 * drawImage(graphics, image, x + 2, y + 2, squareSize - 4, squareSize - 4);
		 */
	}

	public void drawMap(Graphics2D graphics, SimpleGameData data) {
		Dungeon dungeon = SimpleGameData.getDungeon();
		Hero hero = data.getHero();
		drawImage(graphics,data.getMaploader().image(6),xFromI(0)-squareSize,yFromJ(0)-squareSize,squareSize*14,squareSize*7+yFromJ(0));
		for (int i = 0; i < dungeon.get(hero.floor()).columns(); i++) {
			for (int j = 0; j < dungeon.get(hero.floor()).lines(); j++) {

				if (dungeon.get(hero.floor()).typeRoom(i, j) != 0)
					drawRoom(graphics, dungeon.get(hero.floor()), i, j, data);
			}
		}
	}

	private void drawRotate(Graphics2D graphics, BufferedImage image, int rotation, float x, float y, float dimX,
			float dimY) {
		var width = image.getWidth();
		var height = image.getHeight();
		float test = 0;
		float test1 = 0;
		if (Math.abs(dimX - dimY) >= squareSize) {
			if (rotation % 2 != 0) {
				// Si la rotation est impaire, on échange la largeur et la hauteur de l'image
				int tmp = width;
				width = height;
				height = tmp;
			}
		}
		var scale = Math.min(dimX / width, dimY / height);
		for (int i = 0; i < rotation; i++)
			image = rotateImage(image);

		AffineTransform transform = new AffineTransform(scale, 0, 0, scale, (x + (dimX - scale * width) / 2) + test,
				(y + (dimY - scale * height) / 2) - test1);
		// transform.rotate(Math.toRadians(90 * rotation), image.getWidth()/2,
		// image.getHeight()/2);
		graphics.drawImage(image, transform, null);
	}

	BufferedImage rotateImage(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, image.getType());

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				newImage.setRGB(height - 1 - j, i, image.getRGB(i, j));

		return newImage;
	}

	public void drawItems(Graphics2D graphics, SimpleGameData data, boolean isBackpack) {
		var backpack = data.getBackpack();
		float addi = 0;
		if (!isBackpack) {
			addi = squareSize * (backpack.columns() + 2);
			backpack = data.getTrash();
		}
		for (var item : backpack.getInventory()) {
			drawItem(graphics, data, backpack, item, addi);
		}
	}

	private void drawItem(Graphics2D graphics, SimpleGameData data, BackPack backpack, Item item, float addi) {
		int min_i = Integer.MAX_VALUE;
		int min_j = Integer.MAX_VALUE;
		int max_i = Integer.MIN_VALUE;
		int max_j = Integer.MIN_VALUE;
		for (var cell : backpack.getCells(backpack.getIndex(item))) {
			max_i = Math.max(max_i, cell.i());
			max_j = Math.max(max_j, cell.j());
			min_i = Math.min(min_i, cell.i());
			min_j = Math.min(min_j, cell.j());
		}
		try (var input = Files.newInputStream(Path.of("data" + "/" + item.getName() + ".png"))) {
			var image = ImageIO.read(input);
			drawRotate(graphics, image, backpack.getRotation(item), xFromI(min_j) + addi, yFromJ(min_i),
					xFromI(max_j + 1) - xFromI(min_j), yFromJ(max_i + 1) - yFromJ(min_i));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		drawItemdetails(graphics, data, item, max_j, addi, max_i, min_j, min_i);
	}

	private void drawItemdetails(Graphics2D graphics, SimpleGameData data, Item item, int max_j, float addi, int max_i,
			int min_j, int min_i) {
		if (item.equals(data.getSelectedItem())) {
			drawSelectedItem(graphics, data, item, max_j, addi, max_i, min_j, min_i);
		}
		graphics.setColor(Color.white);
		graphics.setFont(data.fontdialogue());
		if (item.isStackable()) {
			Stackable item2 = (Stackable) item;
			graphics.drawString("" + item2.getQuantity(), xFromI(max_j + 1) + addi - (float) (squareSize * 0.1),
					yFromJ(max_i + 1));
		}
		if (item.getName().equals("Mana")) {
			Mana item2 = (Mana) item;
			graphics.drawString("" + item2.getUses() + "/" + item2.maxUses(),
					xFromI(max_j + 1) + addi - (float) (squareSize * 0.1), yFromJ(max_i + 1));
		}
	}

	private void drawSelectedItem(Graphics2D graphics, SimpleGameData data, Item item, int max_j, float addi, int max_i,
			int min_j, int min_i) {
		graphics.setColor(Color.PINK);
		graphics.drawRect((int) (xFromI(min_j) + addi), (int) yFromJ(min_i), (int) (xFromI(max_j + 1) - xFromI(min_j)),
				(int) (yFromJ(max_i + 1) - yFromJ(min_i)));
		graphics.setColor(Color.RED);
		graphics.drawRect((int) (xFromI(min_j) + addi), (int) yFromJ(min_i), (int) (xFromI(min_j + 1) - xFromI(min_j)),
				(int) (yFromJ(min_i + 1) - yFromJ(min_i)));
		if (data.getSelectedItem() != null) {
			drawCardItem(graphics, data, data.getSelectedItem());
			if (data.getEventnumber() == 3) {
				drawPriceItem(graphics, data, data.getSelectedItem());
			}
		}
	}

	public void drawBackpack(Graphics2D graphics, SimpleGameData data) {
		drawCenteredText(graphics, data.getBackgroundloader().image(9),(float)(xFromI(0)-squareSize*1.5),0,squareSize*(data.getBackpack().columns()+3),squareSize*(data.getBackpack().lines()+1)+yFromJ(0),"");
		for (int i = 0; i < data.getBackpack().columns(); i++) {
			for (int j = 0; j < data.getBackpack().lines(); j++) {
				if (data.getBackpack().getcase(j, i) == -1) {
					if (data.isLevelingup() >= 0) {
						drawImage(graphics, data.getBackgroundloader().image(1), xFromI(i), yFromJ(j), squareSize,
								squareSize);
					}
				} else if (data.getBackpack().getcase(j, i) != -2) {
					drawImage(graphics, data.getBackgroundloader().image(0), xFromI(i), yFromJ(j), squareSize,
							squareSize);
				}
			}
		}
		drawItems(graphics, data, true);
	}

	private void drawTrash(Graphics2D graphics, SimpleGameData data) {
		 float alpha = 0.7f;
		 AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		 graphics.setComposite(alphaComposite);

		 graphics.setColor(Color.decode("#2a2d30"));
		 graphics.fillRect((int) xFromI(data.getBackpack().columns() + 2), (int) yFromJ(0), squareSize*data.getTrash().columns(), squareSize*data.getTrash().lines());
		 graphics.setComposite(AlphaComposite.SrcOver);
		 
		drawItems(graphics, data, false);
	}

	private void drawleveling(Graphics2D graphics, SimpleGameData data) {
		if (data.isLevelingup() == -1 || SimpleGameData.isOrganizing()) {
			drawTrash(graphics, data);
		} else {
			graphics.setFont(data.fontdialogue());
			graphics.setColor(Color.white);
			graphics.drawString("Choose a slot to unlock !", (float) (width / 4 + (squareSize * 1.1)),
					(float) (height * 2.5 / 3));
		}
	}

	private void drawswitch(Graphics2D graphics, SimpleGameData data) {
		Path path;
		if (data.isBackpackOpen()) {
			path = Path.of("data" + "/" + "mapicon.png");
			drawBackpack(graphics, data);
			if (!data.getHero().isInFight() || SimpleGameData.isOrganizing()) {
				drawleveling(graphics, data);
			}
		} else {
			path = Path.of("data" + "/" + "backpackicon.png");
			drawMap(graphics, data);
		}
		try (var input = Files.newInputStream(path)) {
			graphics.setColor(Color.DARK_GRAY);
			graphics.fill(
					new Rectangle2D.Float((float) (width - squareSize * 1.5), 80, squareSize * 2, squareSize + 40));
			var image = ImageIO.read(input);
			drawImage(graphics, image, (float) (width - squareSize * 1.2), 100, squareSize - 10, squareSize - 10);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void drawHero(Graphics2D graphics, SimpleGameData data) {
		if ((!data.getTrash().isEmpty() && data.isBackpackOpen()) || SimpleGameData.isOrganizing()) {
			drawImage(graphics, data.getCharacterloader().image(2), width / 4, height * 2 / 3,
					(float) (squareSize * 2.5), (float) (squareSize * 2.5));
		} else {
			drawImage(graphics, data.getCharacterloader().background(), width / 4, height * 2 / 3,
					(float) (squareSize * 2.5), (float) (squareSize * 2.5));
		}
		drawCharac(graphics, data);
	}

	private void drawCharac(Graphics2D graphics, SimpleGameData data) {
		var hero = data.getHero();
		graphics.setColor(Color.GREEN);
		Font font = new Font("Arial", Font.BOLD, 24);
		graphics.setFont(font);
		double ratio = (double) hero.getPv()/hero.getMaxhealth();
		String color = (ratio>=0.7) ? "#00ff00" : (ratio<=0.3) ? "#ff2a00" : "#ffa200";
		graphics.setColor(Color.decode(color));
		graphics.fill(new Rectangle2D.Float((width* 1/ 4), (height * 2 / 3 - 25), (((squareSize*2)*(hero.getPv()))/(hero.getMaxhealth())), 20));
		graphics.setColor(Color.GREEN);
		graphics.drawString("HP : " + hero.getPv() + "/" + hero.getMaxhealth() + " + " + hero.getProtection(),
				(float) (width * (1.0 / 4)), height * 2 / 3 - 35);
			for (int i = 0; i < hero.getEnergy(); i++) {
				drawImage(graphics, data.getCharacterloader().image(4), (float) (width / 4 + (squareSize * 1.1 * i)), (float) (height * 2.5 / 3),
						squareSize - 10, squareSize - 10);
			}
			drawEffects(graphics,data,hero,(float)(width*(0.95/4)));
	}
	
	private void drawEffects(Graphics2D graphics, SimpleGameData data, Characters target,float x){
		graphics.setColor(Color.white);
		graphics.setFont(data.fontdialogue());
		var effects = target.getEffects();
		int count = 0;
		for (var effect : effects.keySet()){
			if (effects.get(effect)>0){
			try (var input = Files.newInputStream(Path.of("data/" + effect + ".png"))) {
				var image = ImageIO.read(input);
				drawCenteredText(graphics, image, x+squareSize*count, height * 2 / 3-120, (float) (squareSize /1.5), (float) (squareSize /1.5), effects.get(effect).toString());
				count++;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			}
		}
	}

	private void drawbackground(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.decode("#2C2238"));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));

		drawImage(graphics, data.getBackgroundloader().background(), 0, 0, width, height);

	}

	private void drawEnd(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.decode("#2C2238"));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.decode("#9d76b8"));
		graphics.fill(new Rectangle2D.Float((float) (width * 1 / 4), (float) (height * 1 / 4), (float) (width * 1 / 2),
				(float) (height * 1 / 2)));
		Font font = new Font("Arial", Font.BOLD, 50);
		graphics.setFont(font);
		graphics.setColor(Color.decode("#1f1f1f"));
		graphics.drawString("You have reached the end of the Dungeon :)", (float) (width * 1 / 2 - 520),
				height * 1 / 2 - 150);
		graphics.drawString("What a master!", (float) (width * 1 / 2 - 200), height * 1 / 2 - 50);
		graphics.drawString("Press q to exit", (float) (width * 1 / 2 - 200), height * 1 / 2 + 50);
		graphics.drawString("See you soon for new adventures little hero!", (float) (width * 1 / 2 - 520),
				height * 1 / 2 + 150);
		System.out.println("print");
	}

	private void drawHome(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.decode("#2C2238"));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		drawImage(graphics, data.getBackgroundloader().image(2), (float) ((width * 1 / 2) - (width * 1 / 4)),
				(float) ((height * 1 / 3) - (height / 6)), (float) (width / 2), (float) (height / 3));
		drawImage(graphics, data.getBackgroundloader().image(11), (float) ((width * 2.5/12) - (width * 1/6)),
				(float) ((height * 4.5/6) - (height / 6)), (float) (width *1/3), (float) (height / 3));
		drawImage(graphics, data.getBackgroundloader().image(12), (float) ((width * 4.8/6) - (width * 4/12)),
				(float) ((height * 2 / 3) - (height * 1.25/6)), (float) (width *4/6), (float) (height * 1.25/3));
		Font font = new Font("Arial", Font.BOLD, 50);
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		for (Button button : data.getHomeRoom().getButtons()) {
			drawButton(graphics, data, button);
		}
	}

	private void drawName(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.decode("#2C2238"));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		drawImage(graphics, data.getBackgroundloader().image(2), (float) ((width * 1 / 2) - (width * 1 / 4)),
				(float) ((height * 1 / 3) - (height / 6)), (float) (width / 2), (float) (height / 3));
		Font font = new Font("Arial", Font.BOLD, 50);
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);;
		graphics.drawString("ENTER YOUR NAME : ", (float) ((width * 1 / 2) - 260), (float) (height * 4.7 / 8));
		drawCenteredText(graphics,data.getBackgroundloader().image(3),(float) ((width * 1 / 2) - 245),
				(float) (height * 5 / 8), (float) (500), (float) (100),data.getHero().getName());
		for (Button button : data.getHomeRoom().getButtons()) {
			drawButton(graphics, data, button);
		}
	}

	private void drawHOF(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.decode("#2C2238"));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.WHITE);
		Font font = new Font("Arial", Font.BOLD, 100);
		graphics.setFont(font);
		graphics.drawString("HALL OF FAME", (float) ((width * 1 / 2) - 368), (float) (height * 1 / 8));
		Font font2 = new Font("Arial", Font.BOLD, 40);
		graphics.setFont(font2);
		graphics.setColor(Color.WHITE);
		graphics.drawString("Press q to quit", (float) ((width * 1 / 2) - 190), (float) (height * 7.5 / 8));
		graphics.drawString("You obtained a score of "+String.valueOf(data.getScore())+", "+data.getHero().getName(), (float) ((width * 1 / 2) - 340), (float) (height * 1.75 / 8));
		graphics.drawString("Our best players :", (float) ((width * 1 / 2) - 205), (float) (height * 2.5 / 8));
		drawImage(graphics, data.getBackgroundloader().image(4), (float) ((width * 1 / 2) - (squareSize*(11.5/2))),
				(float) (height * 5 / 8 - (squareSize*5)), (float) (squareSize*11), (float) (squareSize*10));
		graphics.setFont(new Font("Arial", Font.BOLD, 30));
		for (int i = 0; i < 3; i++) {
			double[] h1 = { 0, +(height/50), +(height/20) };
			double[] w = { 0, -(width*1/8), +(width*1/8) };
			double[] h2 = { -(height/25), +(height/50), +(2*height/30) };
			String name = (data.topScores().size() > i) ? data.topScores().get(i).getName() : "UNKNOWN";
			String xp = (data.topScores().size() > i) ? (Integer.toString(data.topScores().get(i).getScore())) : "?";
			graphics.setColor(Color.WHITE);
			graphics.drawString(name, (float) ((width * 1 / 2) - (name.length() * 110 / 7) + w[i]),
					(float) (height *3/4 + h1[i]));
			graphics.setColor(Color.BLACK);
			graphics.drawString(xp, (float) ((width * 1 / 2) - (xp.length() * 50 / 3) + w[i]),
					(float) (height *1/2 + h2[i]));
		}
	}

	public ArrayList<String> splitString(String input) {
		ArrayList<String> resultList = new ArrayList<>();
		if (input.contains("\n")) {
			String[] splitArray = input.split("\n");
			for (String str : splitArray) {
				resultList.add(str);
			}
		} else {
			resultList.add(input);
		}
		return resultList;
	}

	private void drawCardItem(Graphics2D graphics, SimpleGameData data, Item item) {
		graphics.setColor(Color.decode("#000000"));
		Font font = new Font("Arial", Font.BOLD, 20);
		graphics.setFont(font);
		drawCardCost(graphics,data,item);
		
		
		
		graphics.setColor(Color.WHITE);
		ArrayList<String> description = splitString(item.getDescription());
		graphics.drawString(item.getName(), (float) (150), (float) (90));
		
		for (int j = 0; j < description.size(); j++) {
			graphics.drawString(description.get(j), (float) (110), (float) (180 + j * 30));
		}
		graphics.setColor(Color.PINK);
		graphics.drawString(item.getrarity(), (float) (190), (float) (140));
	}
	
	private void drawCardCost(Graphics2D graphics, SimpleGameData data, Item item) {
		if (item.getCost().isEmpty()) return;
		int i = 5;
		int costing = item.getCost().get(0).value();
		boolean otherthanenergy=false;
		for (var cost : item.getCost()) {
			i = (cost.material().equals("mana")) ? 6 : i;
			costing = (cost.material().equals("mana")) ? cost.value() : costing;
			otherthanenergy = (!cost.material().equals("energy")) ? true : otherthanenergy;
		}
		drawImage(graphics, data.getBackgroundloader().image(i), (float) (50), (float) (50), (float) (400),
				(float) (300));
		if (otherthanenergy&& i!=6) {costing=0;}
		graphics.drawString(Integer.toString(costing), (float) (380), (float) (90));
	}

	private void drawPriceItem(Graphics2D graphics, SimpleGameData data, Item item) {
		graphics.setColor(Color.WHITE);
		Font font = new Font("Arial", Font.BOLD, 20);
		graphics.setFont(font);
		drawImage(graphics, data.getBackgroundloader().image(7), (float) (135), (float) (275), (float) (200), (float) (150));
		graphics.drawString("COST : "+Integer.toString(item.getPrice()), (float) (190), (float) (360));
	}

	private void draw(Graphics2D graphics, SimpleGameData data) {
		// drawHome(graphics, data);
		// drawName(graphics, data);
		drawbackground(graphics, data);
		drawswitch(graphics, data);
		drawHero(graphics, data);
		drawevent(graphics, data);
		//drawHOF2(graphics, data);
		// drawCardItem(graphics, data, sword);
		// drawPriceItem(graphics, data, sword);
	}

	/**
	 * Draws the game board from its data, using an existing
	 * {@code ApplicationContext}.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 * @param view    GameView on which to draw.
	 */
	public static void draw(ApplicationContext context, SimpleGameView view, SimpleGameData data) {
		context.renderFrame(graphics -> view.draw(graphics, data)); // do not modify
	}
}