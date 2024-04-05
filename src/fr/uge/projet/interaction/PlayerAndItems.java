package fr.uge.projet.interaction;


import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.inventoryitem.AllItems;
import fr.uge.projet.inventoryitem.UsefulItem;
import fr.uge.projet.personnage.Player;

public class PlayerAndItems {

	
	/**
	 * This method check if the item is a book or a paper and if their is text in the item before displaying it.
	 * @param player The Player's information.
	 * @return -1 if the item is not a 'BOOK' or a 'PAPER', 1 if it don't have text or 0 if everything is good.
	 */
	public static int readBook(Player player) {
		var item = player.getInventory().get(player.getPosInventory());
		if(!item.skin().equals("BOOK") && !item.skin().equals("PAPER")) {
			return -1;
		}
		player.setAfficheInventory(false);
		if(item.text() == null) {
			return 1;
		}
		player.setTalkingWith(item.text());
		player.setAfficheInventory(false);
		return 0;
	}
	
	/**
	 * This method add a item that is at the same position as the player in the inventory.
	 * It place the item in the hand of the player if the player's hand is empty.
	 * @param player The player's information.
	 * @param items The list of item in the map.
	 */
	public static void collectItem(Player player, AllItems items) {
		var pos = new Position(player.getPosition().getY(), player.getPosition().getX());
		var item = items.mapItemWithPosition().get(pos);
		if(item != null) {
			item.position().setX(-1);
			item.position().setY(-1);
			player.addInventory(item);
			items.getAllItems().remove(item);
		}
	}
	
	/**
	 * This method look if the item in hand is edible and add health to the player health if that the case.
	 * @param player The player's information.
	 * @param items The list of all items in the map.
	 */
	public static void playerEat(Player player, AllItems items) {
		var listItems = items.getAllItems();
		var handItem = player.getInHand();
		if(handItem != null && UsefulItem.toEat(handItem.skin())) {
			listItems.remove(handItem);
			player.setHealth(player.getHealth() + 2);
			player.getInventory().remove(handItem);
			player.setInHand(null);
		}
	}
}
