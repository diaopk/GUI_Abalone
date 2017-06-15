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

public class Timer extends Group {

	private static final Integer STARTTIME = 300; // set the start time as 300 seconds i.e. 5 mins
	private Timeline timeline; // tracks the time
	private Label timerLabel;  // displays the time
	private Label playerLabel; // displays the player
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);  

	public Timer(Abalone a, String playerLabelString){		
		this.timerLabel = new Label(); 
		this.playerLabel = new Label(playerLabelString);
		this.getChildren().addAll(this.playerLabel, this.timerLabel);
		 // Bind the timerLabel text property to the timeSeconds property which means the label value updates as the timer counts.   
		timerLabel.textProperty().bind(timeSeconds.asString());
		//styling of the timer
		timerLabel.setTextFill(Color.ALICEBLUE);
		timerLabel.setStyle("-fx-font-size: 1.5em;"); 
		timeline = new Timeline();
		//setting the timer with the start value
		timeSeconds.set(STARTTIME);
		//specifing how the timer will opperate
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(STARTTIME+1),
						new KeyValue(timeSeconds, 0)));

		// this method execute when the timer is finished    
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Player"+playerLabel.getText()+ " has run out of time");
				
				/* Turn to next player */
				a.getBoard().getLogic().nextPlayer(a.getBoard().getLogic().getCurrentPlayer());
			}
		});		
	}

	// call this method in the appropriate location to start the timer 
	public void start() {
		timeline.playFromStart();
	}

	// call this method in the appropriate location to pause the timer
	public void pause(){
		timeline.pause();
	}
	
}