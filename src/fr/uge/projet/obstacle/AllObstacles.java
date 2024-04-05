package fr.uge.projet.obstacle;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import fr.uge.projet.intermediaire.Position;

public class AllObstacles {
	private final List<Obstacle>listObstacles;

	public AllObstacles(List<Obstacle>element) {
		Objects.requireNonNull(element);
		this.listObstacles = element;
	}


	public List<Obstacle>getListObstacles(){
		return listObstacles;
	}


	
	/**
	 * This method extract every Obstacle that have a position in the map.
	 * @return A map of Obstacle with their position.
	 */
	public Map<Position, Obstacle> mapElementWithPosition() {
		return listObstacles.stream()
				.collect(Collectors.toMap(Obstacle::position, mapElement -> mapElement));
	}
	
	
	/**
	 * This method extract every Obstacle that have a skin in the map.
	 * @return A map of skin with their position.
	 */
	public Map<Position, String> elementsWithSkinPosition() {
		return listObstacles.stream()
				.collect(Collectors.toMap(Obstacle::position, Obstacle::skin));
	}
	
	public boolean isObstacle(Obstacle elem) {
		return true;
	}
	
	

}
