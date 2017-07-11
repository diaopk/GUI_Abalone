package GUI_Abalone;

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
	private ObjectProperty<CellControl>[] cell; // Array of nodes surrounding this node
	private int[] index; // Index in the game grid
	private Circle piece;
	
	/* Each piece is identified by type and state
	 * if one of these value changes there will be 
	 * changes to its corresponding values.
	 * type is the player
	 * state is one of selected, unselected, movable.
	 */
	private IntegerProperty type, state;
	
	@SuppressWarnings("unchecked")
	CellControl(int player, Board board) {
		/* Initialise them */
		b = board;
		hex = new Polygon();
		state = new SimpleIntegerProperty(UNSELECTED);
		cell = (ObjectProperty<CellControl>[]) new ObjectProperty[6]; 
		type = new SimpleIntegerProperty(player);
		piece = new Circle();
		
		/* index hold two elements, row and column */
		index = new int[2];
		
		index[0] = 0;
		index[1] = 0;
	
		/* Initialise 6 surrounding cells' state */
		for (int i = 0; i < 6; i++)
			cell[i] = new SimpleObjectProperty<CellControl>(null);
		
		this.setSkin(new CellControlSkin(this));
		
		/* Initialise the hex and piece */
		hex.getPoints().addAll(makeVertices(b.getStage().getWidth()* Math.sqrt(3.0)/27, 6));
		
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
				moveAndPush();
			}
			
		});
		
		/* When state changes */
		state.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				/* oldValue is previous state */
				/* newValue is changed state */
				setStateAtrbs((int)newValue, (int)oldValue);
				
			}
			
		});
		
		/* When piece's type changes */
		getTypeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				/* oldValue is previous type */
				/* newValue is new type */
				setAttributes((int)newValue, getState());
				/*System.out.println("Type: "+getType()+"| old: "+oldValue);
				System.out.println("         new: "+newValue+"\n");
				/* I need to check pieces if they meet to be pushable below*/
				//checkPieceForPush((int)newValue);
			}
			
		});
		
		/* When a piece is hover */
		/* Set hovered piece's colour */
		getCircle().hoverProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				setPieceHoverColor(getType(), getState());
			}
			
		});
		
		/************ End of Events ***************/
	
	}
	
	/**************** METHOD move and push START ******************/
	
	/* Method to move pieces when they are able to move */
	/* Method Description:
	 * This method assumes that this cell is going to be placed pieces
	 * and check surrounding pieces to find the selected one.
	 * If found selected pieces it then is capable of being placed pieces.
	 */
	/* This method move(place) piece depending on three conditions
	 * 1. One-piece movement: assume this cell is the cell is going to be placed a piece
	 * check surrounding cells for selected piece. If found and this cell is clicked then place a piece that is selected
	 * 2. Two-piece movement: Two-pieces move straight and not straight..i don't know how to say it
	 * 3. Three-piece movement: 
	 */
	public void moveAndPush() {
		/* Normal checking from line 161 to line 165 */
		if (getType() == EMPTY && getState() == MOVABLE ||
			getType() != b.getLogic().getCurrentPlayer() && getType() != EMPTY)
			for (int x = 0; x < 6; x++)
				if (getSurr(x) != null)
					if (getSurr(x).getState() == SELECTED &&
						getSurr(x).getType() == b.getLogic().getCurrentPlayer()) {
						/* getSurr(x) is the most surrounding-selected piece */
						
						if (b.getLogic().numPizSelected() == 1) { // One piece movement
							if (getType() == EMPTY) {
							setType(getSurr(x).getType());
							getSurr(x).setType(EMPTY);
							
							b.getLogic().nextPlayer(getType());
							}
						} else if (b.getLogic().numPizSelected() == 2) { // Two pieces' movement and push
							/* If the surrounding piece is selected and is not null */
							if (getSurr(x).selectedPiece(0) != null)
								/* If two next-to pieces are the same type and both selected */
								if (getSurr(x).selectedPiece(0).getType() == getSurr(x).getType())
									/* Assume this object(cell) is going to be placed with a new piece
									 * If the surrounding piece of this cell is selected 
									 * and that surrounding piece's surrounding is also selected
									 * and these two surrounding are the same direction with this cell
									 */
									
									if (selectedPizPos(0) == getSurr(x).selectedPizPos(0)) {
										/* Two-pieces straight movement or push */
										
										/* Create Two local variables for easy use */
										/* The piece most next to this cell */
										CellControl piece1 = getSurr(x);
										/* The piece most next to the piece1 */
										CellControl piece2 = getSurr(x).selectedPiece(0);
										
										if (getType() == EMPTY) { // Move!
											//System.out.println(getType());
											setType(piece1.getType());
											piece1.setType(getSurr(x).selectedPiece(0).getType());
											piece2.setType(EMPTY);
											
											b.getLogic().nextPlayer(getType());
											
										} else { // Push!
											int opposite = opposite(x);
											
											if (getSurr(opposite) != null) {
												System.out.println("two piece - push");
												if (getSurr(opposite).getType() == EMPTY) {
													getSurr(opposite).setType(getType());
													setType(getSurr(x).getType());
													getSurr(x).getSurr(x).setType(EMPTY);
													
													b.getLogic().nextPlayer(getType());
												}
											} else {
												System.out.println("two piece - push out");
												b.getLogic().changeCountPiece(getType());
												
												setType(getSurr(x).getType());
												getSurr(x).setType(getSurr(x).getSurr(x).getType());
												getSurr(x).getSurr(x).setType(EMPTY);
												
												b.getLogic().nextPlayer(getType());
											}
											
										}
									
									} else { // If the two surrounding pieces are not straight with this object
										/* This means this object is not a straight line with those
										 * then process another two-pieces movement
										 */
										/*
										 * ob[getSurr(x).selectedPizDirc(0)].get().getType() means 
										 * the type of the surrounding piece of this cell
										 */
										if (cell[getSurr(x).selectedPizPos(0)].get() != null &
												cell[getSurr(x).selectedPizPos(0)].get().getType() != selectedPiece(0).getType() &&
												cell[getSurr(x).selectedPizPos(0)].get().getType() == EMPTY) {
											
											setType(getSurr(x).getType());
											cell[getSurr(x).selectedPizPos(0)].get().setType(getSurr(x).selectedPiece(0).getType());
											getSurr(x).setType(EMPTY);
											getSurr(x).selectedPiece(0).setType(EMPTY);
								
											b.getLogic().nextPlayer(getType());
										
										}
									}
						} else if (b.getLogic().numPizSelected() == 3) { // Three pieces movement and push
							if (getSurr(x).selectedPiece(0) != null)
								/*
								 * (selectedPizPos(0) == getSurr(x).selectedPizPos(0) means that
								 * if this and three selected pieces are form one line.
								 */
								if (selectedPizPos(0) == getSurr(x).selectedPizPos(0) &&
										getSurr(x).selectedPizPos(0) == getSurr(x).selectedPiece(0).selectedPizPos(0, getSurr(x))) {
									
									/* Three pieces movement */
									if (getType() == EMPTY) {
										setType(getSurr(x).getType());
										
										getSurr(x).setType(getSurr(x).selectedPiece(0).getType());
										
										getSurr(x).getSurr(getSurr(x).selectedPizPos(0)).setType(getSurr(x).selectedPiece(0).selectedPiece(0).getType());
									
										getSurr(x).selectedPiece(0).selectedPiece(0, getSurr(x)).setType(EMPTY);
										
										b.getLogic().nextPlayer(getType());
									
									/* Three pieces push */
									} else if (getType() != EMPTY && getType() != b.getLogic().getCurrentPlayer()) {
										System.out.println("get it");
										int opposite = opposite(x);
										
										/* One piece push out */
										if (getSurr(opposite) == null) {
											System.out.println("Three pieces - one piece push out");
											b.getLogic().changeCountPiece(getType());
											
											setType(getSurr(x).getType());
											getSurr(x).getSurr(x).setType(getSurr(x).getType());
											getSurr(x).getSurr(x).getSurr(x).setType(EMPTY);
											
											b.getLogic().nextPlayer(getType());
										
										/* one piece push */
										} else if (getSurr(opposite).getType() == EMPTY) {
											System.out.println("Three pieces - one piece push");
											
											getSurr(opposite).setType(getType());
											setType(getSurr(x).getType());
											getSurr(x).getSurr(x).setType(getSurr(x).getType());
											getSurr(x).getSurr(x).getSurr(x).setType(EMPTY);
											
											b.getLogic().nextPlayer(getType());
											
										/* Tow pieces push */	
										} else if (getSurr(opposite).getType() == getType()) {
											if (getSurr(opposite).getSurr(opposite) == null) {
												System.out.println("Three pieces - two pieces push out");
												b.getLogic().changeCountPiece(getType());
												
												getSurr(opposite).setType(getType());
												setType(getSurr(x).getType());
												getSurr(x).setType(getSurr(x).getSurr(x).getType());
												getSurr(x).getSurr(x).setType(getSurr(x).getSurr(x).getSurr(x).getType());
												getSurr(x).getSurr(x).getSurr(x).setType(EMPTY);
												
												b.getLogic().nextPlayer(getType());
												
											} else if (getSurr(opposite).getSurr(opposite).getType() == EMPTY) {
												System.out.println("Three pieces - two pieces push");
												
												getSurr(opposite).getSurr(opposite).setType(getSurr(opposite).getType());
												getSurr(opposite).setType(getType());
												setType(getSurr(x).getType());
												getSurr(x).setType(getSurr(x).getSurr(x).getType());
												getSurr(x).getSurr(x).setType(getSurr(x).getSurr(x).getSurr(x).getType());
												getSurr(x).getSurr(x).getSurr(x).setType(EMPTY);
												
												b.getLogic().nextPlayer(getType());
											}
										}
									}
									
								/* If the cell to move does not form a line with those three pieces */
								} else if (selectedPizPos(0) != getSurr(x).selectedPizPos(0)) {
									/*
									 * the selectedPiece(0) is the head, middle and tail one
									 * So we need to find out the index(or position) of the selectedPiece(0)
									 */
									/* Target position and current player */
									int target = opposite(selectedPizPos(0));
									int currentPlayer = b.getLogic().getCurrentPlayer();
									
									/* Condition 1 and 2 */
									if (getSurr(x).selectedPiece(0).selectedPiece(0, getSurr(x)) != null) { // 0-1-1 and 1-1-0
										CellControl piece1 = getSurr(x);
										CellControl piece2 = getSurr(x).selectedPiece(0);
										CellControl piece3 = getSurr(x).selectedPiece(0).selectedPiece(0, getSurr(x));
										
										if (piece1.selectedPizPos(0) == piece2.selectedPizPos(0, piece1) &&
												piece2.getSurr(target) != null && piece3.getSurr(target) != null &&
												piece1.getSurr(target).getType() == EMPTY &&
												piece2.getSurr(target).getType() == EMPTY &&
												piece3.getSurr(target).getType() == EMPTY) {
											
											/* Setup for movement */
											piece3.getSurr(target).setType(currentPlayer);
											piece2.getSurr(target).setType(currentPlayer);
											this.setType(currentPlayer);
											
											piece3.setType(EMPTY);
											piece2.setType(EMPTY);
											piece1.setType(EMPTY);
											
											b.getLogic().nextPlayer(currentPlayer);
											
											break;
										}
										
									/* Condition 3 */
									} else if (getSurr(x).selectedPiece(0, getSurr(0).selectedPiece(0)) != null) { // 1-0-1 
										CellControl piece1 = getSurr(x);
										CellControl piece2 = getSurr(x).selectedPiece(0);
										CellControl piece3 = getSurr(x).selectedPiece(0, piece2);
										if (piece2.selectedPizPos(0) == piece1.selectedPizPos(0, piece2) &&
												piece2.getSurr(target) != null && piece3.getSurr(target) != null &&
												piece1.getSurr(target).getType() == EMPTY &&
												piece2.getSurr(target).getType() == EMPTY &&
												piece3.getSurr(target).getType() == EMPTY) {
											//System.out.println("Condition 3");
											/* Setup for movement */
											piece2.getSurr(target).setType(currentPlayer);
											piece3.getSurr(target).setType(currentPlayer);
											this.setType(currentPlayer);
											
											piece3.setType(EMPTY);
											piece2.setType(EMPTY);
											piece1.setType(EMPTY);
											
											b.getLogic().nextPlayer(currentPlayer);
											
											break;
										}
									}
								}
						} /* End of the Three pieces movement and push */
					}
	}
	
	/********** FOLLOWING FOUR METHODS FOR ASSISTING IN MOVEMENTS and PUSH **********/
	
	/* Private method to return surrounding selected piece */
	private CellControl selectedPiece(int index) {
		/* There are 6 pieces surrounding this cell
		 * If reach the last surrounding piece
		 * then meaning no selected piece, return null, which means no other selected piece
		 * If not reach the last one then call the function itself until reach the last one
		 */
		if (index != 6) {
			if (this.getSurr(index) != null)
				if (this.getSurr(index).getState() == SELECTED)
					return this.getSurr(index);
				else
					return selectedPiece(index+1);
			else
				return selectedPiece(index+1);
		} else
			return null;
	}
	
	/* Private method to return the surrounding selected piece but not return the c */
	private CellControl selectedPiece(int index, CellControl c) {
		if (index != 6) {
			if (this.getSurr(index) != null)
				if (this.getSurr(index).getState() == SELECTED &
						getSurr(index) != c)
					return getSurr(index);
				else
					return selectedPiece(index+1, c);
			else 
				return selectedPiece(index+1, c);
		} else
			return null;
	}
	
	/* Private method to return the surrounding selected piece's position */
	private int selectedPizPos(int index) {
		/* If the index reaches the last one(6), return negative value
		 * If there is a null cell surrounding this object,
		 * plus one and continue this function.
		 * Until find a selected piece.
		 */
		//if (index == 6) {
			if (getSurr(index) != null) {
				if (getSurr(index).getState() == SELECTED)
					return index;
				else
					return selectedPizPos(index+1);
			} else 
				return selectedPizPos(index+1);
		//} else
			//return -1;
	}
	
	/* Private method to return surrounding selected piece's position */
	/* This method basically is similar to the previous one 
	 * Except it takes the CellControl as an argument for checking ...
	 */
	private int selectedPizPos(int index, CellControl c) {
		if (index != 6)
			if (getSurr(index) == this.selectedPiece(0, c) &
				getSurr(index) != c) // this line is what i want to talk about..
			return index;
		else
			return selectedPizPos(index+1, c);
		else
			return -1;
	}
	
	/* Private method to return the opposite position based on the index agrument */
	private int opposite(int index) {
		if (index == 0)
			return 3;
		else if (index == 1)
			return 4;
		else if (index == 2)
			return 5;
		else if (index == 3)
			return 0;
		else if (index == 4)
			return 1;
		else
			return 2;
	}
	/********************* METHOD move and push END *********************/
	
	/* Method to set state for an non-empty cell when Pressed */
	public void setStateWhenPressed() {
		if (getType() != EMPTY)
			/* Set to selected */
			if (getState() == UNSELECTED & 
				b.getLogic().getCurrentPlayer() == getType() &
				b.getLogic().numPizSelected() < 3)
				setState(SELECTED);
		
			/* Set to unselected */
			else if (getState() == SELECTED)
				setState(UNSELECTED);
				
	}
	
	/* Method to set surrounding nodes' colour when this node's state changes */
	private void setSurrPizs(int state) {
		if (state == SELECTED)
			setSurrPizsAtrbs(MOVABLE, SELECTED);
		else if (state == UNSELECTED)
			setSurrPizsAtrbs(UNSELECTED, UNSELECTED);
	}
	
	/* Private method to set attributes for a specific state */
	/* When the cell's state changes, then call this function
	 * changing corresponding attributes depending on what state it changes to
	 */
	private void setStateAtrbs(int newState, int oldValue) {
		switch (newState) {
			case UNSELECTED: // If Changes to UNSELECTED
				setAttributes(getType(), newState);
				hex.setFill(Color.DIMGRAY);
				changeNumSltedPiz(oldValue);
				setSurrPizs(UNSELECTED);
				break;
				
			case SELECTED: // If changes to SELECTED
				setAttributes(getType(), newState);
				hex.setFill(Color.LIGHTGRAY);
				changeNumSltedPiz(oldValue);
				setSurrPizs(SELECTED);
				
				break;
			case MOVABLE: // If changes to MOVABLE
				setAttributes(getType(), newState);
				//getPiece().setPizHoverColor(Color.DEEPPINK, Color.HOTPINK);
			
			case PUSHABLE:
				setAttributes(getType(), newState);
				break;
			default: // Debug info
				System.out.println("setStateAtrbs()");
		}
	}
	
	
	/* Private method to be used for setting surrounding's node colour and state
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
		if (state == UNSELECTED)
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
			
		else // This object is set to selected
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
	
	/***************** THE PIECE'S METHODS ******************/
	/* Pieces' methods are about changing pieces' colour */
	
	/* Method to change piece attributes 
	 * when the piece's type changes on the where the cell's state changes
	 */
	public void setAttributes(int pieceType, int state) {
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
				System.out.println("setAttributes()");
		}
	}
	
	/* Method to set hover colour */
	public void setPieceHoverColor(int type, int state) {
		
		switch (type) {
			case BLACK:
				if (state == PUSHABLE) {
					if (piece.isHover())
						piece.setFill(Color.AQUA);
					else
						piece.setFill(Color.BLACK);
				} else {
					if (piece.isHover())
						piece.setFill(Color.gray(0.3));
					else
						piece.setFill(Color.BLACK);
				}
				break;
			case WHITE:
				if (state == PUSHABLE) {
					if (piece.isHover())
						piece.setFill(Color.BISQUE);
					else
						piece.setFill(Color.WHITE);
				} else {
					if (piece.isHover())
						piece.setFill(Color.gray(0.75));
					else
						piece.setFill(Color.WHITE);
				}
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
		hex.getPoints().setAll(makeVertices(b.getStage().getWidth()* Math.sqrt(3.0)/27, sides));
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

	
	/* Methods to return nodes that are surrounding with this node */
	/************************************************
	 * 
	 * 			(cell(1))	(cell(2))
	 * 		(cell(0)) (this.Cell) (cell(3))
	 * 			(cell(5))	(cell(4))
	 * 
	 ************************************************/
	public ObjectProperty<CellControl> surrCellProperty(int i) { return cell[i]; }
	
	/* Method to retrieve the surrounding cells */
	public CellControl getSurr(int x) { return cell[x].get(); }
	
	/* Methods to return nodes */
	public CellControl left() { return cell[0].get(); }
	public CellControl topLeft() { return cell[1].get(); }
	public CellControl topRight() { return cell[2].get(); }
	public CellControl right() { return cell[3].get(); }
	public CellControl bottomRight() { return cell[4].get(); }
	public CellControl bottomLeft() { return cell[5].get(); }
	
	/* Methods to set these nodes */
	public void setLeft(CellControl c) { cell[0].set(c); }
	public void setTopLeft(CellControl c) { cell[1].set(c); }
	public void setTopRight(CellControl c) { cell[2].set(c); }
	public void setRight(CellControl c) { cell[3].set(c); }
	public void setBottomRight(CellControl c) { cell[4].set(c); }
	public void setBottomLeft(CellControl c) { cell[5].set(c); }

}

/* The original ideal was that:
 * build a cellControl.java and a piece.java on the top of the 
 * cellControl.java. but the fact is that resize() and relocate()
 * were not easy to deal with. So I combined two
 * Still can't fix the resize() and relocate() methods
 * I also keep my piece.java in the package
 */

