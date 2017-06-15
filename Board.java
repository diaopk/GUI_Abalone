package GUI_Abalone;

import javafx.scene.shape.Polygon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/* Board java file is to display the whole game grid */

public class Board extends Pane implements Constants{
	
	/* Attributes */
	private Abalone a;
	
	/* Game board, a big hexagon */
	private Polygon hexagon;
	
	/* 9 * 9 cells */
	private CellControl[][] cc;
	private GameLogic gl;
	private double s, subS;
	private int sides = 6;
	
	Board(Abalone main) {
		a = main;
		hexagon = new Polygon();
		cc = new CellControl[9][9];
		
		/* Initlise 9 * 9 cells */
		for (int i = 0; i <= 8; i++)
			for (int j = 0; j <= 8; j++)
				cc[i][j] = null;
		
		gl = new GameLogic(this);
		
		hexagon.setFill(Color.DIMGRAY);
		hexagon.setStrokeWidth(4);
		hexagon.setStroke(Color.BLACK);
		hexagon.setStyle(hexStyle());
		
		getChildren().add(hexagon);
		
	}
	
	/* Method to return 6 points for forming a hexagon */
	private Double[] makeVertices(double radius, int sides){
		Double[] vertices = new Double[sides * 2]; // stuff to be returned

		int indexInVerticesArray = 0;

		for(int i = 1; i <= sides; i++){
			vertices[indexInVerticesArray++] = radius * Math.cos((2*Math.PI*i)/sides);//x coordinate
			vertices[indexInVerticesArray++] = radius * Math.sin((2*Math.PI*i)/sides);//y coordinate
		}

		return vertices; 

	}
	
	/* Method to reset the game */
	public void reset() {
		
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				this.getChildren().remove(cc[i][j]);
				cc[i][j] = null;
			}
		//getChildren().remove(hexagon);
		
		resize(WINDOW_SIZE, WINDOW_SIZE);
		getLogic().setCurrentPlayer(BLACK);
		getLogic().setNumPizSelected(0);
		getLogic().setWinner(EMPTY);
		//a.getTimer().start();
	}
	
	/* A stupid method */
	private String hexStyle() {
		return "-fx-effect: dropshadow(three-pass-box, #cccccc, 20, 0, 8, 8);";
	}
	
	/* After a couple times of testing the program, I found that
	 * the resize method runs twice since the whole program created 
	 * (non-Javadoc)
	 * @see javafx.scene.layout.Region#resize(double, double)
	 */
	@Override
	public void resize(final double width, final double height) {
		super.resize(width, height);
		
		//System.out.println("board resize() call"); 
		
		/* CellControl Positions are based on my calculation 
		 * do not care about how they come from, Just trust them :) */
		s = width * 1/2;
		subS = width* Math.sqrt(3.0)/27;
		
		hexagon.getPoints().addAll(makeVertices(s, sides));
		
		/* Place cellControls to display a game grid of abalone */
		/* The following calculates the positions of each cell,
		 * which are empty cells, which are white pieces and black pieces
		 */
		for (int i = 0; i <= 8; i++)
			for (int j = 0; j <= 8; j++) {
			if (cc[i][j] == null) { // Display the game grid
				System.out.println("Board resize IF calls");
				if (i == 0 & j>1 & j<7 |
						i == 1 & j>0 & j<7 |
						i == 2 & j>0 & j<8 |
						i == 3 & j < 8 |
						i == 4 |
						i == 6 & j>0 & j<8 |
						i == 5 & j<8 |
						i == 7 & j>0 & j<7|
						i == 8 & j>1 & j<7) {
					cc[i][j] = new CellControl(EMPTY, this);
					cc[i][j].setIndex(i, j);
					getChildren().add(cc[i][j]);
				}
				
				if (i == 0 & j>1 & j<7 |
						i == 1 & j>0 & j<7 |
						i == 2 & j>2 & j<6) // Place white pieces
					cc[i][j].setType(WHITE);
				
				else if (i == 8 & j>1 & j<7 |
						i == 7 & j>0 & j<7 |
						i == 6 & j>2 & j<6) // Place black pieces
					cc[i][j].setType(BLACK);
				
			} else { // If cell are not null
				System.out.println("Board resize ELSE calls");
				cc[i][j].setRotate(90);
				cc[i][j].resize(2*subS, subS);
				cc[i][j].relocate(subS*Math.sqrt(3.0)*j, subS*i*3/2);
				
				if (i % 2 != 0)
					cc[i][j].setTranslateX(subS*Math.sqrt(3.0)/2);
				
				cc[i][j].setTranslateY((height-14*subS)/2);
				
				cc[i][j].checkCell();
			}

		}
	}
	
	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
		hexagon.relocate(x, y);
	}
	
	public CellControl[][] getCell() { return cc; }
	
	public GameLogic getLogic() { return gl; } 
	
	public Abalone abalone() { return a; }
	
}
