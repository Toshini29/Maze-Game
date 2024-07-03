package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.Maze.MazeRunnerGame;

/**
 * The screen displayed when the player loses the game.
 * This screen shows a title indicating "GameOver" and provides options to restart the game or return to the main menu.
 */
public class GameOverScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;

    /**
     * Constructor for the GameOverScreen class.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameOverScreen(MazeRunnerGame game) {
        this.game = game;

        // Create an OrthographicCamera and set it to the screen dimensions
        var camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create a viewport for the stage using the camera
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        // Create a table layout for UI elements
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add a label displaying "GameOver"
        Label titleLabel = new Label("GameOver", game.getSkin(), "title");
        table.add(titleLabel).padBottom(80).row();

        // Create and add a "Restart" button
        TextButton restartButton = new TextButton("Restart", game.getSkin());
        table.add(restartButton).width(200).height(50).padBottom(20).row();

        // Create and add a "Return to Menu" button
        TextButton returnToMenuButton = new TextButton("Go to Menu", game.getSkin());
        table.add(returnToMenuButton).width(200).height(50).row();

        // Add a click listener to the "Restart" button
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Restart the game when the button is clicked
                game.goToGame();
            }
        });

        // Add a click listener to the "Return to Menu" button
        returnToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Return to the main menu when the button is clicked
                game.goToMenu();
            }
        });
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
