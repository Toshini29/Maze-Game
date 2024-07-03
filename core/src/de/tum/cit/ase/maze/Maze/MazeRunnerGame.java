package de.tum.cit.ase.maze.Maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Map.MapDataHolder;
import de.tum.cit.ase.maze.Map.MapLoader;
import de.tum.cit.ase.maze.Screens.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;
    private Music backgroundMusic;

    private Music backgroundMusicGame;
    private Sound lifeLostSound;
    private Sound keyCollectedSound;
    private Sound victorySound;
    private Sound gameOverSound;
    private GameScreen pausedGameScreen; // Variable to store the paused game screen

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        //this.loadCharacterAnimation(); // Load character animation

        // Play some background music
        // Background sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);

        // Load music
        backgroundMusicGame = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
        backgroundMusicGame.setLooping(true);

        // Load sound effects
        lifeLostSound = Gdx.audio.newSound(Gdx.files.internal("lifeLost.mp3"));
        keyCollectedSound = Gdx.audio.newSound(Gdx.files.internal("key.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("victory.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOver.mp3"));
        MapDataHolder.setSelectedMap(MapLoader.loadMap("maps/level-1.properties"));

        goToMenu(); // Navigate to the menu screen
    }

    public void loadMapForLevel(int level) {
        String mapPath = String.format("maps/level-%d.properties", level);
        MapDataHolder.setSelectedMap(MapLoader.loadMap(mapPath));
    }

    public void resumeGame() {
        if (pausedGameScreen != null) {
            gameScreen = pausedGameScreen;
            setScreen(gameScreen);
            gameScreen.setPaused(false);
            pausedGameScreen = null; // Reset the paused game screen
        } else {
            // Handle the case where there is no pausedGameScreen (e.g., user pressed escape without a previous pause)
            Gdx.app.log("ResumeGame", "No paused game screen found");
        }
    }

    public void pauseGame(GameScreen gamescreen) {
            gamescreen.setPaused(true);
            pausedGameScreen = gamescreen; // Save the current game screen before going to pause

            setScreen(new PauseMenuScreen(this));

    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public Music getBackgroundMusicGame() {
        return backgroundMusicGame;
    }

    public Sound getLifeLostSound() {
        return lifeLostSound;
    }

    public Sound getKeyCollectedSound() {
        return keyCollectedSound;
    }

    public Sound getVictorySound() {
        return victorySound;
    }

    public Sound getGameOverSound() {
        return gameOverSound;
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    public void goToOver() {
        this.setScreen(new GameOverScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    public void goToWin(float elapsedTime) {
        this.setScreen(new GameWinScreen(this, elapsedTime)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}