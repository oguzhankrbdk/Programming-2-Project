package game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Pipe extends Tile{
	private String property;
	
	public Pipe() {
		
	}
	public Pipe(boolean isMovable, String property, int position) {
		super(isMovable, position);
		setProperty(property);
		setFill();
		setIsMovable(isMovable);
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
		setName("Pipe");
	}
	
	
	public String getProperty() {
		return this.property;
	}
	
	public void setProperty(String prop) {
		this.property = prop;
	}
	
	@Override
	public void setFill() {
		if(isMovable()) {
			if(getProperty().equals("Vertical")) {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/pipeVertical.png"))));
			}
			else {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/pipeHorizontal.png"))));
			}
		}
		else {
			if(getProperty().equals("Vertical")) {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/staticPipeVertical.png"))));
			}
			else {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/staticPipeHorizontal.png"))));
			}
		}
		
	}
	
	@Override
	public String toString() {
		
		return "ID: " + getName() + " Property: " + getProperty() +  " PosX: " + getPosX() + " PosY: " + getPosY() + " GridPos: " + getGridPos() + " ColumnIndex: " + getColumnIndex() + " RowIndex: " + getRowIndex();
	}
	

}
