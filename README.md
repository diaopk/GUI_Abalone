# GUI-Abalone
The Bsc3 GUI project.
This project is originally working in Eclipse.
I am not sure if it's working when putting in other IDE.

# Classes brief description
# Abalone.java
This class launches the whole application project, defining the Scene object to be shown.

# Board.java
The class shows the game board containing all the pieces and empty cells needed.
The class overrides a resize() and relocate() methods to resize and relocate 
all the sub-objects when appropriated.

Resizing the game board and its sub-objects:
The board uses 9*9 CellControls to show up the entire board, and uses appropriate 
data to translate cells into correct positions. The correct position can be done by
simple math computing.

# CellControl.java
I did not use any of data structures to build the cell.
Each cell is able to show up different pieces or empty cell depending on the type attribute.
Each cell keeps 6 references of its surrounding cells. We can get any of these by getSurr(index)
method.
Each cell keeps its state and type to represent different conditions.

# Piece.java
This basically is a shape of circle used by CellControl.java to display circle.
You can change its filling colour, ...

# GameLogic.java
This class is kind of important to handle all the game logic stuff behind the game.
It counts the number of pieces, setting the status bar, setting the next player then 
changing corresponding settings and setting the winner. 