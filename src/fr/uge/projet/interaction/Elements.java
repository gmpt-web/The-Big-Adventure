package fr.uge.projet.interaction;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;

import fr.uge.projet.inventoryitem.AllItems;
import fr.uge.projet.main.Main;
import fr.uge.projet.obstacle.AllObstacles;
import fr.uge.projet.personnage.AllPersonnages;
import fr.uge.projet.personnage.Player;

public class Elements {

	private final Grid grid;
	private final AllObstacles obstacles;
	private final AllPersonnages persos;
	private final AllItems items;
	private final Player player;
	private final Map<String, BufferedImage> images;

	public Elements(Grid grid, AllObstacles obstacles, AllPersonnages persos,
			AllItems items, Player player) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(obstacles);
		Objects.requireNonNull(persos);
		Objects.requireNonNull(items);
		Objects.requireNonNull(player);
		this.player = player;
		this.obstacles = obstacles;
		this.persos = persos;
		this.items = items;
		this.grid = grid;
		images = new HashMap<String, BufferedImage>();
	}

	/**
	 * This method contains the name of all Element.
	 */
	public static Set<String> listOfElement() {
		return Set.of("ALGAE", "CLOUD", "FLOWER", "FOLIAGE", "GRASS", "LADDER",
				"LILY", "PLANK", "REED", "ROAD", "SPROUT", "TILE", "TRACK", "VINE",
				"BED", "BOG", "BOMB", "BRICK", "CHAIR", "CLIFF", "DOOR", "FENCE",
				"FORT", "GATE", "HEDGE", "HOUSE", "HUSK", "HUSKS", "LOCK", "MONITOR",
				"PIANO", "PILLAR", "PIPE", "ROCK", "RUBBLE", "SHELL", "SIGN", "SPIKE",
				"STATUE", "STUMP", "TABLE", "TOWER", "TREE", "TREES", "WALL", "BUBBLE",
				"DUST", "BABA", "BADBAD", "BADBAD-South", "BADBAD-North", "BADBAD-East",
				"BABA-North", "BABA-East", "BABA-South", "BABA-West", "BAT",
				"BEE", "BIRD", "BUG", "BUNNY", "CAT", "ANNI", "CRAB", "DOG", "FISH", "FOFO",
				"FOFO-West", "FOFO-South", "FOFO-North", "FOFO-East", "FROG", "GHOST",
				"IT", "IT-West", "IT-South", "IT-North", "IT-East", "JELLY", "JIJI",
				"KEKE", "LIZARD", "ME", "MONSTER", "ROBOT", "SNAIL", "SKULL", "TEETH",
				"TURTLE", "WORM", "BOOK", "BOLT", "BOX", "CASH", "CLOCK", "COG",
				"CRYSTAL", "CUP", "DRUM", "FLAG", "GEM", "GUITAR", "HIHAT", "KEY",
				"KEY-West", "KEY-South", "KEY-North", "KEY-East", "LAMP", "LEAF",
				"MIRROR", "MOON", "ORB", "PANTS", "PAPER", "PLANET", "RING", "ROSE",
				"SAX", "SCISSORS", "SEED", "SHIRT", "SHOVEL", "STAR", "STICK",
				"STICK-North", "STICK-East", "STICK-West", "STICK-South", "SUN",
				"SWORD", "SWORD-West", "SWORD-South", "SWORD-North", "SWORD-East",
				"TRUMPET", "VASE", "Fleche", "BANANA", "BOBA", "BOTTLE", "BURGER",
				"CAKE", "CHEESE", "DONUT", "DRINK", "EGG", "FRUIT", "FUNGUS", "FUNGI",
				"LOVE", "PIZZA", "POTATO", "PUMPKIN", "TURNIP", "WATER", "ICE", "LAVA");
	}

	/**
	 * This method check if the given name is an Element.
	 * 
	 * @param tname
	 * The name of the object that need to be tested.
	 * @return true if tname is among the string that are considered as Element.
	 */
	public static boolean isElement(String tname) {
		var listElement = listOfElement();
		return listElement.contains(tname);
	}

	private void importImage() throws IOException {
		BufferedImage image;
		var listElement = listOfElement();
		for (var elem : listElement) {
			try (var input = Main.class
					.getResourceAsStream("/images/" + elem + ".png")) {
				image = ImageIO.read(input);
				if (image == null) {
					throw new IllegalArgumentException("Image not exists");
				}
				images.put(elem, image);
			}
		}
	}

	public void fielElements() throws IOException {
		grid.fillGrid();
		grid.mapPositionGrill();
		importImage();
	}

	public Grid getGrid() {
		return grid;
	}

	public AllObstacles getObstacles() {
		return obstacles;
	}

	public AllPersonnages getPersos() {
		return persos;
	}

	public AllItems getItems() {
		return items;
	}

	public Player getPlayer() {
		return player;
	}

	public Map<String, BufferedImage> getImages() {
		return images;
	}

}
