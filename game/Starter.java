package game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Starter extends Pipe{
	
	public Starter() {
		
	}
	public Starter(String property, int position) {
		super(false, property, position);
		setFill();
		setName("Starter");
		setProperty(property);
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
		
	}
	
	@Override
	public void setFill() {
		if(getProperty().equals("Vertical")) {
			this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/starterVertical.png"))));
		}
		else {
			this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/starterHorizontal.png"))));
		}
	}

}
