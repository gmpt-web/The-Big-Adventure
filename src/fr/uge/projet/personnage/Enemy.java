package fr.uge.projet.personnage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import fr.uge.projet.intermediaire.Position;
import fr.uge.projet.intermediaire.Zone;

public class Enemy implements Personnage {
	private final String name;
	private final String skin;
	private final Position position;
	private int health;
	private final Zone zone;
	private final String behavior;
	private final int damage;

	public Enemy(String name, String skin, Position position, int health, Zone zone, String behavior, int damage) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		//Objects.requireNonNull(zone);
		Objects.requireNonNull(position);
		//Objects.requireNonNull(behavior);
		if(damage < 0 ) {
			throw new IllegalArgumentException("Le damage doit être positifs");
		}
		this.name = name;
		this.skin = skin;
		this.position = position;
		this.health = health;
		this.zone = zone;
		this.behavior = behavior;
		this.damage = damage;
	}

	@Override
	public String toString() {
		return "Ennemy{" +
				"name='" + name + '\'' +
				", skin = " + skin +
				", position=" + position.toString() +
				", health = " + health +
				//", zone = " + zone.toString() +
				", behavior = " + behavior +
				", damage = " + damage +
				'}';
	}

	@Override
	public boolean isPlayer() {
		return false;
	}
	
	@Override
	public boolean isFriend() {
		return false;
	}

	public Position position() {
		return this.position;
	}


	public String getName() {
		return name;
	}



	@Override
	public Zone zone() {
		return zone;
	}

	@Override
	public String skin() {
		return skin;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getText() {
		return null;
	}
	
	public Map<String, List<String>> getTrade() {
		return null;
	}

}
