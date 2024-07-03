# **TUM FOP - The Labyrinth Quest**

This is a maze game made by Toshini and Yi as a part of the TUM FOP curriculum. The game aims to allow the user the freedom of accessing any maze level by inputting a .properties file, then rendering the map and allowing the user to play. The user can move the character using arrows and the goal of the game is to access the key and find the exit without hitting the traps or the ghosts. If the user hits the objects 3 times, the game is lost, signified by the hearts at the top. If the user safely access the exit with the key, the game is won. The user then has the chance to replay the game or move to the menu screen for a different level.

The code is structured so that each part of the games is its only class, so it is easier to keep track of where everything is, and therefore don't have to worry about inheritance and rendering issues. There are 4 main packages within the code: the Character package, which involves classes that is related to character animations and interactions; the Map package, which is used to access and convert .properties file into array; the Maze package, which is mainly used for converting and rendering maps into maze; and the Screens package with contains all the screens of the game, which is menu, game, gamewin, gameover and pause.

Let's delve a bit deeper into each of the mentioned packages and the class hierarchy:

## **Character Package** 

**Character:** The central hub for character-related functionalities, such as its position.

**CharacterAnimation:** This class orchestrates the loading, updating, and presentation of character animations, imbuing the character's movements with life using the LibGDX graphics and animation capabilities.

## Map Package
**MapDataHolder:** Consider this class a repository of information pertaining to the selected maze map. 

**MapLoader:** The maestro behind the scenes, responsible for transforming properties from a file into a structured 2D array that perfectly fits the map dimensions.

## Maze Package
**MazeRunnerGame:** The commanding conductor of our game's orchestration. This class expertly manages screens, resources, and transitions between different game states. It serves as a comprehensive control center for all things game-related, leveraging the cool features of LibGDX.

**RenderMaze:** The artistic visionary entrusted with visually representing our maze. Through the enchantment of LibGDX, it dynamically renders various cell types, enhancing the visual appeal of our maze.

## Screens Package
**MenuScreen:** The warm and inviting gateway to the game. Here, players are welcomed with the main menu, offering options to select a level or gracefully exit the game.

**GameScreen:** The heart of the action! This is where players engage with the maze, striving to conquer its challenges and emerge victorious and also responsible for the lives of the character and key collection.

**GameWinScreen:** The jubilant celebration zone! It springs to life when players triumph over a level, providing them with the opportunity to revel in their success, replay the level, or return to the menu.

**GameOverScreen:** The compassionate companion that emerges when challenges prove insurmountable. Should players fall victim to traps or ghosts three times, this screen extends an empathetic choice: attempt another round or to return to the menu.

**PauseMenuScreen:** It represents the pause menu screen in the MazeRunner game, allowing players to pause the game, resume gameplay, restart the current level, or return to the main menu.

## Game Controls and Features

### Player Controls

- **Arrow Keys**: Navigate through the maze.
- **Escape Key**: Pause the game, bringing up the pause menu.
- **Zoom out and in Key**: To see the entire maze (zoom out) use `Z` 
                       and to zoom back in use `X`. 

### Pause Menu Options

While playing Maze Runner, you have control over the game flow:

- **Pause**: Press the `Escape` key to pause the game at any point.
- **Resume**: From the pause menu, select "Resume" to continue the game from where you left off.
- **Restart**: If you wish to start the current maze over, select "Restart" from the pause menu.
- **Main Menu**: Return to the main menu at any time by selecting "Main Menu" from the pause menu.

## Game Features(Bonus)

### Intelligent Enemy Movement

In Maze Runner, enemies are not your typical mindless foes. They exhibit intelligent behavior by using a path-finding algorithm when they are within a certain range of the main character. This means that as a player, you'll need to strategize and plan your movements to avoid getting caught by these cunning adversaries.

### Maze Completion Timer

As part of the game experience the game also includes a timer that tracks how much time it takes for a player to successfully navigate and complete each maze. Challenge yourself and compete for the fastest completion times to test your skills and agility.

**Take advantage of these options to fine-tune your gaming experience and conquer the mazes in The Labyrinth Quest!**