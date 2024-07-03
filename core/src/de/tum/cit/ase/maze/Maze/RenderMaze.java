package de.tum.cit.ase.maze.Maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;


/**
 * The `RenderMaze` class is responsible for rendering the maze based on the provided 2D array of cell types.
 * It loads necessary textures from sprite sheets and maps cell types to corresponding textures.
 */
public class RenderMaze {

    private SpriteBatch spriteBatch;
    private Texture spriteSheet;

    private TextureRegion wallTexture;
    private TextureRegion entryTexture;
    private TextureRegion exitTexture;
    private TextureRegion trapTexture;
    private TextureRegion enemyTexture;
    private TextureRegion keyTexture;
    private TextureRegion cellTexture;

    private Texture keySheet;
    private Texture mobSheet;

    /**
     * Constructor for creating a `RenderMaze` instance.
     *
     * @param spriteBatch The SpriteBatch used for rendering.
     */
    public RenderMaze(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        loadAssets();
    }

    /**
     * Loads the necessary textures from sprite sheets.
     */
    private void loadAssets() {
        spriteSheet = new Texture("basictiles.png");
        keySheet = new Texture("things.png");
        mobSheet = new Texture("mobs.png");
        cellTexture = new TextureRegion(spriteSheet, 48, 32, 16, 16);
        wallTexture = new TextureRegion(spriteSheet, 0, 0, 16, 16);
        entryTexture = new TextureRegion(keySheet, 0, 0, 16, 16);
        exitTexture = new TextureRegion(keySheet, 48, 0, 16, 16);
        trapTexture = new TextureRegion(new Texture("Trap.png"));
        keyTexture = new TextureRegion(new Texture("icons8-key-200.png"));
        enemyTexture = new TextureRegion(mobSheet, 96, 64, 16, 16);
    }

    /**
     * Renders the maze based on the provided 2D array of cell types and the positions of enemies.
     *
     * @param maze            The 2D array representing the maze with different cell types.
     * @param enemyPositions  The list of enemy positions to be rendered.
     */
    public void renderMaze(int[][] maze, List<Vector2> enemyPositions) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                TextureRegion renderTexture = getTextureForCell(maze[row][col]);
                if (renderTexture != null) {
                    float x = col * 16;
                    float y = row * 16;
                    spriteBatch.draw(renderTexture, x, y, 16, 16);
                }
            }
        }
        for (Vector2 enemyPosition : enemyPositions) {
            spriteBatch.draw(enemyTexture, enemyPosition.x, enemyPosition.y, 16, 16);
        }
    }

    /**
     * Gets the appropriate texture for a given cell type.
     *
     * @param cellType The type of the cell in the maze.
     * @return The TextureRegion corresponding to the cell type.
     */
    private TextureRegion getTextureForCell(int cellType) {
        switch (cellType) {
            case 0:
                return wallTexture;
            case 1:
                return entryTexture;
            case 2:
                return exitTexture;
            case 3:
                return trapTexture;
            case 4:
                return enemyTexture;
            case 5:
                return keyTexture;
            default:
                return cellTexture;
        }
    }

    /**
     * Retrieves the texture for the key item.
     *
     * @return The TextureRegion for the key.
     */
    public TextureRegion getKeyTexture() {
        return keyTexture;
    }

    /**
     * Disposes of the loaded textures to free up resources.
     */
    public void dispose() {
        if (spriteSheet != null) spriteSheet.dispose();
        if (keySheet != null) keySheet.dispose();
        if (mobSheet != null) mobSheet.dispose();
    }
}
