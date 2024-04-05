package fr.uge.projet.personnage;

import java.util.List;
import java.util.Map;


import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.intermediaire.Zone;

public interface Personnage{
	boolean isPlayer();
	boolean isFriend();
	Position position();
	Zone zone();
	String skin();
	String getName();
	String getText();
	Map<String, List<String>> getTrade();
	int getHealth();
	void setHealth(int health);
}
