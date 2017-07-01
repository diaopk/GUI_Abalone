package GUI_Abalone;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Cell_test extends Control {
	private Polygon p;
	private SimpleDoubleProperty w;
	private SimpleDoubleProperty h;
	private Stage s;
	
	public Cell_test(Stage size) {
		
		s = size;
		
		w = new SimpleDoubleProperty(200.00);
		h = new SimpleDoubleProperty(200.00);
		
		p = new Polygon();
		p.setFill(Color.ALICEBLUE);
		p.setStroke(Color.AQUA);
		p.getPoints().addAll(makePoints(200, 6));
		
		setSkin(new Cell_test_skin(this));
		
		getChildren().add(p);
		
	}
	
	private Double[] makePoints(double radius, int sides){
		Double[] vertices = new Double[sides * 2]; // stuff to be returned

		int indexInVerticesArray = 0;

		for(int i = 1; i <= sides; i++){
			vertices[indexInVerticesArray++] = radius * Math.cos((2*Math.PI*i)/sides);//x coordinate
			vertices[indexInVerticesArray++] = radius * Math.sin((2*Math.PI*i)/sides);//y coordinate
		}

		return vertices; 

	}
	
	/* The problem is that each time calling this method, it automatically add a new polygon*/
	public void resize(double width, double height) {
		super.resize(width, height);
		System.out.println("ScreenWidht: "+s.getWidth());
		this.w.set(width/2);
		this.h.set(height/2);
		p.getPoints().setAll(makePoints(s.getWidth()/2, 6));
		System.out.println("width: "+width+", height: "+height);
		System.out.println("resize() call");
	}
	
	public void relocate(double x, double y) {
		super.relocate(x, y);
		p.relocate(x, y);
		System.out.println("relocate() call");
	}
	
	public double getScreenWidth() { return w.get(); }
	public double getScreenHeight() { return h.get(); }
	
}
