package GUI_Abalone;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

public class Test_resize extends Application {

	@Override
	public void start(Stage s) throws Exception {
		// TODO Auto-generated method stub
		VBox vb = new VBox();
		Cell_test c = new Cell_test();
		
		vb.getChildren().add(c);
		
		Scene scene = new Scene(vb, 400,400);
		s.setTitle("Test resize");
		s.setScene(scene);
		s.show();
	}
	
	public static void main(String[] args) { launch(args); }

}
