package GUI_Abalone;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/* The main java file to show the application */

public class Abalone extends Application implements Constants{
	
	private VBox vb;
	private HBox hb;
	private Board board;
	private Timer t;
	private Button bt1;
	private Label lb1, lb2;
	private Circle c;
	private Scene scene;
	
	public void init() {
		
	}
	
	public void start(Stage primaryStage) {
		/* Two layouts */
		vb = new VBox();
		hb = new HBox();
		
		/* Reset button */
		bt1 = new Button("RESET GAME");
		
		/* Label to display current player */
		lb1 = new Label("Current Player:");
		lb2 = new Label("Black Piece(s): 14");
		
		/* A circle to indicate the current player */
		c = new Circle();
		
		/* A Timer instance */
		t = new Timer(this);
		
		/* A Board instance */
		board = new Board(this, primaryStage);
		
		/* Setup for the circle */
		c.setRadius(9.0);
		c.setFill(Color.BLACK);
		c.setStroke(Color.WHITE);
		c.setStrokeWidth(0);
		c.setTranslateY(3.0);
		
		board.setStyle("-fx-padding: 30;");
		
		/* Set Id for each object */
		lb1.setId("label_1");
		lb2.setId("label_2");
		bt1.setId("button");
		c.setId("c");
		hb.setId("hb");
		vb.setId("vb");
		
		/* Start the timer */
		t.start();
		
		hb.getChildren().addAll(bt1, lb1, c, lb2, t);
		
		vb.getChildren().addAll(hb, board);
		
		scene = new Scene(vb, WINDOW_SIZE, WINDOW_SIZE);
		/* Get the css style sheet */
		scene.getStylesheets().add(Abalone.class.getResource("Abalone.css").toExternalForm());
		
		primaryStage.setTitle("Abalone");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void stop(){}
	
	public static void main(String[] args) { launch(args); }
	
	public Circle barCircle() { return c; }
	
	public Label barLabel_1() { return lb1; }
	
	public Label barLabel_2() { return lb2; }
	
	public Button barButton() { return bt1; }
	
	public Board getBoard() { return board; }
	
	public Timer getTimer() { return t; }
	
	public HBox hb() { return hb; }
	
}
