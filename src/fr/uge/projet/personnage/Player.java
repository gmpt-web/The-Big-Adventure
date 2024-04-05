package fr.uge.projet.personnage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.uge.projet.intermediaire.Direction;
import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.inventoryitem.InventoryItem;

public class Player {
	private final String name;
	private final String skin;
	private final boolean player;
	private Position position;
	private int health;
	private Direction direction;
	private InventoryItem inHand;
	private List<InventoryItem> Inventory;
	private boolean afficheInventory;
	private int posInventory;
	private String TalkingWith;
	private Map<String, List<String>> trading;
	private int nbtrade;
	
	
	public Player(String name, String skin, boolean player, Position position, int health) {
		Objects.requireNonNull(position);
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		this.name = name;
		this.skin = skin;
		this.player = player;
		this.position = position;
		this.health = health;
		this.setDirection(Direction.West);
		this.setInHand(null);
		this.Inventory = new ArrayList<InventoryItem>();
		this.afficheInventory = false;
		this.posInventory = 0;
		this.TalkingWith = null;
		this.trading = null;
		this.nbtrade = 0;
	}

	public String getName() {
		return this.name;
	}


	public String skin() {
		return this.skin;
	}


	public boolean getPlayer() {
		return this.player;
	}
	
	public Position getPosition() {
		return this.position;
	}

	public Position position() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public String toString() {
		return "Player{" +
				"name='" + name + '\'' +
				", skin=" + skin +
				", player=" + player +
				", position = " + position.toString() +
				", health=" + health +
				'}';
	}

	

	public InventoryItem getInHand() {
		return inHand;
	}

	public void setInHand(InventoryItem inHand) {
		this.inHand = inHand;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Add an item to the player's inventory.
	 * @param item The item that need to be added.
	 */
	public void addInventory(InventoryItem item) {
		Objects.requireNonNull(item);
		Inventory.add(item);
	}

	public List<InventoryItem> getInventory() {
		return Inventory;
	}
	
	/**
	 * Trade an item if present in the inventory.
	 * @param skin The skin of the item that the player want to trade. 
	 * @return
	 */
	public boolean tradeItem(String skin) {
		int i = 0;
		for(var item: Inventory) {
			if(item.skin().equals(skin)) {
				//Inventory.remove(i); // On peut aussi faire Inventory.remove(item);
				Inventory.remove(i);
				return true;
			}
			i++;
		}
		return false;
	}
    
	public void setInventory(List<InventoryItem> inventory) {
		Inventory = inventory;
	}
	
	public boolean isAfficheInventory() {
		return afficheInventory;
	}

	public void setAfficheInventory(boolean displayInventory) {
		afficheInventory = displayInventory;
	}
	
	public int getPosInventory() {
		return posInventory;
	}

	public void setPosInventory(int posInventory) {
		this.posInventory = posInventory;
	}
	
	public boolean isTalkingWith() {
		if (TalkingWith == null)
			return false;
		else
			return true;
	}

	public String GetTalkingWith() {
		return TalkingWith;
	}

	public void setTalkingWith(String isTalkingWith) {
		this.TalkingWith = isTalkingWith;
	}

	public Map<String, List<String>> getTrading() {
		return trading;
	}

	public void setTrading(Map<String, List<String>> trading) {
		this.trading = trading;
	}

	public int getNbtrade() {
		return nbtrade;
	}

	public void setNbtrade(int nbtrade) {
		this.nbtrade = nbtrade;
	}

}
