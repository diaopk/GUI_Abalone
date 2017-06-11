package Main;

import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/* This class is to represent each cell containing a piece on the game board */
public class CellControl extends Control implements Constants{

	/* Attributes */
	private Board b;
	private Polygon hex;
	private final int sides = 6;
	private ObjectProperty<CellControl>[] ob; // Array of nodes surrounding this node
	private int[] index; // Index in the game grid
	private Circle piece;
	
	/* Each piece is identified by type and state
	 * if one of these value changes there will be 
	 * changes to its corresponding values
	 */
	private IntegerProperty type, state;
	
	@SuppressWarnings("unchecked")
	CellControl(int player, Board board) {
		
		/* Initialise them */
		b = board;
		hex = new Polygon();
		state = new SimpleIntegerProperty(UNSELECTED);
		ob = (ObjectProperty<CellControl>[]) new ObjectProperty[6]; 
		type = new SimpleIntegerProperty(player);
		piece = new Circle();
		
		/* index hold two elements, row and column */
		index = new int[2];
		
		index[0] = 0;
		index[1] = 0;
	
		/* Initialise 6 surrounding cells' state */
		for (int i = 0; i < 6; i++)
			ob[i] = new SimpleObjectProperty<CellControl>(null);
		
		this.setSkin(new CellControlSkin(this));
		
		/* Appearance setup */
		hex.setStrokeWidth(0.1);
		hex.setStroke(Color.BLACK);
		hex.setFill(Color.DIMGRAY);
		piece.setStrokeWidth(0.0);
		
		/* Set colour for this cell based on the cell's type (player) */
		switch (type.get()) {
			case EMPTY:
				piece.setFill(Color.PINK);
				break;
				
			case BLACK: // Player black
				piece.setFill(Color.BLACK);
				break;
				
			case WHITE: // Player white
				piece.setFill(Color.WHITE);
				break;
				
			default:
				System.out.println("invaild type");
			
		}
		
		getChildren().addAll(hex, piece);
		
		/*********** THE NUMBER OF EVENTS ******************/
		
		/* Action when piece is pressed */
		getCircle().setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				setStateWhenPressed();
				move();
			}
			
		});
		
		/* When state changes */
		state.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				setStateAtrbs((int)newValue, (int)oldValue);
			
			}
			
		});
		
		/* When piece is pressed */
		/*getCircle().pressedProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue,
					Object newValue) {
				// TODO Auto-generated method stub
				//move();
				
			}
			
		});
		*/
		/* When piece's type changes */
		getTypeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				setAtrbs((int)newValue, getState());
			}
			
		});
		
		/* When a piece is hover */
		getCircle().hoverProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				setPizHoverColor(getType(), getState());
			}
			
		});
	
	}
	
	/* Method to move pieces when they are able to move */
	/* This function moves this piece depanding on three conditions
	 * 1. One-piece movement: assume this cell is the cell is going to be placed a piece
	 * check surrounding cells for selected piece. If found and this cell is clicked then place a piece that is selected
	 * 2. Two-piece movement:
	 * 3. Three-piece movement: 
	 */
	public void move() {
		if (getType() == EMPTY & getState() == MOVABLE)
			for (int x = 0; x < 6; x++)
				if (getSurr(x) != null)
					if (getSurr(x).getState() == SELECTED &
						getSurr(x).getType() == b.getLogic().getCurrentPlayer()) {
						
						if (b.getLogic().numPizSelected() == 1) { // One piece movement
							setType(getSurr(x).getType());
							getSurr(x).setType(EMPTY);
							getSurr(x).setState(UNSELECTED);
							setState(UNSELECTED);
							b.getLogic().nextPlayer(getType());
						
						} else if (b.getLogic().numPizSelected() == 2) { // Two pieces movement
							// If the surrounding piece is selected and is not null
							if (getSurr(x).selectedPiz(0) != null)
								// If two next-to pieces are the same type and both selected
								if (getSurr(x).selectedPiz(0).getType() == getSurr(x).getType())
									/* Assume this object(cell) is going to be placed with a new piece
									 * If the surrounding piece of this cell is selected 
									 * and that surrounding piece's surrounding is also selected
									 * and these two surrounding are the same direction from this cell
									 */
									if (selectedPizDirc(0) == getSurr(x).selectedPizDirc(0)) {
										// Two-pieces straight movement
										//System.out.println(selectedPizDirc(0));
										//System.out.println("GetSurr.selectedPizDirc(0): " + getSurr(x).selectedPizDirc(0));
										setType(getSurr(x).getType());
										getSurr(x).setType(getSurr(x).selectedPiz(0).getType());
										getSurr(x).selectedPiz(0).setType(EMPTY);
										
										getSurr(x).selectedPiz(0).setState(UNSELECTED);
										getSurr(x).setState(UNSELECTED);
										setState(UNSELECTED);
										b.getLogic().nextPlayer(getType());
									
									} else { // If the two next-to pieces are not straight with this object
										/* This means this object is not a straight line with those
										 * then process another two-pieces movement
										 */
										/*
										 * ob[getSurr(x).selectedPizDirc(0)].get().getType() means 
										 * the type of the surrounding piece of this cell
										 */
										if (ob[getSurr(x).selectedPizDirc(0)].get() != null &
												ob[getSurr(x).selectedPizDirc(0)].get().getType() != selectedPiz(0).getType() &&
												ob[getSurr(x).selectedPizDirc(0)].get().getType() == EMPTY) {
											
											setType(getSurr(x).getType());
											ob[getSurr(x).selectedPizDirc(0)].get().setType(getSurr(x).selectedPiz(0).getType());
											getSurr(x).setType(EMPTY);
											getSurr(x).selectedPiz(0).setType(EMPTY);
											
											b.getLogic().nextPlayer(getType());
										
										}
									}
						} else { // Three pieces movement
							if (getSurr(x).selectedPiz(0) != null)
								if (selectedPizDirc(0) == getSurr(x).selectedPizDirc(0) &
								getSurr(x).selectedPizDirc(0) == getSurr(x).selectedPiz(0).selectedPizDirc(0, getSurr(x))) {
									
									setType(getSurr(x).getType());
									
									getSurr(x).setType(getSurr(x).selectedPiz(0).getType());
									getSurr(x).setState(UNSELECTED);
									
									getSurr(x).getSurr(getSurr(x).selectedPizDirc(0)).setType(getSurr(x).selectedPiz(0).selectedPiz(0).getType());
								
									getSurr(x).selectedPiz(0).selectedPiz(0).setType(EMPTY);
									
									b.getLogic().nextPlayer(getType());
								
								}
						}
					}
	}
	
	/********** FOLLOWING FOUR METHODS FOR ASSISTING IN MOVEMENTS **********/
	
	/* Private method to return surrounding selected piece */
	private CellControl selectedPiz(int index) {
		/* There are 6 pieces surrounding this cell
		 * If reach the last one surrounding piece
		 * then meaning no selected piece, return null
		 * If not reach the last one then call the function itself until reach the last one
		 */
		if (index != 6) {
			if (this.getSurr(index) != null)
				if (this.getSurr(index).getState() == SELECTED)
					return this.getSurr(index);
				else
					return selectedPiz(index+1);
			else
				return selectedPiz(index+1);
		} else
			return null;
	}
	
	private CellControl selectedPiz(int index, CellControl c) {
		if (index != 6) {
			if (this.getSurr(index) != null)
				if (this.getSurr(index).getState() == SELECTED &
						getSurr(index) != c)
					return getSurr(index);
				else
					return selectedPiz(index+1, c);
			else 
				return selectedPiz(index+1, c);
		} else
			return null;
	}
	
	/* Private method to return the surrounding selected piece's position */
	private int selectedPizDirc(int index) {
		//if (this.selectedPiz(0) != null) {
		// If the surrounding piece is selected
		//if (getSurr(index) == selectedPiz(0))
		if (getSurr(index).getState() == SELECTED)
				return index;
			else
				return selectedPizDirc(index+1);
		/*} else 
			return -1;*/
	}
	
	private int selectedPizDirc(int index, CellControl c) {
		if (index != 6)
			if (getSurr(index) == this.selectedPiz(0, c) &
				getSurr(index) != c)
			return index;
		else
			return selectedPizDirc(index+1, c);
		else
			return -1;
	}
	/********************* END OF MOVEMENT METHODS *********************/
	
	/* Method to set state for an non-empty cell when Pressed */
	public void setStateWhenPressed() {
		if (getType() != EMPTY)
			if (getState() == UNSELECTED & 
				b.getLogic().getCurrentPlayer() == getType() &
				b.getLogic().numPizSelected() < 3)
				
				setState(SELECTED);
		
			else if (getState() == SELECTED )
				setState(UNSELECTED);
				
	}
	
	/* Method to set surrounding nodes' color when this node's state changes */
	private void setSurrPizs(int state) {
		if (state == SELECTED)
			setSurrPizsAtrbs(MOVABLE, SELECTED);
		else if (state == UNSELECTED)
			setSurrPizsAtrbs(UNSELECTED, UNSELECTED);
	}
	
	/* Private method to set attributes for a specific state */
	/* When the cell's state changes, then call this function
	 * changing corresponding attributes depanding on what state it changes to
	 */
	private void setStateAtrbs(int newState, int oldValue) {
		switch (newState) {
			case UNSELECTED: // If Changes to UNSELECTED
				setAtrbs(getType(), newState);
				hex.setFill(Color.DIMGRAY);
				changeNumSltedPiz(oldValue);
				setSurrPizs(UNSELECTED);
				break;
				
			case SELECTED: // If changes to SELECTED
				setAtrbs(getType(), newState);
				hex.setFill(Color.LIGHTGRAY);
				changeNumSltedPiz(oldValue);
				setSurrPizs(SELECTED);
				
				break;
			case MOVABLE: // If changes to MOVABLE
				setAtrbs(getType(), newState);
				//getPiece().setPizHoverColor(Color.DEEPPINK, Color.HOTPINK);
				
				break;
			default: // Debug info
				System.out.println("Invaild State");
		}
	}
	
	
	/* Private method to be used for setting surrounding's node color and state
	 * if they are EMPTY type*/
	private void setSurrPizsAtrbs(int newState, int thisState) {
		for (int x = 0; x <= 5; x++)
			if (getSurr(x) != null)
				if (getSurr(x).getType() == EMPTY &
				checkSurrState(getSurr(x), 0, thisState))
					getSurr(x).setState(newState);
	}
	
	/* Method used by setSurrPizsAtrbs() to check 
	 * the passed surrounding cell's surrounding cell's state */
	private boolean checkSurrState(CellControl c, int index, int state) {
		if (state == UNSELECTED) {
			if (c.getSurr(index) != null) {
				if (c.getSurr(index).getState() != SELECTED & index <= 5) {
					if (index < 5) {
						/* index less than 5, then go ahead to check */
						return checkSurrState(c, index+1, state);
					
					} else
						/* Last one is still not selected 
						 * allow to process the loop */
						return true;		
				} else
					/* if 'one selected' state occurs,
					 * Don't allow to process the loop
					 */
					return false;
	
			} else if (c.getSurr(index) == null & index < 5) {
				return checkSurrState(c, index+1, state);
			
			} else
				/* By default i want to return false
				 * but returning true works,
				 * now i have to trust it T.T
				 */
				return true;
			
		} else // This object is set to selected
			return true;
	}

	/* Method to change the number of selected piece
	 * when the piece is selected
	 * or the piece is unselected (the state changes)
	 */
	public void changeNumSltedPiz(int oldState) {
		if (oldState == UNSELECTED)
			b.getLogic().setNumPizSelected(b.getLogic().numPizSelected()+1);
		
		else if (oldState == SELECTED)
			b.getLogic().setNumPizSelected(b.getLogic().numPizSelected()-1);
	}
	
	/* Method to check this cell's surrounding nodes, 
	 * if not null, set surrounding nodes for this node */
	/* This method maybe too much but I cannot find out a better way to set surrounding nodes */
	public void checkCell() {
		
		CellControl[][] cc = b.getCell();
		int x = getIndexRow(); // Row
		int y = getIndexCol(); // Column
		
		if (x%2 == 0) { // even row
			
			/* Check Special case first */
			if (x == 0) { // Case row 0
				if (cc[x][y-1] != null)
					setLeft(cc[x][y-1]);
				
				if (cc[x][y+1] != null)
					setRight(cc[x][y+1]);
				
				if (cc[x+1][y-1] != null)
					setBottomLeft(cc[x+1][y-1]);
				
				if (cc[x+1][y] != null)
					setBottomRight(cc[x+1][y]);
			
			} else if (x == 8) { // Case row 8
				if (cc[x][y-1] != null)
					setLeft(cc[x][y-1]);
				
				if (cc[x][y+1] != null)
					setRight(cc[x][y+1]);
				
				if (cc[x-1][y-1] != null)
					setTopLeft(cc[x-1][y-1]);
			
				if (cc[x-1][y] != null)
					setTopRight(cc[x-1][y]);
			
			} else if (y == 0) { // Case index of [4][0]
				if (cc[x][y+1] != null)
					setRight(cc[x][y+1]);
				
				if (cc[x-1][y] != null)
					setTopRight(cc[x-1][y]);
				
				if (cc[x+1][y] != null)
					setBottomRight(cc[x+1][y]);
				
			} else if (y == 8) { // Case index of [4][8]
				if (cc[x][y-1] != null)
					setLeft(cc[x][y-1]);
				
				if (cc[x-1][y-1] != null)
					setTopLeft(cc[x-1][y-1]);
				
				if (cc[x+1][y-1] != null)
					setBottomLeft(cc[x+1][y-1]);
			
			/* Normal Case checking here */
			} else {
				if (cc[x][y-1] != null)
					setLeft(cc[x][y-1]);
				
				if (cc[x-1][y-1] != null)
					setTopLeft(cc[x-1][y-1]);
				
				if (cc[x-1][y] != null)
					setTopRight(cc[x-1][y]);
				
				if (cc[x][y+1] != null)
					setRight(cc[x][y+1]);
				
				if (cc[x+1][y] != null)
					setBottomRight(cc[x+1][y]);
				
				if (cc[x+1][y-1] != null)
					setBottomLeft(cc[x+1][y-1]);
				
			}
			
		} else { // odd row
			
			/* Special case checking first */
			if (y == 0) { // Case index of [3][0] or [5][0]
				if (cc[x][y+1] != null)
					setRight(cc[x][y+1]);
				
				if (cc[x-1][y+1] != null)
					setTopRight(cc[x-1][y+1]);
				
				if (cc[x+1][y+1] != null)
					setBottomRight(cc[x+1][y+1]);
			
			/* Normal Case checking here */
			} else { 
				if (cc[x][y-1] != null)
					setLeft(cc[x][y-1]);
				
				if (cc[x-1][y] != null)
					setTopLeft(cc[x-1][y]);
				
				if (cc[x-1][y+1] != null)
					setTopRight(cc[x-1][y+1]);
				
				if (cc[x][y+1] != null)
					setRight(cc[x][y+1]);
				
				if (cc[x+1][y+1] != null)
					setBottomRight(cc[x+1][y+1]);
				
				if (cc[x+1][y] != null)
					setBottomLeft(cc[x+1][y]);
				
			}
		}
	}
	
	/* Private method to set points for this hexagon */
	private Double[] makeVertices(double radius, int sides){
		Double[] vertices = new Double[sides * 2];

		int indexInVerticesArray = 0;

		for(int i = 1; i <= sides; i++){
			vertices[indexInVerticesArray++] = radius * Math.cos((2*Math.PI*i)/sides);//x coordinate
			vertices[indexInVerticesArray++] = radius * Math.sin((2*Math.PI*i)/sides);//y coordinate
		}

		return vertices; 

	}
	
	public void countPizs() {
		switch(this.getType()) {
			case BLACK:
				b.getLogic().setCountBlack(b.getLogic().countBlack()+1);
				break;
			case WHITE:
				b.getLogic().setCountWhite(b.getLogic().countWhite()+1);
				break;
			default:
				break;
		}
	}
	
	/***************** PIECES METHODS ******************/
	
	/* Method to change piece attributes 
	 * when the piece's type changes on the where the cell's state changes */
	public void setAtrbs(int pieceType, int state) {
		switch(pieceType) {
			case EMPTY:
				if (state == MOVABLE)
					piece.setFill(Color.HOTPINK);
				else
					piece.setFill(Color.PINK);
				break;
				
			case BLACK:
				piece.setFill(Color.BLACK);
				break;
				
			case WHITE:
				piece.setFill(Color.WHITE);
				break;
				
			default:
				System.out.println("Invaild pieceType");
		}
	}
	
	/* Method to set hover color */
	public void setPizHoverColor(int type, int state) {
		
		switch (type) {
		case BLACK:
			if (piece.isHover())
				piece.setFill(Color.gray(0.3));
			else
				piece.setFill(Color.BLACK);
			break;
		case WHITE: 
			if (piece.isHover())
				piece.setFill(Color.gray(0.75));
			else
				piece.setFill(Color.WHITE);
			
			break;
			
		case EMPTY:
			if (state == MOVABLE) {
				if (piece.isHover())
					piece.setFill(Color.DEEPPINK);
				else
					piece.setFill(Color.HOTPINK);
			} else {
				if (piece.isHover())
					piece.setFill(Color.web("#ff99aa"));
				else
					piece.setFill(Color.PINK);
			}
			
			break;
			
		default :
				System.out.println("Invaild type for setPizHoverColor()");
		}
	}
	
	/********************* END OF PIECES METHODS *********************/
	
	@Override
	public void resize(final double width, final double height) {
		super.resize(width, height);
		piece.setRadius((width*Math.sqrt(3.0)/2)/2 - 5);
		hex.getPoints().addAll(makeVertices(width/2, sides));
	}
	
	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
		hex.relocate(x, y); piece.relocate(x, y);
	}
	

    public int getState() { return state.get(); }
	
	public void setState(int status) { state.set(status); }
	
	public void setType(int player) { type.set(player);; }
	
	public int getIndexRow() { return index[0]; }
	
	public int getIndexCol() { return index[1]; }
	
	public void setIndex(int i, int j) { index[0] = i; index[1] = j; }
	
	public Polygon getHex() { return hex; }

	public Circle getCircle() { return piece; }
	
	public IntegerProperty getTypeProperty() { return type; }
	
	public int getType() { return type.get(); }

	
	/* Methods to return nodes surrounding this node */
	/************************************************
	 * 
	 * 			(cell(1))	(cell(2))
	 * 		(cell(0)) (this.Cell) (cell(3))
	 * 			(cell(5))	(cell(4))
	 * 
	 ************************************************/
	public ObjectProperty<CellControl> surrCellProperty(int i) { return ob[i]; }
	
	public CellControl getSurr(int x) { return ob[x].get(); }
	
	/* Methods to return nodes */
	public CellControl left() { return ob[0].get(); }
	public CellControl topLeft() { return ob[1].get(); }
	public CellControl topRight() { return ob[2].get(); }
	public CellControl right() { return ob[3].get(); }
	public CellControl bottomRight() { return ob[4].get(); }
	public CellControl bottomLeft() { return ob[5].get(); }
	
	/* Methods to set these nodes */
	public void setLeft(CellControl c) { ob[0].set(c); }
	public void setTopLeft(CellControl c) { ob[1].set(c); }
	public void setTopRight(CellControl c) { ob[2].set(c); }
	public void setRight(CellControl c) { ob[3].set(c); }
	public void setBottomRight(CellControl c) { ob[4].set(c); }
	public void setBottomLeft(CellControl c) { ob[5].set(c); }

}

/* The original ideal was that:
 * build a cellControl.java and a piece.java on the top of the 
 * cellControl.java. but the fact is that resize() and relocate()
 * were not easy to deal with. So I combined two
 * Still can't fix the resize() and relocate() methods
 * I also keep my piece.java in the package
 */

