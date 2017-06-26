package GUI_Abalone;

import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Cell_test extends Control {
	private Polygon p;
	
	public Cell_test() {
		p = new Polygon();
		p.setFill(Color.ALICEBLUE);
		p.setStroke(Color.AQUA);
		
		this.setSkin(new Cell_test_skin(this));
		
		getChildren().add(p);
		
	}
	
	private Double[] makeVertices(double radius, int sides){
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
		double s = width/2;
		p.getPoints().addAll(makeVertices(s,6));
		System.out.println("width: "+width+", height: "+height);
		System.out.println("resize() call");
	}
	
	public void relocate(double x, double y) {
		super.relocate(x, y);
		p.relocate(x, y);
		System.out.println("relocate() call");
	}
	
}
