package fr.uge.projet.graphique;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


import java.util.Map;
import java.util.Objects;

import fr.uge.projet.interaction.Elements;
import fr.uge.projet.interaction.Grid;
import fr.uge.projet.intermediaire.Direction;
import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.inventoryitem.AllItems;
import fr.uge.projet.obstacle.AllObstacles;
import fr.uge.projet.personnage.AllPersonnages;
import fr.uge.projet.personnage.Player;
import fr.umlv.zen5.ApplicationContext;

public class Affichage {

	private final int width, height;
	private final double screenWidth, screenHeight;
	private final Map<String, BufferedImage> mapImage;
	private final ApplicationContext context;

	public Affichage(int height, int width, Map<String, BufferedImage> mapImages, ApplicationContext context) {
		Objects.requireNonNull(mapImages);
		if(height <= 0) {
			throw new IllegalArgumentException("La hauteur est inferieur à 0");
		}
		if(width <= 0) {
			throw new IllegalArgumentException("La largeur est inferieur à 0");
		}
		this.height = height;
		this.width = width;
		this.screenWidth = context.getScreenInfo().getWidth();
		this.screenHeight = context.getScreenInfo().getHeight();
		this.context = context;
		this.mapImage = mapImages;
	}



	/**
	 * This method display the player at his position with his health.
	 * @param context The graphics data of the application.
	 * @param play The player's information.
	 */
	private void playerMove(Graphics2D context, Player play) {
		var imag = mapImage.get(play.skin() + "-" + play.getDirection());
		if (imag != null) {
			var i = play.getPosition().getX();
			var j = play.getPosition().getY();
			context.drawImage(imag, (int) (j * screenWidth / width), (int) (i * screenHeight / height),
					(int) screenWidth / width, (int) screenHeight / height, null);
		}
		drawHP(context, play.getHealth(), play.getPosition(), play.getDirection());
	}

	/**
	 * This method display the item in the player's at his position.
	 * @param context The graphics data of the application.
	 * @param play The player's information.
	 */
	private void displayHand(Graphics2D context, Player player) {
		if (player.getInHand() != null) {
			var invent = player.getInHand();
			Image imag = null;
			if (!invent.skin().equals("BOLT")) {
				imag = mapImage.get(invent.skin() + "-" + player.getDirection());
			} else {
				imag = mapImage.get(invent.skin());
			}

			if (imag != null) {
				var i = invent.position().getX();
				var j = invent.position().getY();
				context.drawImage(imag, (int) (j * screenWidth / width), (int) (i * screenHeight / height),
						(int) screenWidth / width, (int) screenHeight / height, null);
			}
			player.getInHand().position().setY(-1);
			player.getInHand().position().setX(-1);
		}
	}

	/**
	 * This method display the health bar of a personage.
	 * @param graphics The graphics data of the application.
	 * @param hp The health of the personage.
	 * @param pos The position of the personage.
	 * @param direc The direction of the personage.
	 */
	private void drawHP(Graphics2D graphics, int hp, Position pos, Direction direc) {		
		if(hp >= 10) graphics.setColor(Color.GREEN);
		else if(hp >= 5) graphics.setColor(Color.YELLOW);
		else graphics.setColor(Color.RED);
		switch (direc) {
		case North:
			graphics.fillRect((int) (pos.getY() * screenWidth / width), (int) ((pos.getX()) * screenHeight / height), 1, hp);
			break;
		case South:
			graphics.fillRect((int) ((pos.getY() + 1) * screenWidth / width), (int) ((pos.getX()) * screenHeight / height), 1,
					hp);
			break;
		case West:
			graphics.fillRect((int) ((pos.getY()) * screenWidth / width), (int) ((pos.getX()) * screenHeight / height), hp,
					1);
			break;
		case East:
			graphics.fillRect((int) ((pos.getY()) * screenWidth / width), (int) ((pos.getX()) * screenHeight / height), hp,
					1);
			break;
		default:
			break;
		}
	}


	/**
	 * This method display all the elements present on the map.
	 * @param context The graphics data of the application.
	 */
	private void displayElement(Graphics2D context, AllObstacles obstacles) {
		var ens = obstacles.getListObstacles();
		for (var elem : ens) {
			var imag = mapImage.get(elem.skin());
			if (imag != null) {
				var i = elem.position().getX();
				var j = elem.position().getY();
				context.drawImage(imag, (int) (i * screenWidth / width), (int) (j * screenHeight / height),
						(int) screenWidth / width, (int) screenHeight / height, null);
			}
		}

	}

	/**
	 * This method display all the personages present on the map.
	 * @param context The graphics data of the application.
	 */
	private void displayCaracter(Graphics2D context, AllPersonnages persos) {
		var ens = persos.getAllPerso();
		for (var elem : ens) {
			var imag = mapImage.get(elem.skin());
			if (imag != null && !elem.isPlayer()) {
				var i = elem.position().getX();
				var j = elem.position().getY();
				context.drawImage(imag, (int) (i * screenWidth / width), (int) (j * screenHeight / height),
						(int) screenWidth / width, (int) screenHeight / height, null);
				context.setColor(Color.BLACK);
				context.drawString(elem.getName(), (int) (i * screenWidth / width), (int) ((j + 1.0) * screenHeight / height));
			}
		}
	}

	/**
	 * This method display all the items present on the map.
	 * @param context The graphics data of the application.
	 */
	private void displayItem(Graphics2D context, AllItems items) {
		var ens = items.getAllItems();
		for (var elem : ens) {
			var imag = mapImage.get(elem.skin());
			if (imag != null) {
				var i = elem.position().getX();
				var j = elem.position().getY();
				context.drawImage(imag, (int) (i * screenWidth / width), (int) (j * screenHeight / height),
						(int) screenWidth / width, (int) screenHeight / height, null);
			}
		}
	}


	/**
	 * This method display all the element of the grid.
	 * @param context The graphics data of the application.
	 */
	private void field(Graphics2D context, Grid grid) {
		var posElem = grid.getMapElemGrill();
		for (var entry : posElem.entrySet()) {
			var pos = entry.getKey();
			int i = pos.getY(), j = pos.getX();
			var imag = mapImage.get(entry.getValue());
			if (imag != null) {
				context.drawImage(imag, (int) (j * screenWidth / width), (int) (i * screenHeight / height),
						(int) screenWidth / width, (int) screenHeight / height, null);
			} else {
				throw new IllegalArgumentException("Pas d'image");
			}
		}
	}

	/**
	 * This method display the player's inventory when AfficheInventory is true.
	 * @param context The graphics data of the application.
	 * @param play The player's data.
	 */
	private void displayInventaire(Graphics2D context, Player player) {
		if (!player.isAfficheInventory())
			return;
		context.setColor(Color.DARK_GRAY);
		context.fillRect((int) ((player.getPosition().getY() - 2) * (screenWidth / width)),
				(int) ((player.getPosition().getX() - 1) * (screenHeight / height)), (int) screenWidth / width * 5,
				(int) screenHeight / height * 2);
		context.setColor(Color.WHITE);
		var tmp = 0;
		for (int i = -1; i < 1; i++) {
			for (int j = -2; j < 3; j++) {
				context.fillRect((int) ((player.getPosition().getY() + j) * (screenWidth / width)) + 2,
						(int) ((player.getPosition().getX() + i) * (screenHeight / height)) + 2, (int) screenWidth / width - 6,
						(int) screenHeight / height - 6);
				if (player.getInventory().size() > tmp) {
					var imag = mapImage.get(player.getInventory().get(tmp).skin());
					if (imag != null)
						drawInventoryItem(context, player.getPosInventory(),  player.getPosition().getX() + i, player.getPosition().getY() + j, tmp++, imag);
				}
			}
		}
	}

	/**
	 * This method display an element present in the inventory with a yellow rectangle if the item is selectionned at the moment.
	 * @param context The graphics data of the application.
	 * @param x The coordinate x where the picture will be drawn.
	 * @param y The coordinate y where the picture will be drawn.
	 * @param tmp The position of the element to be drawn.
	 * @param imag The image to be drawn.
	 */
	private void drawInventoryItem(Graphics2D context, int posInventory, int x, int y, int tmp, Image imag) {
		context.drawImage(imag, (int) (y * (screenWidth / width)) + 2,
				(int) (x * (screenHeight / height)) + 2, (int) screenWidth / width - 6,
				(int) screenHeight / height - 6, null);
		if (tmp == posInventory) {
			context.setColor(Color.YELLOW);
			context.drawRect((int) (y * (screenWidth / width)) + 2,
					(int) (x * (screenHeight / height)) + 2, (int) screenWidth / width - 6,
					(int) screenHeight / height - 6);
			context.setColor(Color.WHITE);
		}
	}

	/**
	 * This method display the text and the trade if the player is talking with a friend and if he can trade.
	 * @param context The graphics data of the application.
	 * @param x The coordinate x of the center of the displayed screen.
	 * @param y The coordinate y of the center of the displayed screen.
	 */
	private void displayText(Graphics2D context, Player player, int x, int y) {
		if (!player.isTalkingWith())
			return;
		drawTextBox(context, player, x, y);
		var trading = player.getTrading();
		if (trading != null) {
			int i = 0, j = 0;
			context.setColor(Color.DARK_GRAY);
			for (var s : trading.keySet()) {
				var imag = mapImage.get(s);
				var img = mapImage.get("Fleche");
				for (var t : trading.get(s)) {
					var imag2 = mapImage.get(t.split(" ")[0]);
					if (imag != null && img != null && imag2 != null) {
						drawTrade(context, player.getPosInventory(), x - 1 + i % 2, y + 4 * j, i++, imag, img, imag2);
						if (i % 2 == 0)
							j++;
					}
				}
			}
			player.setNbtrade(i);
		}
	}

	/**
	 * This method draw a box where the text will be displayed.
	 * @param context The graphics data of the application.
	 * @param x The coordinate x of the center of the displayed screen.
	 * @param y The coordinate y of the center of the displayed screen.
	 */
	private void drawTextBox(Graphics2D context, Player player, int x, int y) {
		context.setColor(Color.WHITE);
		context.fillRect((int) ((y - 10) * (screenWidth / width)), (int) ((x + 1) * (screenHeight / height)),
				(int) screenWidth / width * 30, (int) screenHeight / height * 4);
		context.setColor(Color.BLACK);
		context.drawString(player.GetTalkingWith(), (int) ((y - 6) * screenWidth / width), (int) ((x + 1.25) * screenHeight / height));
	}

	/**
	 * This method draw the item that can be traded.
	 * @param context The graphics data of the application.
	 * @param x The coordinate x where the trade will be displayed.
	 * @param y The coordinate y where the trade will be displayed.
	 * @param i The position of the element to be drawn.
	 * @param imag1 The picture of the element that need to be given for the trade.
	 * @param imag2 The picture of an arrow.
	 * @param imag3 The picture of the element that the player can get.
	 */
	private void drawTrade(Graphics2D context, int posInventory, int x, int y, int i, Image imag1, Image imag2, Image imag3) {
		context.fillRect((int) ((y - 5) * (screenWidth / width)), (int) (x * (screenHeight / height)), 
				(int) screenWidth / width * 3, (int) screenHeight / height);
		context.drawImage(imag1, (int) ((y - 5) * screenWidth / width), (int) (x * screenHeight / height), 
				(int) screenWidth / width, (int) screenHeight / height, null);
		context.drawImage(imag2, (int) ((y - 4) * screenWidth / width), (int) (x * screenHeight / height), 
				(int) screenWidth / width, (int) screenHeight / height, null);
		context.drawImage(imag3, (int) ((y - 3) * screenWidth / width), (int) (x * screenHeight / height),
				(int) screenWidth / width, (int) screenHeight / height, null);
		if (i == posInventory) {
			context.setColor(Color.YELLOW);
			context.drawRect((int) ((y - 5) * (screenWidth / width)),(int) (x * (screenHeight / height)), 
					(int) screenWidth / width * 3, (int) screenHeight / height);
			context.setColor(Color.DARK_GRAY);
		}
	}
	
	/**
	 * This method displays all game elements, including the field, obstacles, characters,
	 * items, player's hand, player movement, inventory, and text on the graphics context.
	 *
	 * @param graphics The graphics context of the application.
	 * @param allElements The collection of all game elements to be displayed.
	 * @param player The player object.
	 * @param centerAffX The x-coordinate of the center of the displayed screen.
	 * @param centerAffY The y-coordinate of the center of the displayed screen.
	 */
	public void displayAllElements(Graphics2D graphics, Elements allElements, Player player, int centerAffX, int centerAffY) {
	    // Draw the game field
	    field(graphics, allElements.getGrid());

	    // Display obstacles on the graphics context
	    displayElement(graphics, allElements.getObstacles());

	    // Display characters on the graphics context
	    displayCaracter(graphics, allElements.getPersos());

	    // Display items on the graphics context
	    displayItem(graphics, allElements.getItems());

	    // Display the player's hand on the graphics context
	    displayHand(graphics, player);

	    // Handle player movement
	    playerMove(graphics, player);

	    // Display the player's inventory on the graphics context
	    displayInventaire(graphics, player);

	    // Display text on the graphics context
	    displayText(graphics, player, centerAffX, centerAffY);
	}


  public void GameOver(ApplicationContext context, Player play){
    context.pollOrWaitEvent(10000);
    for(int i = 12; i >= 0; i--) {
        final int ii = i;
        context.renderFrame(graphics -> {
            graphics.setColor(Color.BLACK);
            graphics.fillRect((int)(ii * screenWidth/24), (int)(ii * screenHeight/24) , 
            		(int)(screenWidth - ii * screenWidth/12), (int)(screenHeight - ii * screenHeight/12));
        });
        context.pollOrWaitEvent(100);
    }
    context.renderFrame(graphics -> {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("name", 1, 100));
        graphics.drawString("Game Over", (int)screenWidth/3, (int)screenHeight/2);
    });
    context.pollOrWaitEvent(10000);
}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}


	public double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}


	public ApplicationContext getContext() {
		return context;
	}




}
