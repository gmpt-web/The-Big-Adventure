package fr.uge.projet.interaction;

import java.io.IOException;
import java.util.Map;


import fr.uge.projet.intermediaire.Direction;
import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.inventoryitem.AllItems;
import fr.uge.projet.inventoryitem.UsefulItem;
import fr.uge.projet.inventoryitem.Weapon;
import fr.uge.projet.obstacle.AllObstacles;
import fr.uge.projet.personnage.AllPersonnages;
import fr.uge.projet.personnage.Personnage;
import fr.uge.projet.personnage.Player;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.KeyboardKey;

public class PersonnageMoving {

	/**
	 * This method manage the events with the player after a key is pressed.
	 * @param t The application that is running.
	 * @param allElements The information of everything in the game.
	 * @param player The Player's information.
	 * @param key key The key that was pressed.
	 * @throws IOException
	 */
	public static void setOfCaractersParameters(ApplicationContext t, Elements allElements, Player player, KeyboardKey key)
			throws IOException {
		playerInput(t, allElements, player, key);
		PlayerAndItems.collectItem(player, allElements.getItems());
		killEnemy(player, allElements.getPersos());
		TryTokillPlayer(player, allElements.getPersos());
	}

	
	/**
	 * Call the corresponding function depending on the pressed key.
	 * @param t The application that is running.
	 * @param allElements The information of everything in the game.
	 * @param player The Player's information.
	 * @param key The key that was pressed.
	 * @throws IOException
	 */
	private static void playerInput(ApplicationContext t, Elements allElements, Player player, KeyboardKey key)
			throws IOException {
		switch (key) {
		case UP -> { eventUpKey(allElements, player); }
		case DOWN -> { eventDownKey(allElements, player); }
		case LEFT -> { eventLeftKey(allElements, player); }
		case RIGHT -> { eventRightKey(allElements, player); }
		case SPACE -> { eventSpaceKey(allElements, player); }
		case I -> { eventIKey(player); }
		case Q -> { eventQKey(t, player); }
		default -> { break; }
		}
	}

	/**
	 * Method that manages the possible actions when the key 'Up' is pressed. 
	 * @param allElements The information of everything in the game.
	 * @param player The Player's information.
	 */
	private static void eventUpKey(Elements allElements, Player player) {
		if (player.isTalkingWith()) {
			inTrade(player, -1);
		} else if (player.isAfficheInventory()) {
			if (player.getPosInventory() > 4) {
				player.setPosInventory(player.getPosInventory() - 5);
			}
		} else {
			player.setDirection(Direction.North);
			GameController.movePlayerIfPossible(allElements.getGrid(), allElements.getObstacles(), player, -1, 0);
		}
	}

	/**
	 * Method that manages the possible actions when the key 'Down' is pressed. 
	 * @param allElements The information of everything in the game.
	 * @param player The Player's information.
	 */
	private static void eventDownKey(Elements allElements, Player player) {
		if (player.isTalkingWith()) {
			inTrade(player, 1);
		} else if (player.isAfficheInventory()) {
			if (player.getPosInventory() < 20) {
				if (player.getPosInventory() + 5 < player.getInventory().size())
					player.setPosInventory(player.getPosInventory() + 5);
			}
		} else {
			player.setDirection(Direction.South);
			GameController.movePlayerIfPossible(allElements.getGrid(), allElements.getObstacles(), player, 1, 0);
		}
	}

	/**
	 * Method that manages the possible actions when the key 'Left' is pressed. 
	 * @param allElements The information of everything in the game.
	 * @param player The Player's information.
	 */
	private static void eventLeftKey(Elements allElements, Player player) {
		if (player.isTalkingWith()) {
			inTrade(player, -2);
		} else if (player.isAfficheInventory()) {
			if (player.getPosInventory() % 5 > 0)
				player.setPosInventory(player.getPosInventory() - 1);
		} else {
			player.setDirection(Direction.West);
			GameController.movePlayerIfPossible(allElements.getGrid(), allElements.getObstacles(), player, 0, -1);
		}
	}

	/**
	 * Method that manages the possible actions when the key 'Right' is pressed. 
	 * @param allElements The information of everything in the game.
	 * @param player The Player's information.
	 */
	private static void eventRightKey(Elements allElements, Player player) {
		if (player.isTalkingWith()) {
			inTrade(player, 2);
		} else if (player.isAfficheInventory()) {
			if (player.getPosInventory() % 5 < 4) {
				if (player.getPosInventory() + 1 < player.getInventory().size())
					player.setPosInventory(player.getPosInventory() + 1);
			}
		} else {
			player.setDirection(Direction.East);
			GameController.movePlayerIfPossible(allElements.getGrid(), allElements.getObstacles(), player, 0, 1);
		}
	}

	/**
	 * Method that manages the possible actions when the key 'Space' is pressed. 
	 * @param AllElements The information of everything in the game.
	 * @param player The Player's information.
	 */
	private static void eventSpaceKey(Elements AllElements, Player player) throws IOException {
		if (player.isTalkingWith()) {
			goTrade(player);
			return;
		} if (player.isAfficheInventory()) {
			if (player.getInventory().size() > 0)
				if (PlayerAndItems.readBook(player) != -1)
					return;
			player.setInHand(player.getInventory().get(player.getPosInventory()));
			player.setAfficheInventory(false);
			return;
		}
		hitWithEppe(AllElements, player);
		PlayerAndItems.playerEat(player, AllElements.getItems());
	}

	/**
	 * Method that manages the possible actions when the key 'I' is pressed. 
	 * @param player @param player The Player's information.
	 */
	private static void eventIKey(Player player) {
		if (player.isTalkingWith())
			return;
		if (player.isAfficheInventory())
			player.setAfficheInventory(false);
		else {
			player.setAfficheInventory(true);
			player.setPosInventory(0);
		}
	}

	/**
	 * Method that manages the possible actions when the key 'Q' is pressed. 
	 * @param t The application that is running.
	 * @param player The Player's information.
	 */
	private static void eventQKey(ApplicationContext t, Player player) {
		if (player.isTalkingWith()) {
			player.setTalkingWith(null);
			player.setTrading(null);
			return;
		}
		GameController.abortGame(t);
	}

	/**
	 * This method check if the personage ahead is a friend and actiavte the text and trade box. 
	 * @param persos The list of personages present on the map.
	 * @param player The Player's information.
	 * @param pos The position ahead of the player.
	 * @return 0 if a friend is present ahead, or else 1.
	 */
	public static int TalkAhead(AllPersonnages persos, Player player, Position pos) {
		var friend = persos.onlyFriends().get(pos);
		if (friend == null)
			return 1;
		if (friend.isFriend()) {
			if (friend.getText() != null) {
				player.setTalkingWith(friend.getText());
				if (friend.getTrade() != null) {
					player.setTrading(friend.getTrade());
					player.setPosInventory(0);
				}
				return 0;
			}
		}
		return 1;
	}

	/**
	 * Move the position of the player when the player is trading.
	 * @param player The Player's information.
	 * @param move The value to be added to the position of the player in trade.
	 */
	public static void inTrade(Player player, int move) {
		if (player.getPosInventory() + move >= 0 && player.getPosInventory() + move < player.getNbtrade()) {
			player.setPosInventory(player.getPosInventory() + move);
		}
	}

	/**
	 * This function manages the trading by checking if the player has item that have to be traded.
	 * If place the new exchanged item in the inventory and delete the item that was given. 
	 * @param play The Player's information.
	 *
	 */
	public static void goTrade(Player player) {
		if(player.getTrading() == null) return;
		int i = 0;
		String give = null;
		String get = null;
		String name = null;
		for (var s : player.getTrading().keySet()) {
			for (var t : player.getTrading().get(s)) {
				if (i == player.getPosInventory()) {
					give = s;
					get = t.split(" ")[0];
					if (t.split(" ").length > 1)
						name = t.split(" ")[1];
				}
				i++;
			}
		}
		if (player.tradeItem(give)) {
			if (name == null)
				name = "nameless";
			if (Weapon.isWeapon(get))
				player.addInventory(new Weapon(name, get, new Position(0, 0), "item", 10));
			else
				player.addInventory(new UsefulItem(name, get, new Position(0, 0), "item", null));
			player.setTalkingWith(null);
			player.setTrading(null);
		}
	}
	
	

	

	/**
	 * This method check if there is an enemy ahead of the player and remove this enemy.
	 * @param player The Player's information.
	 * @param perso The list of personages present on the map.
	 */
	private static void killEnemy(Player player, AllPersonnages perso) {
		if (player.getInHand() != null && AllItems.isWeapon(player.getInHand())) {
			int x = player.getInHand().position().getX();
			int y = player.getInHand().position().getY();
			var position = new Position(y, x);
			var mapPositionEnemy = perso.friendEnemyWhoMove();
			var enemy = mapPositionEnemy.get(position);
			if (enemy != null) {
				enemy.setHealth(enemy.getHealth() - 10);
				if(enemy.getHealth() <= 0) {
					perso.getAllPerso().remove(enemy);
				}
			}
		}
	}
	
	private static void TryTokillPlayer(Player player, AllPersonnages perso) {
			int x = player.position().getX();
			int y = player.position().getY();
			var position = new Position(y, x);
			var mapPositionEnemy = perso.friendEnemyWhoMove();
			var enemy = mapPositionEnemy.get(position);
			if (enemy != null) {
				player.setHealth(player.getHealth() - 2);
			}
	}

	/**
	 * This method check if there is an element at the given position and change it to a 'BOX'.
	 * @param grid The information of grid the game.
	 * @param pos The given position of the element.
	 */
	public static void changeToBox(Grid grid , Position pos) {
		var mapPosition = grid.getMapElemGrill();
		if (mapPosition.containsKey(pos)) {
			mapPosition.put(pos, "BOX");
		}
	}

	/**
	 * This method check the element at the given position and change it to a 'TILE'
	   and delete the element that was in the player's hand.
	 * @param grid The information of grid in the game.
	 * @param pos The given position of the element.
	 */
	public static void openTheDoor(Grid grid, Player player, Position pos) {
		var mapPosition = grid.getMapElemGrill();
		if (mapPosition.containsKey(pos)) {
			mapPosition.put(pos, "TILE");
			player.getInventory().remove(player.getInHand());
			player.setInHand(null);
		}
	}

	/**
	 * This method check is the player have a 'SWORD' in hand and if there is a 'TREE' or a 'TREES' ahead of him
	   and call the function changeToBox if that the case.
	 * @param aff The information of everything in the game.
	 * @param player The Player's information.
	 * @param tree The element ahead of the player.
	 * @param newPosition The value of the position ahead of the player.
	 * @param repX The value that will be added to the coordinate x of the item in the hand of the player.
	 * @param repY The value that will be added to the coordinate y of the item in the hand of the player.
	 */
	private static void swordStrikeOnTrees(Grid grid, Player player, String tree, Position newPosition, int repX,
			int repY) {
		if (player.getInHand().skin().equals("SWORD")) {
			if (tree != null && (tree.equals("TREE") || tree.equals("TREES"))) {
				moveItemInHand(player, repX, repY);
				changeToBox(grid, newPosition);
			}
		}
	}

	
	/**
	 * This method look if a personage can move to a given position.
	 * @param aff The information of everything in the game.
	 * @param position The position of the personage.
	 * @param mapSkin A map containing all object in the grid.
	 * @param mapElement A map containig all element with their position.
	 * @param repX The value that will be added to the coordinate x of the item in the hand of the player.
	 * @param repY The value that will be added to the coordinate y of the item in the hand of the player.
	 * @return
	 */
	private static boolean canMoveToPosition(Grid grid, Position position, Map<Position, String> mapSkin,
			AllObstacles obstacles, int repX, int repY) {
		return GameController.NearBorder(grid, position.getY() + repY, position.getX() + repX)
				&& !GameController.adblock(position.getY() + repY, position.getX() + repX, obstacles, mapSkin);
	}

	/**
	 * This method change the position of item in hand by respectively
	   adding repX and repY to the coordinate x and y of the item.
	 * @param player The player's information.
	 * @param repX The value that will be added to the coordinate x of the item in the hand of the player.
	 * @param repY The value that will be added to the coordinate y of the item in the hand of the player.
	 */
	private static void moveItemInHand(Player player, int repX, int repY) {
		player.getInHand().position().setY(player.position().getY() + repY);
		player.getInHand().position().setX(player.position().getX() + repX);
	}

	
	/**
	 * This method manages the action with the item in the hand of the player.
	 * @param allElements The information of everything in the game.
	 * @param player The player's information.
	 * @param mapSkin A map containing all object in the grid.
	 * @param mapElement A map containig all element with their position.
	 * @param repX The value that will be added to the coordinate x of the item in the hand of the player.
	 * @param repY The value that will be added to the coordinate y of the item in the hand of the player.
	 * @throws IOException
	 */
	private static void appleDirection(Elements allElements, Player player, Map<Position, String> mapSkin,
			AllObstacles mapElement, int repX, int repY) throws IOException {
		var newPosition = new Position(player.position().getY() + repY, player.position().getX() + repX);
		var tree = mapSkin.get(newPosition);
		if (TalkAhead(allElements.getPersos(), player, newPosition) == 0 || player.getInHand() == null)
			return;
		if (AllItems.isWeapon(player.getInHand())) {
			swordStrikeOnTrees(allElements.getGrid(), player, tree, newPosition, repX, repY);
		}
		if (GameController.NearBorder(allElements.getGrid(), player.position().getX() + repX, player.position().getY() + repY)
				&& !GameController.adblock(player.position().getX() + repX, player.position().getY() + repY, mapElement,
						mapSkin)) {
			moveItemInHand(player, repX, repY);
		}
	}

	/**
	 * This method call the function appleDirection with different value depending on the direction where the player is looking.
	 * @param allElements The information of everything in the game.
	 * @param player The player's information.
	 * @throws IOException
	 */
	public static void hitWithEppe(Elements allElements, Player player) throws IOException {
		var mapSkin = allElements.getGrid().getMapElemGrill();
		var mapElement = allElements.getObstacles();
		switch (player.getDirection()) {
		case Direction.North -> {
			appleDirection(allElements, player, mapSkin, mapElement, -1, 0);
		}
		case Direction.South -> {
			appleDirection(allElements, player, mapSkin, mapElement, 1, 0);
		}
		case Direction.West -> {
			appleDirection(allElements, player, mapSkin, mapElement, 0, -1);
		}
		case Direction.East -> {
			appleDirection(allElements, player, mapSkin, mapElement, 0, 1);
		}
		default -> {
			break;
		}
		}

	}

	
	/**
	 * This method manages the mecanism of the enemies.
	 * @param aff The information of everything in the game.
	 */
	public static void enemyBeforeStroll(Elements allElements) {
		var mapEnemy = allElements.getPersos().friendEnemyWhoMove();
		for (var elem : mapEnemy.values()) {
			enemyStroll(allElements, elem);

		}
	}

	/**
	 * This method manage the random movement of a moving enemy.
	 * @param allElements The information of everything in the game.
	 * @param enemiW The enemy that will move.
	 */
	private static void enemyStroll(Elements allElements, Personnage enemiW) {
		int rand = GameController.rand();
		var mapSkin = allElements.getGrid().getMapElemGrill();
		var mapElement = allElements.getObstacles();
		switch (rand) {
		case 0 -> {
			moveEnemyIfPossible(allElements, enemiW, mapSkin, mapElement, 0, -1);
		}
		case 1 -> {
			moveEnemyIfPossible(allElements, enemiW, mapSkin, mapElement, 0, 1);
		}
		case 2 -> {
			moveEnemyIfPossible(allElements, enemiW, mapSkin, mapElement, 1, 0);
		}
		case 3 -> {
			moveEnemyIfPossible(allElements, enemiW, mapSkin, mapElement, -1, 0);
		}
		default -> {
			break;
		}
		}
	}

	/**
	 * This method check if the enemy can move to a certain direction.
	 * @param allElements The information of everything in the game.
	 * @param enemiW The enemy who is moving.
	 * @param mapSkin A map containing all object in the grid.
	 * @param mapElement A map containing all element with their position.
	 * @param repX The value that will be added to the coordinate x of the item in the hand of the player.
	 * @param repY The value that will be added to the coordinate y of the item in the hand of the player.
	 */
	private static void moveEnemyIfPossible(Elements allElements, Personnage enemiW, Map<Position, String> mapSkin,
			AllObstacles mapElement, int repX, int repY) {
		if (isWithinZone(enemiW, repX, repY)
				&& canMoveToPosition(allElements.getGrid(), enemiW.position(), mapSkin, mapElement, repX, repY)) {
			enemiW.position().setY(enemiW.position().getY() + repY);
			enemiW.position().setX(enemiW.position().getX() + repX);
		}
	}

	/**
	 * This method check if the moving monster will be in he's area of deplacement after moving.
	 * @param enemiW The enemy who is moving.
	 * @param repX The value that will be added to the coordinate x of the item in the hand of the player.
	 * @param repY The value that will be added to the coordinate y of the item in the hand of the player.
	 * @return
	 */
	private static boolean isWithinZone(Personnage enemiW, int repX, int repY) {
		return enemiW.position().getY() + repY >= enemiW.position().getY() - enemiW.zone().y()
				&& enemiW.position().getY() + repY <= enemiW.position().getY() + enemiW.zone().y()
				&& enemiW.position().getX() + repX >= enemiW.position().getX() - enemiW.zone().x()
				&& enemiW.position().getX() + repX <= enemiW.position().getX() + enemiW.zone().x();
	}

}
