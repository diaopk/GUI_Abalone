package GUI_Abalone;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;


public class Test extends Application{
	public void init() {
	}
	
	public void start(Stage primaryStage) {

		VBox vb = new VBox();
		Button btn = new Button("Button");
		
		vb.getChildren().add(btn);
		Scene scene = new Scene(vb);
		primaryStage.setTitle("Abalone");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void stop() {}
	
	public static void main(String[] args) { launch(args); }
}
