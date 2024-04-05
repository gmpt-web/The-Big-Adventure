package fr.uge.projet.inventoryitem;

import fr.uge.projet.intermediaire.Position;

public sealed interface InventoryItem permits UsefulItem, Weapon{
	String skin();
	Position position();
	String text();
}
