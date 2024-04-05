package fr.uge.projet.obstacle;

import java.util.Objects;
import java.util.Set;

import fr.uge.projet.intermediaire.Position;

public record Obstacle(String name, String skin, Position position, String kind){
	public Obstacle{
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		Objects.requireNonNull(kind);
	}


	@Override
	public String toString() {
		return "Obstacle{" +
				"name='" + name + '\'' +
				", skin=" + skin +
				", position=" + position.toString() +
				", kind = " + kind +
				'}';
	}

	/**
	 * This method check if the name of the element is among the strings considered as obstacle.
	 * @param tname The name of the element that have to be tested.
	 * @return true if tname is an Obstacle or else, false.
	 */
	public static boolean isObstacle(String tname) {
		return Set.of("BED", "BOG", "BOMB", "BRICK", "CHAIR", "CLIFF", "DOOR", "FENCE", 
				"FORT", "GATE", "HEDGE", "HOUSE", "HUSK", "HUSKS", "LOCK", "MONITOR", "PIANO", 
				"PILLAR", "PIPE", "ROCK", "RUBBLE", "SHELL", "SIGN", "SPIKE", "STATUE", "WATER", 
				"STUMP", "TABLE", "TOWER", "TREE", "TREES", "WALL", "LAVA").contains(tname);
	}

	/**
	 * This method check if the name of the element is among the strings considered as decoration.
	 * @param tname The name of the element that have to be tested.
	 * @return true if tname is an decoration or else, false.
	 */
	public static boolean isDecoratif(String tname) {
		return Set.of("ALGAE", "CLOUD", "FLOWER", "FOLIAGE", "GRASS", "LADDER", "LILY",
				"PLANK", "REED", "ROAD", "SPROUT", "TILE", "TRACK", "VINE").contains(tname);
	}

	
	




}
