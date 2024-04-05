package fr.uge.projet.intermediaire;

import java.util.Objects;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override 
    public String toString() {
    	return "(" +  x + ' ' + y + ")";
    }
    @Override
    public boolean equals(Object o) {
       return o instanceof Position pos 
      		 && pos.x == x && pos.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}