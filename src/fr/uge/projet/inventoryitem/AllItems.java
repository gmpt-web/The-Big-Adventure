package fr.uge.projet.inventoryitem;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import fr.uge.projet.intermediaire.Position;

public class AllItems {
	private final List<InventoryItem>allItems;
	
	public AllItems(List<InventoryItem>element) {
		Objects.requireNonNull(element);
		this.allItems = element;
	}
	
	
	public List<InventoryItem>getAllItems(){
		return allItems;
	}
	
	/**
	 * This method extract all item with a position.
	 * @return A map containing item with their position.
	 */
	public Map<Position, InventoryItem> mapItemWithPosition() {
		return allItems.stream()
				.collect(Collectors.toMap(InventoryItem::position, mapElement -> mapElement));
	}
	
	/**
	 * This method check if an item is a UsefulItem.
	 * @param item The item that will be tested.
	 * @return true if t's a UsefulItem or else, false.
	 */
	private boolean isUseful(InventoryItem item) {
		return switch(item) {
		case UsefulItem u -> true;
		case Weapon w -> false;
		};
	}
	
	/**
	 * This method check if an item is a Weapon.
	 * @param item The item that will be tested.
	 * @return true if t's a Weapon or else, false.
	 */
	public static boolean isWeapon(InventoryItem item) {
		return switch(item) {
		case UsefulItem u -> false;
		case Weapon w -> true;
		};
	}
	
	/**
	 * This method extract all item that are UsefulItem.
	 * @return A map containing all item that are UsefulItem with their position.
	 */
	public Map<Position, InventoryItem>usefulItemPosition(){
		return allItems.stream().filter(elem -> isUseful(elem))
				.collect(Collectors.toMap(InventoryItem::position, elem->elem));
	}
}
