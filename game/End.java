package game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class End extends Pipe{
	
	public End() {
		
	}
	public End(String property, int position) {
		super(false, property, position);
		setFill();
		setName("End");
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
	}
	
	@Override
	public void setFill() {
		if(getProperty().equals("Vertical")) {
			this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/endVertical.png"))));
		}
		else {
			this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/endHorizontal.png"))));
		}
	}
	

}
