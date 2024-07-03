package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.Maze.MazeRunnerGame;


/**
 * The screen displayed when the player wins the game.
 * This screen shows a background image, the winning title, the time taken to win,
 * and options to restart the game or return to the main menu.
 */
public class GameWinScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private final float elapsedTime;

    private Texture background;


    /**
     * Constructor for the GameWinScreen class.
     *
     * @param game       The main game class, used to access global resources and methods.
     * @param elapsedTime The time elapsed to win the game.
     */
    public GameWinScreen(MazeRunnerGame game, float elapsedTime) {
        this.game = game;
        this.elapsedTime = elapsedTime;

        // Load the background image
        background= new Texture(Gdx.files.internal("game win.png"));

        // Create an OrthographicCamera and set it to the screen dimensions
        var camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create a viewport for the stage using the camera
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        // Create an image actor for the background and make it fill the stage
        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true); // Make the background fill the stage
        stage.addActor(backgroundImage);

        // Create a table layout for UI elements
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add a label displaying "Winner!"
        Label titleLabel = new Label("Winner!", game.getSkin(), "title");
        table.add(titleLabel).padBottom(80).row();


        // Add a label displaying the time taken to win
        Label timeLabel = new Label("Time: " + formatTime(elapsedTime), game.getSkin());
        table.add(timeLabel).padBottom(20).row();

        // Create and add a "Restart" button
        TextButton restartButton = new TextButton("Restart", game.getSkin());
        table.add(restartButton).width(200).height(50).padBottom(20).row();

        // Create and add a "Return to Menu" button
        TextButton returnToMenuButton = new TextButton("Go to Menu", game.getSkin());
        table.add(returnToMenuButton).width(200).height(50).row();

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Restart the game when the button is clicked
                game.goToGame();
            }
        });

        returnToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Return to the menu when the button is clicked
                game.goToMenu();
            }
        });
    }


    /**
     * Formats the time in seconds as minutes and seconds (e.g., "mm:ss").
     *
     * @param time The time in seconds.
     * @return The formatted time string.
     */
    private String formatTime(float time) {
        // Format the time as needed (e.g., minutes:seconds)
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

     /**
     * Called when this screen is displayed. Sets the input processor to the stage.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the screen. Clears the screen, updates the stage, and draws the stage.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the screen is resized. Updates the viewport's dimensions.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Disposes of resources used by this screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}





