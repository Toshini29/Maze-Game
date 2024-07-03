package de.tum.cit.ase.maze.Map;

/**
 * The `MapDataHolder` class is a simple data holder for storing and displaying the selected maze map.
 * It provides methods to set and retrieve the selected map, as well as display its content.
 */
public class MapDataHolder {
    private static int[][] selectedMap;

    public static int[][] getSelectedMap() {
        return selectedMap;
    }

    public static void setSelectedMap(int[][] map) {
        selectedMap = map;
    }

    public static void displaySelectedMap() {
        if (selectedMap != null) {
            for (int[] row : selectedMap) {
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("No map selected.");
        }
    }

}
