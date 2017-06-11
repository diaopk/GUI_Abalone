package Main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.transform.Translate;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
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
				//c.setStroke(Color.BLACK);
				//this.setPizHoverColor(Color.web("#ff99aa"), Color.PINK);
				break;
				
			case BLACK: // Player black
				c.setFill(Color.BLACK);
				//c.setStroke(Color.WHITE);
				//this.setPizHoverColor(Color.gray(0.3), Color.BLACK);
				break;
				
			case WHITE: // Player white
				c.setFill(Color.WHITE);
				//c.setStroke(Color.BLACK);
				//this.setPizHoverColor(Color.gray(0.75), Color.WHITE);
				break;
				
			default:
				System.out.println("invaild type");
			
		}
		
		c.setStrokeWidth(0.0);
		c.getTransforms().add(pos);
		
		getChildren().add(c);
		
	}
	
	public Circle getCircle() { return c; }
	
	public IntegerProperty getTypeProperty() { return type; }
	
	public int getType() { return type.get(); }
	
	public void setType(int player) { type.set(player); }
	
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
	
	/* Method to set hover color */
	public void setPizHoverColor(Color hoverColor, Color origion) {
		
		c.hoverProperty().addListener(new ChangeListener<Object>() {
			
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (c.isHover())
					c.setFill(hoverColor);
				else
					c.setFill(origion);
			}	
		});

	}
	public void setPizHoverColor2(int type, int state) {
		
		
		switch (type) {
		case BLACK:
			if (c.isHover())
				c.setFill(Color.gray(0.3));
			else
				c.setFill(Color.BLACK);
			break;
		case WHITE: 
			if (c.isHover())
				c.setFill(Color.gray(0.75));
			else
				c.setFill(Color.WHITE);
			
			break;
			
		case EMPTY:
			if (state == MOVABLE) {
			if (c.isHover())
				c.setFill(Color.DEEPPINK);
			else
				c.setFill(Color.HOTPINK);
			} else {
				if (c.isHover())
					c.setFill(Color.web("#ff99aa"));
				else
					c.setFill(Color.PINK);
			}
			
			break;
			
			default :
				System.out.println("Invaild type for setPizHoverColor()");
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
		//c.relocate(x, y);
	}
	
}
