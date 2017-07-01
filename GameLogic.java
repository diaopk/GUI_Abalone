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

public class GameLogic implements Constants{

	private Board board;
	private int round;
	private IntegerProperty current_player, countBlack, countWhite, winner;
	private int num_piz_selected;
	
	GameLogic(Board b) {
		board = b; 
		countBlack = new SimpleIntegerProperty(14); 
		countWhite = new SimpleIntegerProperty(14);
		current_player = new SimpleIntegerProperty(BLACK);
		winner = new SimpleIntegerProperty(EMPTY);
		round = 1;
		num_piz_selected = 0;
		
		/* When current player changes */
		current_player.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				clearSelectedPizs();
				setNumPizSelected(0);
				setBarState();
				//b.abalone().getTimer().start();
			}
			
		});
		
		/* Action When button of the bar is clicked */
		board.abalone().barButton().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
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
					b.abalone().hb().getChildren().add(new Label(winner(getWinner())));
					b.abalone().getTimer().pause();
				}
			}
			
		});
		
		//countWhite.addListener(new ChangeListener(WHITE, countWhite.get(), this));
		//countBlack.addListener(new ChangeListener(BLACK, countBlack.get(), this));
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
	public void clearSelectedPizs() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board.getCell()[i][j] != null)
					if (board.getCell()[i][j].getType() != getCurrentPlayer() &
					board.getCell()[i][j].getState() != UNSELECTED) {
						board.getCell()[i][j].setState(UNSELECTED);
					}
	}
	
	public int numPizSelected() { return num_piz_selected; }
	
	public void setNumPizSelected(int number) {
		if (number > 3 | number < 0)
			System.out.println("Exceed the limit");
		else
			num_piz_selected = number;
	}
	
	public void setBarState() {
		switch (getCurrentPlayer()) {
		case BLACK:
			board.abalone().barCircle().setFill(Color.BLACK);
			board.abalone().barCircle().setStroke(Color.WHITE);
			//board.abalone().barLabel_2().setText("Black Piece(s): "+countBlack.get());
			break;
			
		case WHITE:
			board.abalone().barCircle().setFill(Color.WHITE);
			board.abalone().barCircle().setStroke(Color.BLACK);
			//board.abalone().barLabel_2().setText("White Piece(s): "+countWhite.get());
			break;
			
		default:
			System.out.println("Abalone Error!");
		}
	
	System.out.println(board.getLogic().getCurrentPlayer());
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
	
	public void dropBlackPiece() { countBlack.subtract(1); }
	
	public void dropWhitePiece() { countWhite.subtract(1); }
	
	public void setWinner(int value) { winner.set(value); }
	
	public int getWinner() { return winner.get(); }
	
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
	
}

/* Class to generate a winner */
class CountChangeListener implements ChangeListener<Number>, Constants {

	private int count;
	private int player;
	private GameLogic gl;
	
	CountChangeListener(int p, int c, GameLogic g) {
		count = c;
		gl = g;
		player = p;
	}
	
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		// TODO Auto-generated method stub
		if (count == 1) {
			if (player == BLACK)
				gl.setWinner(WHITE);
			else
				gl.setWinner(BLACK);
	
		}	
	}
	
}