package fr.uge.projet.lecture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import fr.uge.projet.interaction.Elements;


public class Parser {
	private final Lexer lexer;
	private int countError;

	public Parser(Lexer lexer) {
		Objects.requireNonNull(lexer);
		this.lexer = lexer;
		this.setCountError(0);
	}

	
	/**
	 * This method collect the size of the map from the Lexer.
	 * "heightCount", This variable serves in the case there is more than one value inside the size,
	    we still want to put them in the map.
	 * @return A map that contain informations on the width and the height extracted from the Lexer.
	 */
	public Map<String, Integer> collectSize() {
		lexer.reset();
		var sizeGrill = new HashMap<String, Integer>();
		var heightCount = "";  
		Result result;
		while ((result = lexer.nextResult()) != null) {
			if (result.token() == Token.IDENTIFIER && result.content().equals("size")) {
				while ((result = lexer.nextResult()) != null && result.token() != Token.RIGHT_PARENS) {
					if (result.token() == Token.NUMBER) {
						if (sizeGrill.isEmpty()) {
							sizeGrill.put("width", Integer.parseInt(result.content()));
						} else {
							sizeGrill.put("height" + heightCount, Integer.parseInt(result.content()));
							heightCount = "1";
						}
					}
				}
				break;
			}
		}
		return Map.copyOf(sizeGrill);
	}

	
	/**
	 * This method collect the encodings from the Lexer.
	 * @return A map containing the encodings associated with their respective keys.
	 */
	public Map<String, String> collectEncodings() {
		lexer.reset();
		var encodings = new LinkedHashMap<String, String>();
		Result result;
		while ((result = lexer.nextResult()) != null) {
			if (result.token() == Token.IDENTIFIER && result.content().equals("encodings")) {
				while ((result = lexer.nextResult()) != null) {
					if (result.token() == Token.IDENTIFIER) {
						String identifier = result.content();
						var suivant = lexer.nextResult();
						if (suivant != null && suivant.token() == Token.LEFT_PARENS) {
							var key = lexer.nextResult();
							if (key != null && key.token() == Token.IDENTIFIER) {
								encodings.put(key.content(), identifier);
							}
						} else {
							break;
						}
					}
				}
			}
		}
		return Map.copyOf(encodings);
	}

	

	/**
	 * This method collect the game grid from the Lexer.
	 * @return A list of arrays of string containing the data of the grid.
	 */
	public List<String[]> collectData() {
		lexer.reset();
		var data = new ArrayList<String[]>();
		Pattern pattern = Pattern.compile("[a-zA-Z ]+");
		Result result;
		while ((result = lexer.nextResult()) != null) {
			if (result.token() == Token.QUOTE && result.content().contains("WWW")) {
				var content = result.content().split("\\n");
				for (var elem : content) {
					if (pattern.matcher(elem).matches()) {
						elem = elem.trim();
						var parts = elem.split("\n");
						data.add(parts);
					}
				}
			}
		}
		return List.copyOf(data);
	}

	/**
	 * Cette méthode collecte les éléments de la map à partir du Lexer.
	 * @return Une liste de chaînes contenant les éléments.
	 */
	/**
	 * This method collect the elements of the map from the Lexer.
	 * @return A list of Strings containing the elements. 
	 */
	public List<String> collectElement() {
		lexer.reset();
		var elements = new ArrayList<String>();
		var elem = new StringBuilder();
		boolean isElement = false;
		var separator = "";
		Result result;
		while ((result = lexer.nextResult()) != null) {
			if (isNewElement(result)) {
				isElement = true;
				elem = appendAllElement(elements, elem);
				separator = "";
			}
			if (shouldAppend(result, isElement)) {
				elem = appendElement(elem, result, separator);
				separator = " ";
			}
		}
		addLastElement(elements, elem);
		return List.copyOf(elements);
	}

	
	/**
	 * This method determine if a new element need to be created.
	 * @param result The actual result of the Lexer.
	 * @return A boolean indicating if a new element need to be created.
	 */
	private boolean isNewElement(Result result) {
		return result.token() == Token.IDENTIFIER && result.content().equals("element");
	}


	/**
	 * This method add all the elements in a list of elements.
	 * @param elements The list of elements.
	 * @param elem The actual alement that need to be added.
	 * @return A new StringBuilder for the next element.
	 */
	private StringBuilder appendAllElement(List<String> elements, StringBuilder elem) {
		if (!elem.isEmpty()) {
			elements.add(elem.toString());
		}
		return new StringBuilder();
	}

	
	/**
	 * This method determine if a element have to be added in a list of elements.
	 * @param result The actual result of the Lexer.
	 * @param isElement A boolean indicating if a element is in creation at the moment.
	 * @return A boolean indicating if a element have to be added.
	 */
	private boolean shouldAppend(Result result, boolean isElement) {
		Pattern pattern = Pattern.compile("\"[\\s\\S]*?\"|[0-9]+|[a-zA-Z][a-zA-Z]+");
		return isElement && !result.content().equals("element") && pattern.matcher(result.content()).matches();
	}


	/**
	 * This method add a element in a list of elements.
	 * @param elem The actual element to be added
	 * @param result The current result of the Lexer.
	 * @param separator The separator to use between the elements.
	 * @return The StringBuilder updated containing the added element.
	 */
	private StringBuilder appendElement(StringBuilder elem, Result result, String separator) {
		return elem.append(separator).append(result.content());
	}

	
	/**
	 * This method add the last element to the list of elements.
	 * @param elements The list of elements.
	 * @param elem The element that have to be added.
	 */
	private void addLastElement(ArrayList<String> elements, StringBuilder elem) {
		if (!elem.isEmpty()) {
			elements.add(elem.toString());
		}
	}


	
	/**
	 * This method determine if an attribute is potentially present.
	 * @param elem The element to be verified.
	 * @return A boolean indicating if an attribute is potentially present in the list of keys.
	 */
	private boolean attributPotentiel(String elem) {
		var pattern = Pattern.compile(".*[0-9]*|[a-zA-Z][a-zA-Z]*[aàbcçdéAÀBCÇDÉ0-9]*");
		var attribut = Set.of("name", "skin", "zone", "position", "health", "behavior", "trade", "text", "kind");
		return !attribut.contains(elem) && pattern.matcher(elem).matches();
	}


	/**
	 * This method put in a map the elements from a string of elements.
	 * @param elements A String of elements to map.
	 * @return A map containing the mapped elements.
	 */
	public Map<String, List<String>> mapElements(String elements) {
		elements = elements.replace("\"\"\"", "").trim();
		String[] parts = elements.split("\\s+");
		Map<String, List<String>> mapChaine = new HashMap<>();
		for (int i = 0; i < parts.length; i += 2) {
			if (i + 1 < parts.length) {
				String key = parts[i];
				String value = parts[i + 1];
				switch (key) {
				case "trade" -> { i = importTradeAndText(parts, i, mapChaine); }
				case "text" -> { i = importTradeAndText(parts, i, mapChaine); }
				case "position" -> { mapChaine.put(key, getPosition(parts, i, value)); i++; }
				case "zone" -> { mapChaine.put(key, getZone(parts, i, value)); i += 3; }
				default -> { mapChaine.put(key, getDefault(parts, i, value)); }
				}
			}
		}
		return mapChaine;
	}

	
	/**
	 * This method import the elements of the type Trade and Text.
	 * @param parts The parts of the String of elements.
	 * @param i The actual index insithe the parts.
	 * @param mapChaine The map where we add the elements.
	 * @return The updated index inside the parts.
	 */
	private int importTradeAndText(String[] parts, int i, Map<String, List<String>> mapChaine) {
		var count = 2;
		int inc1 = 0;
		List<String> intCoordinates = new ArrayList<>();
		String value = parts[i + 1];
		while (attributPotentiel(value)) {
			intCoordinates.add(value);
			if (i + count < parts.length) {
				value = parts[i + count];
				count++;
				inc1++;
			} else {
				break;
			}
		}
		mapChaine.put(parts[i], intCoordinates);
		return i + (inc1 - 1);
	}

	
	/**
	 * This method recover the position from the list of elements.
	 * @param parts List of the String of elements.
	 * @param i The actual index inside the parts.
	 * @param value The actual value.
	 * @return A list containing the coordinate of the position.
	 */
	private List<String> getPosition(String[] parts, int i, String value) {
		List<String> intCoordinates = new ArrayList<>();
		intCoordinates.add(value);
		intCoordinates.add(parts[i + 2]);
		return intCoordinates;
	}

	
	
	/**
	 * This method recover the area from the list of elements.
	 * @param parts List of the String of elements.
	 * @param i The actual index inside the parts.
	 * @param value The actual value.
	 * @return A list containing the coordinate of the area.
	 */
	private List<String> getZone(String[] parts, int i, String value) {
		List<String> intCoordinates = new ArrayList<>();
		intCoordinates.add(value);
		intCoordinates.add(parts[i + 2]);
		intCoordinates.add(parts[i + 3]);
		intCoordinates.add(parts[i + 4]);
		return intCoordinates;
	}

	
	/**
	 * This method recover the default value from the list of elements.
	 * @param parts List of the String of elements.
	 * @param i The actual index inside the parts.
	 * @param value The actual value.
	 * @return A list containing the default value.
	 */
	private List<String> getDefault(String[] parts, int i, String value) {
		List<String> intCoord = new ArrayList<>();
		intCoord.add(value);
		return intCoord;
	}

	/**
	 * This method add a element in a list of maps.
	 * @return A list of maps containing the added elements.
	 */
	public List<Map<String, List<String>>> addElementInListOfMap() {
		var listElement = collectElement();
		var listMap = new ArrayList<Map<String, List<String>>>();
		for (var elem : listElement) {
			var map = mapElements(elem);
			listMap.add(map);
		}
		return List.copyOf(listMap);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////// Checking all Element ///////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	/**
	 * Research the line number where the given content is found in the lexer.
	 * @param content The element to research.
	 * @return The line nulber where the content was found, or 0 if he isn't found.
	 */
	private Integer lineNumber(String content) {
		Integer line = 0;
		lexer.reset();
		Result result;
		while ((result = lexer.nextResult()) != null) {
			if (result.token() == Token.IDENTIFIER && result.content().equals(content)) {
				line = lexer.getLineNumber();
				break;
			}
		}
		return line;
	}

	
	/**
	 * Check if athe map contain exactly 2 elements (width and height), and display 
	 * the syntaxes errors if there isn't 2 elements.
	 */
	public void checkSize() {
		var sizeGrill = collectSize();
		if (sizeGrill.size() != 2) {
			Integer line = lineNumber("size");
			if (line == 0) {
				System.err.println("Syntax Error : Map without size ");
			}else {
				System.err.println("Syntax Error line : " + line);
			}
			setCountError(getCountError() + 1);
		}
	}


	/**
	 * This method check if the encodings are presents. If it's isn't the case, it display a error.
	 */
	private void checkEncodingsName() {
		var encodings = collectEncodings();
		if (encodings.isEmpty()) {
			Integer line = lineNumber("encodings");
			if (line == 0) {
				System.err.println("Syntax Error : Map without encodings ");
			}
			setCountError(getCountError() + 1);
		}
	}


	/**
	 * This method  check if every encoding elements are corrects. If it's isn't the case, it display an error.
	 */
	private void checkEncodingsElement() {
		var encodings = collectEncodings();
		for (var elem : encodings.entrySet()) {
			var value = elem.getValue();
			if (elem.getKey().length() != 1) {
				Integer line = lineNumber(value);
				System.err.println("Syntax Error in encodings line " + line);
				System.err.println("Key of  " + value + " is not an letter");
				setCountError(getCountError() + 1);
			}
			if (!Elements.isElement(value)) {
				Integer line = lineNumber(value);
				System.err.println("Syntax Error in encodings line " + line);
				System.err.println("Syntax Error : " + value + " is an Unknown element ");
				setCountError(getCountError() + 1);
			}
		}
	}
	
	/**
	 * This method check if the game's grid is present. If it's isn't the case, it display an error.
	 */
	private void dataIsEmpty() {
		var data = collectData();
		if (data.isEmpty()) {
			System.err.println("The map does not have the same width in size and data");
			System.err.println("Syntax Error : The map does not have the same height in size and data ");
		}
	}

	
	/**
	 * This method check if the width of the map is equal to the specified width.
	 * If it's isn't the case, it display an error.
	 */
	private void ckeckMapEqualSizeWidth() {
		var sizeWidth = collectSize().get("width");
		var data = collectData();
		Integer line = lineNumber("data");
		for (var premier : data) {
			for (var element : premier) {
				int dataWidth = 0;
				line++;
				for (int i = 0; i < element.length(); i++) {
					dataWidth++;
				}
				if (sizeWidth == null || dataWidth != sizeWidth) {
					System.err.println("Syntax Error in data line " + line);
					System.err.println("Syntax Error : \n" + Arrays.toString(premier));
					System.err.println("The map does not have the same width in size and data");
					setCountError(getCountError() + 1);
				}
			}
		}
	}

	
	
	/**
	 * This method check if the height of the map is equal at the specified height.
	 * if it's isn't the case, it display an error.
	 */
	private void ckeckMapEqualSizeHeight() {
		var data = collectData();
		if (data.isEmpty()) {
			Integer line = lineNumber("data");
			if (line == 0) {
				System.err.println("Syntax Error : Map without data ");
			}
			setCountError(getCountError() + 1);
		}
		var heightData = collectData().size();
		var heightSize = collectSize().get("height");
		if (heightSize == null || heightData != heightSize) {
			System.err.println("Syntax Error : The map does not have the same height in size and data ");
			setCountError(getCountError() + 1);
		}
	}
	
	
	/**
	 * This method check if everything is in order.
	 */
	public void allChecking() {
		checkSize();
		checkEncodingsName();
		checkEncodingsElement();
		dataIsEmpty();
		ckeckMapEqualSizeWidth();
		ckeckMapEqualSizeHeight();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Get and Set ////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getCountError() {
		return countError;
	}

	public void setCountError(int countError) {
		this.countError = countError;
	}

	public Lexer getLexer() {
		lexer.reset();
		return lexer;
	}
}