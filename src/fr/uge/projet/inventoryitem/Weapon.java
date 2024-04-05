package fr.uge.projet.inventoryitem;
import java.util.Objects;
import java.util.Set;

import fr.uge.projet.intermediaire.Position;


public record Weapon(String name, String skin, Position position, String kind, int damage) implements InventoryItem{
	
	public Weapon{
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		Objects.requireNonNull(kind);
		if(damage < 0) {
			throw new IllegalArgumentException("Le damage doit être positif");
		}
	}
	
	@Override
  public String toString() {
      return "Item{" +
              "name='" + name + '\'' +
              ", skin = " + skin +
              ", position=" + position.toString() +
              ", kind = " + kind +
              ", damage = " + damage +
              '}';
  }
	
	/**
	 * This method check if an item is a Weapon.
	 * @param tname The name of the item that need to be tested.
	 * @return true if tname is among the strings considered as Weapon or else, false.
	 */
	public static boolean isWeapon(String tname) {
		return Set.of("STICK", "SHOVEL", "SWORD", "BOLT").contains(tname);
	}

	public String text() {
		return null;
	}
	

}
