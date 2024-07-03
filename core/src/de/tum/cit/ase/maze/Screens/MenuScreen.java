package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.Map.MapDataHolder;
import de.tum.cit.ase.maze.Map.MapLoader;
import de.tum.cit.ase.maze.Maze.MazeRunnerGame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    private int[][] selectedMap;

    private Texture backgroundTexture;

    private Table table;

    private MazeRunnerGame game;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        game.getBackgroundMusic().play();
        var camera = new OrthographicCamera();
        camera.zoom = 0.9f; // Set camera zoom for a closer view
        this.game = game;

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        // Load the background image
        backgroundTexture = new Texture(Gdx.files.internal("adobe maze.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        backgroundImage.setScaling(Scaling.stretch);
        stage.addActor(backgroundImage);

        // Create and configure the table
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table); // Add the table above the background image


        // Add the table to the stage
        stage.addActor(table);

        // Add UI elements to the table
        table.add(new Label(" The Labyrinth Quest", game.getSkin(), "title")).padBottom(80).row();
        TextButton goToGameButton = new TextButton("Go To Game", game.getSkin());
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });
        table.add(goToGameButton).width(300).row();

        createMapSelectionButton(game);
    }

    /**
     * Initiates the map selection process using a file chooser dialog.
     * This method is triggered when the "Select Map" button is pressed.
     * It allows the user to choose a properties file for the game map.
     * The selected map is loaded using `MapLoader` and set in `MapDataHolder`.
     * Uses a Swing EventQueue to ensure proper GUI thread execution.
     */
    private void mapSelection() {
        //from https://www.youtube.com/watch?v=2R5mviHxvic
        EventQueue.invokeLater(() -> {
            // Specify the full path to the maps directory
            String path = "C:\\Users\\iyers\\eclipse-workspace\\fophn2324infun2324projectworkx-ternaryteam\\maps";
            JFileChooser fileChooser = new JFileChooser(path);
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Properties file", "properties");
            fileChooser.setFileFilter(fileFilter);
            fileChooser.addChoosableFileFilter(fileFilter);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                selectedMap = MapLoader.loadMap(filePath);
                MapDataHolder.setSelectedMap(selectedMap);
                MapDataHolder.displaySelectedMap();
            }
        });
    }

    /**
     * Creates and adds a "Select Map" button to the menu screen.
     * This button triggers the map selection process when pressed.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    private void createMapSelectionButton(MazeRunnerGame game) {
        TextButton selectMapButton = new TextButton("Select Map", game.getSkin());

        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapSelection();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(selectMapButton).width(300).padBottom(20).row();

        // Add the table to the stage
        stage.addActor(table);
    }

    /**
     * Renders the menu screen. Clears the screen, updates the stage, and draws the stage.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Play the menu music when the screen is shown
        game.getBackgroundMusic().play();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        // Stop or pause the menu music when the screen is hidden
        game.getBackgroundMusic().stop(); // or .pause()
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

}