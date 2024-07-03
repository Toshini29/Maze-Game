package de.tum.cit.ase.maze.Character;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.Screens.GameScreen;


/**
 * The `Character` class handles movements and interactions for the game character.
 * It keeps track of the character's position and current direction.
 */
public class Character {
    private Vector2 position;
    private Direction currentDirection;

    /**
     * Enumeration representing the possible directions the character can move.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Creates a new character with the specified initial position.
     *
     * @param x           The initial x-coordinate of the character.
     * @param y           The initial y-coordinate of the character.
     * @param gameScreen  The `GameScreen` instance where the character is used.
     */
    public Character(float x, float y, GameScreen gameScreen) {
        this.position = new Vector2(x, y);
        this.currentDirection = Direction.DOWN; // Default direction
    }

    /**
     * Retrieves the current position of the character.
     *
     * @return The current position as a Vector2.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Updates the current direction of the character.
     *
     * @param newDirection The new direction to set.
     */
    public void updateDirection(Direction newDirection) {
        this.currentDirection = newDirection;
    }

    /**
     * Retrieves the current direction of the character.
     *
     * @return The current direction as a `Direction` enum value.
     */
    public Direction getCurrentDirection() {
        return this.currentDirection;
    }
}
