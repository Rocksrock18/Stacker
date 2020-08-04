# Stacker

Plays an arcade-like stacker game.

## Getting Started

Download the jar file to play the game. A save state will be created in the same location to keep track of your progress.

* **Note:** You'll need the latest version of java to run the file. 

## Game Objective

Your goal is to stack rectangles on top of each other in order to reach the top of the screen.
* The game ends when the rectangle misses the stack.

### Controls

* A moving rectangle will constantly move from left to right across the screen.
* Press [enter] in order stop the rectangle where it is and place it on top of your current stack.
* Any part of the rectangle that hangs over the edge will be cut off.
* The size of the next rectangle is always equal to the size of the rectangle on the top of the stack.

## Shop

There is a shop that lets you purchase skins for your rectangle with in-game currency.
* Currency is earned by playing the game. Each rectangle you stack = 2 coins.

### Saving your progress

The game automatically saves your progress after every action you do.
When you launch the game, it will load your current game state, meaning it won't reset by terminating the program.

* **Note**: The save state is loaded by a file called saveState.txt. Deleting this file will cause your save state to be reset.

#### Deleting your progress

You can reset your progress at any time by selecting the "Delete current save state" option in the main menu.

## Built With

Java

## Authors
* **Jacob Maxson**
