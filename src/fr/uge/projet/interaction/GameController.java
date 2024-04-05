package fr.uge.projet.interaction;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import fr.uge.projet.graphique.Affichage;
import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.inventoryitem.AllItems;
import fr.uge.projet.lecture.Lexer;
import fr.uge.projet.lecture.Parser;
import fr.uge.projet.obstacle.AllObstacles;
import fr.uge.projet.obstacle.Obstacle;
import fr.uge.projet.personnage.AllPersonnages;
import fr.uge.projet.personnage.Player;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class GameController {

	/**
	 * Method that check if the data of the map have all the mandatory information.
	 * @param parse The parser containing all the data.
	 * @return The number of errors.
	 */
	public static int checkingMapBeforeGame(Parser parse) {
		parse.allChecking();
		return parse.getCountError();
	}

	/**
	 * This method initialize the game's data.
	 * @param context The application that is running.
	 * @param args list of command line arguments
	 * @throws IOException
	 */
	public static void initGame(ApplicationContext context, String[] args) throws IOException {
		Objects.requireNonNull(args);
		if (args.length == 0) {
			System.err.println("Aucune option spécifiée. Le tableau d'arguments 'args' ne doit pas être vide.");
			System.exit(1);  
		}
		var opt = args[0];
		switch (opt) {
			case "--level" -> levelOption(context, args);
			default -> {
				System.err.println("Option disponible : --level chemin/vers/votre/fichier.map");
				System.exit(1);
			}
		}
	}


	/**
	 * This method open the file.
	 * @param context The application that is running.
	 * @param args list of command line arguments
	 * @throws IOException
	 */
	private static void levelOption(ApplicationContext context, String[] args) throws IOException {
		var map = args[1];
		var path = Path.of(map);
		String text = Files.readString(path);

		var lexer = new Lexer(text);
		var parse = new Parser(lexer);
		int resultParse = checkingMapBeforeGame(parse);

		if (resultParse != 0) {
			System.exit(1);  // Map incorrect
		}
		initAllElements(context, parse);
	}







	/**
	 * This method initialize the element of the game.
	 * @param context The application that is running.
	 * @param parse The parser containing all the data.
	 * @param player The player's data.
	 * @throws IOException
	 */
	private static void initAllElements(ApplicationContext context, Parser parse) throws IOException {
		var sizeGrill = parse.collectSize();
		var encodings = parse.collectEncodings();
		var data = parse.collectData();
		var elements = parse.addElementInListOfMap();
		var grid = new Grid(data, encodings, sizeGrill);
		grid.fillGrid();
		var load = new LoadingDatas(elements);
		load.load();
		var persos = new AllPersonnages(load.getPersonnage());
		var obstacles = new AllObstacles(load.getObstacle());
		var playInit = load.getPlayer();
		var items = new AllItems(load.getInventory());
		var allElements = new Elements(grid, obstacles, persos, items, playInit);
		allElements.fielElements();
		var display = new Affichage(grid.getHeight(), grid.getWidth(), allElements.getImages(), context);
		runGame(context, allElements, display);
	}

	/**
	 * The main loop for running the game.
	 * @param context The application that is running.
	 * @param aff The information of everything in the game.
	 * @throws IOException
	 */
	public static void runGame(ApplicationContext context, Elements allElements, Affichage aff) throws IOException {
		Objects.requireNonNull(aff);
		Objects.requireNonNull(allElements);
		Player player = allElements.getPlayer();
		int mod = 1;
		displayAll(context, allElements, aff, player);
		while (true && player.getHealth() > 0) {
			moveMonster(context, allElements, aff, player, mod);
			gestionEvent(context, allElements, aff, player);
			mod++;
		}
		aff.GameOver(context, player);
		System.err.println("Votre point de vie est à 0. Vous avez perdu ! ");
		System.exit(0);
	}



	/**
	 * Method that manages the the movement of all personages.
	 * @param context The application that is running.
	 * @param aff The information of everything in the game.
	 * @param player The player's data.
	 * @param mod The value used to get the time before the personages move.
	 */
	private static void moveMonster(ApplicationContext context, Elements allElements, Affichage aff, Player player, int mod) {
		if (mod % 120 == 0) {
			if (!player.isTalkingWith()) {
				PersonnageMoving.enemyBeforeStroll(allElements);
			}
			displayAll(context,allElements, aff, player);
		}
	}

	/**
	 * This method manage the player's action.
	 * @param context The application that is running.
	 * @param aff The information of everything in the game.
	 * @param player The player's data.
	 * @throws IOException
	 */
	private static void gestionEvent(ApplicationContext context, Elements allElements, Affichage aff, Player player) throws IOException {
		Event event = context.pollOrWaitEvent(10);
		if (event != null) {
			Action action = event.getAction();
			if (action == Action.KEY_PRESSED) {
				PersonnageMoving.setOfCaractersParameters(context, allElements, player, event.getKey());
				displayAll(context, allElements, aff, player);
			}
		}
	}

	/**
	 * Method that manages the graphic part of the game.
	 * @param context The application that is running.
	 * @param aff The information of everything in the game.
	 * @param player The player's data.
	 */
	public static void displayAll(ApplicationContext context, Elements allElements, Affichage aff, Player player) {
		context.renderFrame(graphics -> {
			int centerAffY = valCenter(player.getPosition().getY(), aff.getWidth(), 6, 7);
			int centerAffX = valCenter(player.getPosition().getX(), aff.getHeight(), 4, 4);
			double translateY = valTranslate(player.getPosition().getY(), aff.getWidth(), aff.getScreenWidth(), aff.getWidth()/13.0, 6, 13);
			double translateX = valTranslate(player.getPosition().getX(), aff.getHeight(), aff.getScreenHeight(), aff.getHeight()/9.0, 4, 9);
			graphics.transform(AffineTransform.getTranslateInstance(translateY, translateX));
			graphics.transform(AffineTransform.getScaleInstance(aff.getWidth() / 13.0, aff.getHeight() / 9.0));
			graphics.setFont(new Font("name", 1, 3));
			graphics.clearRect(0, 0, (int) aff.getScreenWidth(), (int) aff.getScreenHeight());
			aff.displayAllElements(graphics, allElements, player, centerAffX, centerAffY);
		});
	}

	/**
	 * This method calculate the value for TranslateInstance.
	 * @param posXY The coordinate x or y of the player.
	 * @param heightorwidth The height or the width of the grid.
	 * @param screenHW The height or width of the screen
	 * @param flat1 A flat value.
	 * @param flat2 A flat value.
	 * @param flat3 A flat value.
	 * @return
	 */
	public static double valTranslate(int posXY, int heightorwidth, double screenHW, double flat1, int flat2, int flat3) {
		if (posXY >= flat2 && posXY < heightorwidth - flat2) {
			return (screenHW * flat1 * -(posXY - flat2)) / heightorwidth;
		}else if (posXY >= heightorwidth - flat2) {
			return (screenHW * flat1 * -(heightorwidth-flat3)) / heightorwidth;
		}else {
			return 0;
		}
	}

	/**
	 * This method calculate of the value of where the screen is centered.
	 * @param posXY The coordinate x or y of the player.
	 * @param heightorwidth The height or the width of the grid.
	 * @param flat1 A flat value.
	 * @param flat2 A flat value.
	 * @return
	 */
	public static int valCenter(int posXY, int heightorwidth, int flat1, int flat2) {
		if (posXY >= flat1 && posXY < heightorwidth - flat2) {
			return posXY;
		}else if (posXY >= heightorwidth - flat2) {
			return heightorwidth - flat2;
		}else {
			return flat1;
		}
	}





	/**
	 * This method check if the given coordinate contain an obstacle.
	 * @param x The coordinate x given.
	 * @param y The coordinate y given.
	 * @param mapElement A map containing all elements.
	 * @param mapElementGrill A map containing all elements of the map.
	 * @return true if there is an obstacle at the given position or else, false.
	 */
	public static boolean adblock(int x, int y, AllObstacles obstacles, Map<Position, String> mapElementGrill) {
		Position pos = new Position(y, x);
		var obstacleElement = obstacles.mapElementWithPosition().get(pos);
		var obstacleGrill = mapElementGrill.get(pos);
		if (obstacleElement != null) {
			if (obstacles.isObstacle(obstacleElement)) {
				return true;
			}
		}
		if (obstacleGrill != null) {
			if (Obstacle.isObstacle(obstacleGrill)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Selon la direction du joueur, on lui dit si c'est possible ou pas
	 */
	/**
	 * This method change the position of the player if possible.
	 * @param aff The information of everything in the game.
	 * @param play The player's data.
	 * @param dx The value x that will be added to the player's position.
	 * @param dy The value y that will be added to the player's position.
	 */
	public static void movePlayerIfPossible(Grid grid, AllObstacles obstacles, Player play, int dx, int dy) {
		var mapElementGrill = grid.getMapElemGrill();
		var mapElement = obstacles;
		int newX = play.getPosition().getX() + dx;
		int newY = play.getPosition().getY() + dy;
		if (NearBorder(grid, newX, newY) && !adblock(newX, newY, mapElement, mapElementGrill)) {
			play.getPosition().setX(newX);
			play.getPosition().setY(newY);
		}
	}

	/**
	 * Stop the game.
	 * @param t The application that is running.
	 */
	public static void abortGame(ApplicationContext t) {
		System.out.println("abort abort !");
		t.exit(0);
	}

	/* Le joueur ne peut pas sortir de la grille */
	/**
	 * Method that prevent the personage from getting out of the grid.
	 * @param aff The information of everything in the game.
	 * @param x The coordinate x where the personage want to go.
	 * @param y The coordinate y where the personage want to go.
	 * @return
	 */
	public static boolean NearBorder(Grid grid, int x, int y) {
		if (x < 0 || x > grid.getHeight() - 1)
			return false;
		if (y < 0 || y > grid.getWidth() - 1)
			return false;
		return true;
	}

	/*
	 * Random entre 0 et 3 Cette methode est utilisée pour la direction des enemis
	 * qui stroll
	 */
	/**
	 * Method that create a random value.
	 * @return a random int.
	 */
	public static int rand() {
		Random randomNumbers = new Random();
		return randomNumbers.nextInt(4);
	}

}
