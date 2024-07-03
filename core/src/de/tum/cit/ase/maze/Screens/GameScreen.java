package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.Character.Character;
import de.tum.cit.ase.maze.Character.CharacterAnimation;
import de.tum.cit.ase.maze.Map.MapDataHolder;
import de.tum.cit.ase.maze.Maze.MazeRunnerGame;
import de.tum.cit.ase.maze.Maze.RenderMaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private Vector2 currentLocation;
    private static final float HEART_LOSS_COOLDOWN = 2.0f;
    private float heartLossCooldownTimer = 0.0f;
    private Map<Vector2, Float> enemyCooldowns = new HashMap<>();

    private int[][] maze ;
    private RenderMaze renderMaze;
    private Character character;
    private boolean isPaused = false; // Add a flag to track if the game is paused

    private CharacterAnimation characterAnimation;

    // Define a variable to keep track of the character's state time
    private float stateTime = 0f;

    private TextureRegion heartTexture;
    private boolean[] heartsStatus;
    private int numberOfHearts = 3;
    private boolean hasKey = false;
    private float elapsedTime = 0f;
    private List<Vector2> enemyPositions;
    private static final float ENEMY_SPEED = 20.0f;
    private List<Vector2> heartPositions;

    private int moveAmountX = 0;
    private int moveAmountY = 0;

    private final OrthographicCamera uiCamera;

    private static final float CHARACTER_SPEED = 0.1f;

    private float zoomLevel = 1.0f;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        this.maze = MapDataHolder.getSelectedMap();
        this.renderMaze = new RenderMaze(game.getSpriteBatch());
        characterAnimation = new CharacterAnimation();
        heartPositions = new ArrayList<>();
        enemyPositions = new ArrayList<>();

        // Initialize the UI camera
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        heartTexture = new TextureRegion(new Texture("Heart.png"));
        heartsStatus = new boolean[numberOfHearts];
        for (int i = 0; i < numberOfHearts; i++) {
            heartsStatus[i] = true; // All hearts are initially active
        }

        game.getBackgroundMusicGame().play();
        game.getBackgroundMusic().dispose();


        Vector2 entryPoint = findEntryPoint(maze);
        this.currentLocation = entryPoint; // Use the entry point as the current location
        this.character = new Character(entryPoint.x * 16, entryPoint.y * 16, this);
        initializeEnemies();

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.110f; // Adjust this value for desired zoom level


        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }

    private void initializeEnemies() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 4) { // Assuming '4' is the cell type for enemies
                    enemyPositions.add(new Vector2(col * 16, row * 16));
                }
            }
        }
    }


    /**
     * Find the entry point in the maze.
     *
     * @param maze The 2D array representing the maze.
     * @return The entry point as a Vector2.
     */
    private Vector2 findEntryPoint(int[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 1) {
                    // Return the position directly, assuming bottom left as 0,0
                    return new Vector2(col, row);
                }
            }
        }
        return new Vector2(0, 0); // Default to (0,0) if no entry point is found
    }

    /**
     * Sets the game's pause state.
     *
     * @param paused True to pause the game, false to resume.
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Renders the game screen, handling user input and updating game elements.
     * Checks for the escape key press to return to the menu, updates character
     * movement based on input, and draws the maze and character.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }

        // Check for the "Z" key press to zoom out
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            zoomOut(); // Call a method to zoom out
        }

        // Check for another key press (e.g., "X" key) to zoom back in
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            zoomIn(); // Call the zoomIn method
        }

        if (isPaused) {
            // Render the pause menu UI, e.g., display a "Resume" button
            game.pauseGame(this);
        } else {
            ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
            camera.update(); // Update the camera
            moveEnemies(character.getPosition(), delta);

            // Reset movement amounts at the start of each frame
            moveAmountX = 0;
            moveAmountY = 0;

            // Update moveAmountX and moveAmountY based on current key presses
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                moveAmountY = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                moveAmountY = -1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveAmountX = -1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveAmountX = 1;
            }

            // Update the direction based on the most recent movement
            if (moveAmountX != 0 || moveAmountY != 0) {
                character.updateDirection(determineDirection(moveAmountX, moveAmountY));
            }

            // Move the character
            moveCharacter(moveAmountX, moveAmountY, delta);

            // Update animation state time
            characterAnimation.update(delta);
            stateTime += delta;
            elapsedTime += delta; // Update the elapsed time

            // Update character position and camera position to follow the character
            Vector2 characterPosition = character.getPosition();
            camera.position.set(characterPosition.x, characterPosition.y, 0);
            camera.update();
            game.getSpriteBatch().setProjectionMatrix(camera.combined);

            // Begin drawing game elements
            game.getSpriteBatch().begin();

            // Draw game elements (maze, character, etc.)
            renderMaze.renderMaze(maze, enemyPositions);
            Character.Direction characterDirection = character.getCurrentDirection(); // Use the current direction
            characterAnimation.render(game.getSpriteBatch(), characterDirection, characterPosition.x, characterPosition.y);

            // End drawing game elements
            game.getSpriteBatch().end();

            // Start drawing UI elements
            uiCamera.update();
            game.getSpriteBatch().setProjectionMatrix(uiCamera.combined);
            game.getSpriteBatch().begin();

            // Define heart size and position
            float heartSize = 90; // Increase this value for larger hearts
            float uiXOffset = 50; // X offset from the left edge of the screen
            float uiYOffset = Gdx.graphics.getHeight() - 70; // Adjust Y offset for the new heart size

            // Draw the hearts
            for (int i = 0; i < numberOfHearts; i++) {
                if (heartsStatus[i]) {
                    game.getSpriteBatch().draw(heartTexture, uiXOffset + i * heartSize, uiYOffset, heartSize, heartSize);
                }
            }

            // Draw the key if the character has it
            if (hasKey) {
                game.getSpriteBatch().draw(renderMaze.getKeyTexture(), uiXOffset + 3 * heartSize, uiYOffset, heartSize, heartSize);
            }

            // End drawing UI elements
            game.getSpriteBatch().end();
        }
    }

    /**
     * Determines the character's direction based on the movement inputs.
     *
     * @param moveAmountX The change in the x-coordinate.
     * @param moveAmountY The change in the y-coordinate.
     * @return The character's direction.
     */
    private Character.Direction determineDirection(int moveAmountX, int moveAmountY) {
        if (moveAmountX > 0) {
            return Character.Direction.RIGHT;
        } else if (moveAmountX < 0) {
            return Character.Direction.LEFT;
        } else if (moveAmountY > 0) {
            return Character.Direction.UP;
        } else if (moveAmountY < 0) {
            return Character.Direction.DOWN;
        }
        return character.getCurrentDirection(); // Default to current direction if no movement
    }


    /**
     * Handles the logic for heart loss and cooldown.
     */
    private void handleHeartLoss() {
        // Implement logic to handle heart loss
        // Subtract one heart, play a sound, update the UI, etc.
        if (heartsStatus[0]) {
            heartsStatus[0] = false;
        } else if (heartsStatus[1]) {
            heartsStatus[1] = false;
        } else if (heartsStatus[2]) {
            heartsStatus[2] = false;
            game.getGameOverSound().play();
            game.goToOver();
        }
    }

    /**
     * Moves the character by the specified delta values if the destination is not a wall.
     *
     * @param moveDirectionX The change in the x-coordinate.
     * @param moveDirectionY The change in the y-coordinate.
     * @param delta          The time in seconds since the last update.
     */
    private void moveCharacter(int moveDirectionX, int moveDirectionY, float delta) {
        // Scale movement by speed
        float scaledMoveX = moveDirectionX * CHARACTER_SPEED;
        float scaledMoveY = moveDirectionY * CHARACTER_SPEED;

        // Calculate the next position
        float nextX = currentLocation.x + scaledMoveX;
        float nextY = currentLocation.y + scaledMoveY;

        // Check if the new position is within the maze bounds
        if (nextX >= 0 && nextX < maze[0].length && nextY >= 0 && nextY < maze.length) {
            // Check the type of the next block before moving
            int nextBlockType = maze[(int) nextY][(int) nextX];

            // Perform different actions based on the next block type
            switch (nextBlockType) {
                case 0: // Wall - Do nothing
                    break;

                case 1: // Default - Move the character
                case 3: // Trap - Lose a heart
                case 4: // Enemy - Lose a heart
                case 5: // Key - Pick up the key
                case 2: // Exit - Win the game
                    // Update the current location and character position
                    currentLocation.x = nextX;
                    currentLocation.y = nextY;
                    character.getPosition().x = currentLocation.x * 16;
                    character.getPosition().y = currentLocation.y * 16;
                    updateCharacterDirection(moveDirectionX, moveDirectionY);

                    if (nextBlockType == 3 || nextBlockType == 4) {
                        handleInteraction(delta);
                    }
                    if (nextBlockType == 5) {
                        handleKeyPickup();
                    }
                    if (nextBlockType == 2 && hasKey) {
                        goToWinScreen();
                    }
                    break;

                default:
                    // If it's not a special block, move the character normally
                    currentLocation.x = nextX;
                    currentLocation.y = nextY;
                    character.getPosition().x = currentLocation.x * 16;
                    character.getPosition().y = currentLocation.y * 16;
                    break;
            }
        }
    }

    private void handleInteraction(float delta) {
        if (heartLossCooldownTimer <= 0) {
            game.getLifeLostSound().play();
            handleHeartLoss(); // Handle heart loss logic
            heartLossCooldownTimer = HEART_LOSS_COOLDOWN;
        } else {
            heartLossCooldownTimer -= delta;
        }
    }

    private void handleKeyPickup() {
        game.getKeyCollectedSound().play();
        hasKey = true;
        addNewKey(maze);
    }

    /**
     * Updates the character's direction based on movement.
     *
     * @param deltaX The change in the x-coordinate.
     * @param deltaY The change in the y-coordinate.
     */
    private void updateCharacterDirection(float deltaX, float deltaY) {
        if (deltaX > 0) {
            character.updateDirection(Character.Direction.RIGHT);
        } else if (deltaX < 0) {
            character.updateDirection(Character.Direction.LEFT);
        } else if (deltaY > 0) {
            character.updateDirection(Character.Direction.UP);
        } else if (deltaY < 0) {
            character.updateDirection(Character.Direction.DOWN);
        }
    }

    /**
     * Moves enemies towards the player or applies cooldowns if they hit the player.
     *
     * @param playerPosition The player's position.
     * @param delta          The time in seconds since the last update.
     */
    private void moveEnemies(Vector2 playerPosition, float delta) {
        for (Vector2 enemyPosition : enemyPositions) {
            float deltaX = playerPosition.x - enemyPosition.x;
            float deltaY = playerPosition.y - enemyPosition.y;

            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            // Check if the enemy has an active cooldown
            if (enemyCooldowns.containsKey(enemyPosition)) {
                float cooldownTimer = enemyCooldowns.get(enemyPosition);
                if (cooldownTimer <= 0) {
                    // Cooldown has elapsed, reset it
                    enemyCooldowns.remove(enemyPosition);
                } else {
                    // Decrement the cooldown timer and skip the enemy movement
                    enemyCooldowns.put(enemyPosition, cooldownTimer - delta);
                    continue;
                }
            }

            if (distance < 20) {  // Adjust the value based on your collision criteria
                // Enemy hits the character, apply logic here
                game.getLifeLostSound().play();
                handleHeartLoss();  // Handle heart loss logic

                // Apply the cooldown for this specific enemy
                enemyCooldowns.put(enemyPosition, HEART_LOSS_COOLDOWN);
            } else {
                // Continue with normal enemy movement logic
                float directionX;
                float directionY;

                // Check if the character is close enough to the enemy
                if (distance < 100) { // Adjust the value as needed for your game
                    // Move towards the character with decreased speed
                    directionX = deltaX / 200;
                    directionY = deltaY / 200;
                } else {
                    // Move up and down until the character is close
                    directionX = 0;
                    directionY = (float) Math.sin(stateTime) * ENEMY_SPEED * delta; // Adjust speed as needed
                }

                float moveAmount = ENEMY_SPEED * delta;

                enemyPosition.x += directionX * moveAmount;
                enemyPosition.y += directionY * moveAmount;
            }
        }
    }

    /**
     * Navigates to the win screen when the player wins the game.
     */
    private void goToWinScreen() {
        game.goToWin(elapsedTime);
    }

    /**
     * Finds the position of the heart in the maze.
     *
     * @return The position of the heart as a Vector2.
     */
    private Vector2 findHeartPosition() {
        for (Vector2 heartPosition : heartPositions) {
            return heartPosition;
        }
        return new Vector2(0, 0); // Default to (0,0) if no heart is found
    }

    /**
     * Adds a new key to the maze when the player collects a heart.
     *
     * @param maze The 2D array representing the maze.
     */
    private void addNewKey(int[][] maze) {
        // Find the position of the heart in the maze
        Vector2 heartPosition = findHeartPosition();

        // Find a suitable location next to the heart for the new key
        Vector2 newKeyPosition = findNewKeyPosition(maze, heartPosition);

        // Place the new key in the maze
        maze[(int) newKeyPosition.y][(int) newKeyPosition.x] = 5; // Assuming '5' is the key cell type
    }

    /**
     * Finds a suitable position next to the heart for placing a new key.
     *
     * @param maze          The 2D array representing the maze.
     * @param heartPosition The position of the heart.
     * @return The position for the new key as a Vector2.
     */
    private Vector2 findNewKeyPosition(int[][] maze, Vector2 heartPosition) {
        // Define the possible directions next to the heart where the new key can be placed
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int newRow = (int) heartPosition.y + direction[1];
            int newCol = (int) heartPosition.x + direction[0];

            // Check if the new position is within the maze boundaries and is a walkable cell
            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length && maze[newRow][newCol] != 0) {
                return new Vector2(newCol, newRow);
            }
        }

        // If no suitable position is found, return the heart position as a fallback
        return heartPosition;
    }

    /**
     * Zooms out the game view to show more of the map.
     */
    private void zoomOut() {
        // Adjust the zoom level
        zoomLevel = 0.5f; // You can adjust this value for the desired zoom level

        // Update the camera's zoom
        camera.zoom = zoomLevel;

        // Calculate the center position of the map
        float centerX = maze[0].length * 16 * 0.5f; // Adjust 16 to your tile size
        float centerY = maze.length * 16 * 0.5f; // Adjust 16 to your tile size

        // Set the camera's position to the center of the map
        camera.position.set(centerX, centerY, 0);

        // Update the camera
        camera.update();
    }

    /**
     * Zooms back in to the normal game view.
     */
    private void zoomIn() {
        // Reset the zoom level to normal (1.0f)
        zoomLevel = 0.110f;

        // Update the camera's zoom
        camera.zoom = zoomLevel;

        // Reset the camera's position to follow the character
        Vector2 characterPosition = character.getPosition();
        camera.position.set(characterPosition.x, characterPosition.y, 0);

        // Update the camera
        camera.update();
    }

    /**
     * Called when the screen is resized. Updates the camera viewport to
     * maintain the game's aspect ratio.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();

        // Update the UI camera
        uiCamera.setToOrtho(false, width, height);
        uiCamera.update();
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    /**
     * Called when this screen is no longer the current screen.
     * Stops the background music associated with the game.
     */
    @Override
    public void hide() {
        game.getBackgroundMusicGame().stop();
    }

    /**
     * Disposes of resources used by this screen.
     */
    @Override
    public void dispose() {
        // Dispose of resources here
        game.getBackgroundMusicGame().dispose();
        game.getBackgroundMusic().dispose();
        game.getSpriteBatch().dispose();
        game.getSkin().dispose();
        renderMaze.dispose(); // Assuming RenderMaze has a dispose method

        // Dispose texture resources
        heartTexture.getTexture().dispose();


        // Dispose other resources
        game.getLifeLostSound().dispose();
        game.getKeyCollectedSound().dispose();
        game.getVictorySound().dispose();
        game.getGameOverSound().dispose();
    }

}