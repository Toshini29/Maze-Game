package de.tum.cit.ase.maze.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The `MapLoader` class is responsible for loading and parsing maze maps from properties files.
 * It reads coordinates and corresponding values from the file to generate a 2D array representing the maze.
 */
public class MapLoader {

    /**
     * Loads a maze map from the specified file and converts it into a 2D array.
     *
     * @param mapName The name of the file containing maze properties.
     * @return A 2D array representing the loaded maze, or null if an error occurs.
     */
    public static int[][] loadMap(String mapName) {
        Properties mapProperties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(mapName)) {
            mapProperties.load(inputStream);

            int maxRow = 0;
            int maxCol = 0;

            // Iterate through the keys and find the maximum coordinates
            for (Object key : mapProperties.keySet()) {
                String[] coordinates = key.toString().split(",");

                // Check if the key is a valid coordinate
                if (coordinates.length != 2) {
                    continue; // Skip invalid coordinates
                }

                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);

                maxRow = Math.max(maxRow, row);
                maxCol = Math.max(maxCol, col);
            }

            // Create the array dynamically based on maximum coordinates
            int[][] mapArray = new int[maxRow + 1][maxCol + 1];

            // Initialize the array with default value 6
            for (int i = 0; i <= maxRow; i++) {
                for (int j = 0; j <= maxCol; j++) {
                    mapArray[i][j] = 6;
                }
            }

            // Parse properties and fill the 2D array
            for (Object key : mapProperties.keySet()) {
                String[] coordinates = key.toString().split(",");

                // Check if the key is a valid coordinate
                if (coordinates.length != 2) {
                    continue; // Skip invalid coordinates
                }

                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);

                int value = Integer.parseInt(mapProperties.getProperty(key.toString()));
                mapArray[col][row] = value;
            }

            return mapArray;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

