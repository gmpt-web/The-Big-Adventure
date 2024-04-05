package fr.uge.projet.personnage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.uge.projet.intermediaire.Position;

public class AllPersonnages {
	private final List<Personnage>allPerso;
	
	public AllPersonnages(List<Personnage>element) {
		this.allPerso = element;
	}
	
	
	public List<Personnage>getAllPerso(){
		return allPerso;
	}
	
	/*
	 * Cette methode extrait de la liste des elements que les enemies qui bougent
	 * Elle va servir quand on doit  bouger les enemies (stroll)
	 */
	/**
	 * This method extract from the list of element all personage that can move.
	 * @return A Map containing the actual position of every moving personage.
	 */
	public Map<Position, Personnage> friendEnemyWhoMove(){
		var mapFriendEnemy = new HashMap<Position, Personnage>();
		for(var elem : allPerso) {
			if(elem.zone() != null) {
				mapFriendEnemy.put(elem.position(), elem);
			}
		}
		return Map.copyOf(mapFriendEnemy);
	}
	
	/**
	 * This method extract the friend from the list of personage.
	 * @return A map containing the actual position of all friend.
	 */
	public Map<Position, Personnage> onlyFriends() {
        return allPerso.stream()
                .filter(Personnage::isFriend)
                .collect(Collectors.toMap(Personnage::position, elem -> elem));
    }
	
	
	/*public Personnage player() {
		return allPerso.stream().filter(perso -> perso.isPlayer()).findFirst().orElse(null);
	}*/
}
