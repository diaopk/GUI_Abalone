package GUI_Abalone;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;

public class Test_resize extends Application {

	@Override
	public void start(Stage s) throws Exception {
		// TODO Auto-generated method stub
		
		//Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		
		VBox vb = new VBox();
		Cell_test c = new Cell_test(s);
		
		vb.getChildren().add(c);
		
		Scene scene = new Scene(vb, 400,400);
		s.setMinHeight(s.getWidth());
		s.setTitle("Test resize");
		s.setScene(scene);
		s.show();
	}
	
	public static void main(String[] args) { launch(args); }

}
