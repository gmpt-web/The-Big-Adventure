package fr.uge.projet.interaction;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Integer;
import java.util.Objects;

import fr.uge.projet.intermediaire.Position;

public class Grid {
	private final int width, height;
	private final Map<String, String> encodings;
	private final Map<Position, String>mapElemGrill;
	private final Character[][] grid;
	private final List<String[]> data;


	public Grid(List<String[]> data, Map<String, String>encodings, Map<String, Integer>size) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(encodings);
		Objects.requireNonNull(size);
		this.width = size.getOrDefault("width", -1);
		this.height = size.getOrDefault("height", -1);
		if(this.width <= 0) {
			throw new IllegalArgumentException("La largeur de la map est inferieur à 0");
		}
		if(this.height <= 0) {
			throw new IllegalArgumentException("La hauteur de la map est inferieur à 0");
		}
		this.encodings = encodings;
		this.mapElemGrill = new HashMap<>();
		this.data = data;
		this.grid = new Character[height][width];
	}

	
	/**
	 * This method fill the grid that will be displayed on the terminal with 'data'.
	 */
	public void fillGrid() {
		for(int i = 0; i < height; i++) {
			String[] row = data.get(i);
			for(var elem : row) {
				for (int j = 0; j < elem.length(); j++) {
					grid[i][j] = elem.charAt(j);
				}
			}
		}
	}
	
	/**
	 * Method creating a Map containing a string of the name of the file of the
	 *  element of the map that will be displayed with he's position.
	 */
	public void mapPositionGrill(){
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				var elem = encodings.get(grid[i][j].toString());
				if(elem != null) {
					mapElemGrill.put(new Position(j, i), elem);
				}
			}
		}
	}


	public Character[][] getGrid() {
		return grid;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Map<String, String> getEncodings() {
		return encodings;
	}

	public Map<Position, String> getMapElemGrill() {
		return mapElemGrill;
	}


}
