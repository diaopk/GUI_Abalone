package GUI_Abalone;
//This class allows you to add a timer object to your application. 
//The timers "start" and "pause" method should be called from an appropriate location.

//this code has been adapted from http://asgteach.com/wp-content/uploads/2015/04/FXTimerBinding.java
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.KeyValue;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Timer extends Group implements Constants{

	/* Timer attributes */
	private Timeline timeline; // tracks the time
	private Label timerLabel;  // displays the time
	private Label playerLabel; // displays the player
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);  

	public Timer(Abalone a, String playerLabelString){		
		timerLabel = new Label(); 
		timerLabel.setId("label_timer");
		playerLabel = new Label(playerLabelString);
		getChildren().addAll(playerLabel, timerLabel);
		
		 // Bind the timerLabel text property to the timeSeconds property which means the label value updates as the timer counts.   
		timerLabel.textProperty().bind(timeSeconds.asString());
		
		timeline = new Timeline();
		
		//setting the timer with the start value
		//timeSeconds.set(STARTTIME);
		
		
		//specifing how the timer will opperate
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(STARTTIME+1),
						new KeyValue(timeSeconds, 0)));

		// this method execute when the timer is finished    
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//System.out.println("Player"+playerLabel.getText()+ " has run out of time");
				
				/* Turn to next player */
				a.getBoard().getLogic().nextPlayer(a.getBoard().getLogic().getCurrentPlayer());
			}
		});		
	}

	/* Method to start the timer */
	public void start() {
		timeline.playFromStart();
	}

	/* Method to pause the timer */
	public void pause(){
		timeline.pause();
	}
	
	/* Method to reset the timer */
	public void reset() { timeSeconds.set(STARTTIME);
		timerLabel.textProperty().set(timeSeconds.toString());
	}
	
}