package fr.uge.projet.main;
import java.io.IOException;

import fr.uge.projet.interaction.GameController;
import fr.umlv.zen5.Application;
import java.awt.Color;


public class Main {

	/**
	 * @mainpage Title of the main page.
	 * 
	 * This is the content of the main page.
	*/
	public static void main(String[] args)  {
		Application.run(Color.LIGHT_GRAY, t -> {
			try {
				GameController.initGame(t, args);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}


