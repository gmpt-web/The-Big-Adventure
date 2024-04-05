package fr.uge.projet.intermediaire;
import java.util.Objects;

public record Zone(Position position, int x, int y) {
	
	public Zone{
		Objects.requireNonNull(position);
		if(x < 0 || y < 0) {
			throw new IllegalArgumentException("La taille de la zone doit être positive");
		}
	}
	
}
