package GUI_Abalone;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.transform.Translate;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/* This class represents two or more types of pieces */

public class Piece extends Group implements Constants{
	
	private Circle c;
	private Translate pos;
	
	/* Each piece is identified by type value
	 * if this value changes there will be 
	 * changes to its corresponding values
	 */
	private IntegerProperty type;
	
	Piece(int player) {
		type = new SimpleIntegerProperty(player);
		c = new Circle();
		pos = new Translate();
		
		switch (type.get()) {
			case EMPTY:
				c.setFill(Color.PINK);
				break;
				
			case BLACK: // Player black
				c.setFill(Color.BLACK);
				break;
				
			case WHITE: // Player white
				c.setFill(Color.WHITE);
				break;
				
			default:
				System.out.println("invaild type");
			
		}
		
		c.setStrokeWidth(0.0);
		c.getTransforms().add(pos);
		
		getChildren().add(c);
		
	}
	
	/* Method to change piece attributes 
	 * when the piece's type changes on the where the cell's state changes */
	public void setAtrbs(int pieceType, int state) {
		switch(pieceType) {
			case EMPTY:
				if (state == MOVABLE)
					c.setFill(Color.HOTPINK);
				else
					c.setFill(Color.PINK);
				break;
				
			case BLACK:
				c.setFill(Color.BLACK);
				break;
				
			case WHITE:
				c.setFill(Color.WHITE);
				break;
				
			default:
				System.out.println("Invaild pieceType");
		}
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
	
		c.setRadius((width*Math.sqrt(3.0)/2)-5);
	}
	
	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
	}
	
	public Circle getCircle() { return c; }
	
	public IntegerProperty getTypeProperty() { return type; }
	
	public int getType() { return type.get(); }
	
	public void setType(int player) { type.set(player); }
}
