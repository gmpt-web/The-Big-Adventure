package fr.uge.projet.inventoryitem;

import java.util.Objects;
import java.util.Set;

import fr.uge.projet.intermediaire.Position;

public record UsefulItem(String name, String skin, Position position, String kind, String text) implements InventoryItem{
	public UsefulItem{
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(kind);
	}
	
	@Override
  public String toString() {
      return "Item{" +
              "name='" + name + '\'' +
              ", skin = " + skin +
              ", position=" + position.toString() +
              ", kind = " + kind +
              '}';
  }
	
	/**
	 * This method check if a given item is a UsefulItem.
	 * @param tname The name of the item that will be tested.
	 * @return true if tname is among the strings that are considered UsefulItem or else, false.
	 */
	public static boolean isUsefulItem(String tname) {
		return Set.of("BOOK", "BOX", "CASH", "CLOCK", "COG", "CRYSTAL", "CUP", "DRUM", "FLAG", "GEM", 
				"GUITAR", "HIHAT", "KEY", "LAMP", "LEAF", "MIRROR", "MOON", "ORB", "PANTS", "PAPER", "PLANET", 
				"RING", "ROSE", "SAX", "SCISSORS", "SEED", "SHIRT", "STAR", "SUN", "TRUMPET", "VASE").contains(tname);
	}
	
	/**
	 * This method check if a given item is something that can be eaten.
	 * @param tname The name of the item that will be tested.
	 * @return true if tname is among the strings that are considered edible or else, false.
	 */
	public static boolean toEat(String eat) {
		return  Set.of("BANANA", "BOBA", "BOTTLE", "BURGER", "CAKE", "CHEESE", "DONUT", "DRINK",
        "EGG", "FRUIT", "FUNGUS", "FUNGI", "LOVE", "PIZZA", "POTATO", "PUMPKIN", "TURNIP").contains(eat);
	}
	
	

}
