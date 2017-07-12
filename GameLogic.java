package GUI_Abalone;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/* This class sets up a game rule and calculates how a winner comes out */
/* This class controls all the logic stuff
 * like the number of pieces, setting the current player and the winner,
 * everything on the status bar.
 */

public class GameLogic implements Constants{

	private Board board;
	private int round;
	private IntegerProperty current_player, countBlack, countWhite, winner;
	private int num_piz_selected;
	
	/* Initialise the constructor */
	GameLogic(Board b) {
		board = b; 
		
		/* Initialise the number of those pieces are 14 */
		countBlack = new SimpleIntegerProperty(14); 
		countWhite = new SimpleIntegerProperty(14);
		
		/* Assume the first round is turn to black pieces */
		current_player = new SimpleIntegerProperty(BLACK);
		
		/* Initialise the winner is empty */
		winner = new SimpleIntegerProperty(EMPTY);
		
		
		round = 1;
		num_piz_selected = 0;
		
		/* When current player changes */
		current_player.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				/* Clean all the selected Pieces */
				cleanSelectedPizs();
				
				/* reset the number of the selected pieces */
				setNumPizSelected(0);
				
				/* Change the stateBar */
				setBarState();
				
				/* Reset the timer */
				board.abalone().getTimer().reset();//b.abalone().getTimer().reset();
			}
			
		});
		
		/* When the number of piece changes */
		/* When the number of pieces changes, status bar need to be updated as well */
		countBlack.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				setBarState();
			}
			
		});
		
		countWhite.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				setBarState();
			}
			
		});
		
		/* Action When button of the bar is clicked */
		board.abalone().barButton().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				// Reset the game when the button is clicked */
				board.reset();
			}
			
		});
		
		/* Display the winner on the bar */
		/* The pushing action does not finish,
		 * so winner will not show up T.T
		 */
		winner.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if (winner.get() != EMPTY) {
					board.abalone().hb().getChildren().add(new Label(winner(getWinner())));
					board.abalone().getTimer().pause();
				}
			}
			
		});
	}
	
	/* Method to set current_player to next player */
	public void nextPlayer(int curPlayer) {
		if (curPlayer+1 <= WHITE) {
			setCurrentPlayer(curPlayer+1);
			nextRound();
			
		} else
			nextPlayer(backToEmpty(curPlayer));
	}
	
	/* Method used by nextPlayer() */
	private int backToEmpty(int player) {
		while (player != 0) {
			player--;
		}
		return player;
	}
	
	/* Method to clear up all pieces that are selected but are not the current player */
	public void cleanSelectedPizs() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board.getCell()[i][j] != null)
					if (board.getCell()[i][j].getType() != getCurrentPlayer() &
					board.getCell()[i][j].getState() != UNSELECTED) {
						board.getCell()[i][j].setState(UNSELECTED);
					}
	}
	
	/* Method to return the number of pieces that are selected */
	public int numPizSelected() { return num_piz_selected; }
	
	/* Method to Set the number of selected pieces when pieces are selected */
	public void setNumPizSelected(int number) {
		if (number > 3 | number < 0)
			System.out.println("setNumPizSelected()");
		else
			num_piz_selected = number;
	}
	
	/* Method to Change the settings of the stateBar depending on the current player */
	public void setBarState() {
		switch (getCurrentPlayer()) {
		/* If the current player is the black piece */
		case BLACK:
			board.abalone().barCircle().setFill(Color.BLACK);
			board.abalone().barCircle().setStroke(Color.WHITE);
			board.abalone().barLabel_2().setText("Black Piece(s): "+countBlack.get());
			break;
		
			/* If the current player is the white player */
		case WHITE:
			board.abalone().barCircle().setFill(Color.WHITE);
			board.abalone().barCircle().setStroke(Color.BLACK);
			board.abalone().barLabel_2().setText("White Piece(s): "+countWhite.get());
			break;
			
		default:
			System.out.println("setBarState()");
		}
		
	}
	
	/* Method to change the number of pieces */
	public void changeCountPiece(int player) {
		if (player == BLACK)
			dropBlackPiece();
		else if (player == WHITE) 
			dropWhitePiece();
	}
	
	public String winner(int player) {
		switch (player) {
			case BLACK:
				return "Black";
			case WHITE:
				return "White";
			default:
				return "Error";
		}
	}
	
	public int getRound() { return round; }
	
	private void nextRound() { round++; }
	
	public void setCurrentPlayer(int player) { current_player.set(player); }
	
	public int getCurrentPlayer() { return current_player.get(); }
	
	public IntegerProperty currentPlayerProperty() { return current_player; }
	
	public int countBlack() { return countBlack.get(); }
	
	public IntegerProperty countBlackProperty() { return countBlack; }
	
	public void setCountBlack(int count) { countBlack.set(count); }
	
	public int countWhite() { return countWhite.get(); }
	
	public IntegerProperty countWhiteProperty() { return countWhite; }
	
	public void setCountWhite(int count) { countWhite.set(count); }
	
	public void dropBlackPiece() { countBlack.set(countBlack.get()-1);}
	
	public void dropWhitePiece() { countWhite.set(countWhite.get()-1);}
	
	public void setWinner(int value) { winner.set(value); }
	
	public int getWinner() { return winner.get(); }
	
}