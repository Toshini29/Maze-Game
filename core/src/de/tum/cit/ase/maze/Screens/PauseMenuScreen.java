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
 * The `PauseMenuScreen` class represents the pause menu screen in the MazeRunner game.
 * This screen is displayed when the game is paused, allowing the player to resume
 * the game, restart the current level, or return to the main menu.
 */

public class PauseMenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;

    /**
     * Constructor for creating a `PauseMenuScreen`.
     *
     * @param game The `MazeRunnerGame` instance that manages the game.
     */

    public PauseMenuScreen(MazeRunnerGame game) {
        this.game = game;

        var camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add a label displaying "Pause"
        Label titleLabel = new Label("Pause", game.getSkin(), "title");
        table.add(titleLabel).padBottom(80).row();

        // Create and add a "Resume" button
        TextButton resumeButton = new TextButton("Resume", game.getSkin());
        table.add(resumeButton).width(200).height(50).padBottom(20).row();

        // Create and add a "Restart" button
        TextButton restartButton = new TextButton("Restart", game.getSkin());
        table.add(restartButton).width(200).height(50).padBottom(20).row();

        // Create and add a "Return to Menu" button
        TextButton returnToMenuButton = new TextButton("Go to Menu", game.getSkin());
        table.add(returnToMenuButton).width(200).height(50).row();

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Resume the game when the button is clicked
                game.resumeGame();
            }
        });

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


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

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
     * Called when the screen should be disposed of.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
