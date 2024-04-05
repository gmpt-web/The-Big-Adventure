package fr.uge.projet.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.intermediaire.Zone;
import fr.uge.projet.inventoryitem.InventoryItem;
import fr.uge.projet.inventoryitem.UsefulItem;
import fr.uge.projet.inventoryitem.Weapon;
import fr.uge.projet.obstacle.Obstacle;
import fr.uge.projet.personnage.Personnage;
import fr.uge.projet.personnage.Friend;
import fr.uge.projet.personnage.Enemy;
import fr.uge.projet.personnage.Player;

public class LoadingDatas {
	private final List<Map<String, List<String>>> data;
	private final List<InventoryItem> inventory;
	private final List<Personnage> personnage;
	private final List<Obstacle> mapObstacle;
	private Player player = null;

	public LoadingDatas(List<Map<String, List<String>>> data) {
		Objects.requireNonNull(data);
		this.data = data;
		this.inventory = new ArrayList<>();
		this.personnage = new ArrayList<>();
		this.mapObstacle = new ArrayList<>();
	}

	
	/**
	 * This method extract data from a Map to create an Obstacle.
	 * @param mapChaine A map containing the data of an Obstacle.
	 * @return The newly created Obstacle.
	 */
	private Obstacle loadObstacle(Map<String, List<String>> mapChaine) {
		var name = mapChaine.getOrDefault("name", null).getFirst();
		var kind = mapChaine.getOrDefault("kind", null).getFirst();
		var skin = mapChaine.getOrDefault("skin", null).getFirst();
		int posX = Integer.parseInt(mapChaine.getOrDefault("position", null).getFirst());
		int posY = Integer.parseInt(mapChaine.getOrDefault("position", null).getLast());
		Position pos = new Position(posX, posY);
		return new Obstacle(name, skin, pos, kind);
	}

	
	/**
	 * This method extract data from a Map to create an InventoryItem.
	 * @param mapChaine A map containing the data of an InventoryItem.
	 * @return The newly created InventoryItem that can be a Weapon or a UsefulItem.
	 */
	private InventoryItem loadItem(Map<String, List<String>> mapChaine) {
		var name = mapChaine.getOrDefault("name", null).getFirst();
		var kind = mapChaine.getOrDefault("kind", null).getFirst();
		var skin = mapChaine.getOrDefault("skin", null).getFirst();
		var text = mapChaine.getOrDefault("text", null);
		int posX = Integer.parseInt(mapChaine.getOrDefault("position", null).getFirst());
		int posY = Integer.parseInt(mapChaine.getOrDefault("position", null).getLast());
		Position pos = new Position(posX, posY);
		var damage = mapChaine.get("damage");
		if (damage != null) { 
			int dmage = Integer.parseInt(damage.getFirst());
			return new Weapon(name, skin, pos, kind, dmage);
		}
		if(text != null) return new UsefulItem(name, skin, pos, kind, String.join(" ", text));
		return new UsefulItem(name, skin, pos, kind, null);
	}

	
	/**
	 * This method extract data from a Map to create a Player.
	 * @param mapChaine A map containing the data of the Player.
	 * @return The newly created Player.
	 */
	private Player loadPlayer(Map<String, List<String>> mapChaine) {
		var name = mapChaine.getOrDefault("name", null).getFirst();
		var bool = mapChaine.getOrDefault("player", null).getFirst();
		var skin = mapChaine.getOrDefault("skin", null).getFirst();
		boolean truePlayer;
		if (bool.equals("true")) {
			truePlayer = true;
		} else {
			truePlayer = false;
		}
		int heatht = Integer.parseInt(mapChaine.getOrDefault("health", null).getLast());
		int posX = Integer.parseInt(mapChaine.getOrDefault("position", null).getFirst());
		int posY = Integer.parseInt(mapChaine.getOrDefault("position", null).getLast());
		Position pos = new Position(posX, posY);
		return new Player(name, skin, truePlayer, pos, heatht);
	}


	/**
	 * This method extract data from a Map to create a Enemy.
	 * @param mapChaine A map containing the data of the Enemy.
	 * @return The newly created Enemy that can or cannot move..
	 */
	private Personnage loadEnnemy(Map<String, List<String>> mapChaine) {
		var name = mapChaine.getOrDefault("name", null).getFirst();
		var skin = mapChaine.getOrDefault("skin", null).getFirst();
		var behavior = mapChaine.get("behavior");
		int damage = Integer.parseInt(mapChaine.getOrDefault("damage", null).getFirst());
		int heatht = Integer.parseInt(mapChaine.get("health").getFirst());
		int posX = Integer.parseInt(mapChaine.getOrDefault("position", null).getFirst());
		int posY = Integer.parseInt(mapChaine.getOrDefault("position", null).getLast());
		Position pos = new Position(posX, posY);
		if (behavior != null) {
			int zoneX = Integer.parseInt(mapChaine.getOrDefault("zone", null).get(3));
			int zoneY = Integer.parseInt(mapChaine.getOrDefault("zone", null).get(2));
			Zone zone = new Zone(pos, zoneX, zoneY);
			String behavior1 = behavior.getFirst();
			return new Enemy(name, skin, pos, heatht, zone, behavior1, damage);
		}
		return new Enemy(name, skin, pos, heatht, null, null, damage);
	}

	/**
	 * This method extract data on the exchange possible with Friend. 
	 * @param list The list of exchange.
	 * @return A map containing all possible trade.
	 */
	private Map<String, List<String>> trade(List<String> list) {
		if(list == null) return null;
		var map = new HashMap<String, List<String>>();
		for (int i = 0; i < list.size(); i += 2) {
			String key = list.get(i);
			if (i + 2 < list.size()) {
				var value = list.get(i + 1) + " " + list.get(i + 2);
				if (list.get(i + 2).toLowerCase().equals(list.get(i + 2))) {
					map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
					i++;
				}else {
					map.computeIfAbsent(key, k -> new ArrayList<>()).add(list.get(i + 1));
				}
			}else {
				map.computeIfAbsent(key, k -> new ArrayList<>()).add(list.get(i + 1));
			}
		}
		return Map.copyOf(map);
	}

	/**
	 * This method extract data from a Map to create a Friend.
	 * @param mapChaine A map containing the data of the Friend.
	 * @return The newly created Friend.
	 */
	private Personnage loadFriend(Map<String, List<String>> mapChaine) {
		var name = mapChaine.getOrDefault("name", null).getFirst();
		var skin = mapChaine.getOrDefault("skin", null).getFirst();
		int heatht = Integer.parseInt(mapChaine.get("health").getFirst());
		int posX = Integer.parseInt(mapChaine.getOrDefault("position", null).getFirst());
		int posY = Integer.parseInt(mapChaine.getOrDefault("position", null).getLast());
		Position pos = new Position(posX, posY);
		var text = mapChaine.getOrDefault("text", null);
		var text2 = String.join(" ", text);
		var read = mapChaine.getOrDefault("trade", null);
		var mapEchange = trade(read);
		return new Friend(name, skin, pos, heatht,  text2, mapEchange);
	}
	
	/**
	 * Method that manages the loading of all data.
	 */
	public void load() {
		for (var elem : data) {
			var value = elem.get("kind"); // Y A PAS QUE LE JOUEUR QUI N'A PAS DE KIND (FAUDRAIT PENSER UN JOUR)
			if (value == null && elem.get("player") != null) {
				player = loadPlayer(elem);
			} else {
				var kind = value.getFirst();
				switch (kind) {
				case "obstacle" -> { mapObstacle.add(loadObstacle(elem)); }
				case "item" -> { inventory.add(loadItem(elem)); }
				case "enemy" -> { personnage.add(loadEnnemy(elem)); }
				case "friend" -> { personnage.add(loadFriend(elem)); }
				default -> {}
				}
			}
		}
	}

	public List<InventoryItem> getInventory() {
		return inventory;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Personnage> getPersonnage() {
		return personnage;
	}

	public List<Obstacle> getObstacle() {
		return mapObstacle;
	}

}
