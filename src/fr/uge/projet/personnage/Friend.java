package fr.uge.projet.personnage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.intermediaire.Zone;

public record Friend(String name, String skin, Position position, int health,
		String text, Map<String, List<String>>trade) implements Personnage {

	public Friend{
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		Objects.requireNonNull(text);
	}

	@Override
	public String toString() {
		return "Ennemy{" +
				"name='" + name + '\'' +
				", skin = " + skin +
				", position=" + position.toString() +
				", health = " + health +
				'}';
	}
	
	@Override
	public boolean isPlayer() {
  	return false;
  }
	
	@Override
	public boolean isFriend() {
		return true;
	}
	
	@Override
	public Zone zone() {
		return null;
	}


	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		return;
	}
	
	public String getText() {
		return this.text;
	}

	public Map<String, List<String>> getTrade() {
		return trade;
	}

}
