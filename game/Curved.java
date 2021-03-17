package game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Curved extends Pipe{
	public Curved(boolean isMovable, String property, int position) {
		super(isMovable, property, position);
		setFill();
		setIsMovable(isMovable);
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
		setProperty(property);
		setName("CurvedPipe");
	}
	
	@Override
	public void setFill() {
		if(isMovable()) {
			if(getProperty().equals("00")) {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/pipeCurved1.png"))));
			}
			else if(getProperty().equals("01")) {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/pipeCurved2.png"))));
			}
			else if(getProperty().equals("10")) {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/pipeCurved3.png"))));
			}
			else {
				this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/pipeCurved4.png"))));
			}
		}
		else {
			this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/staticPipeCurved.png"))));
		}
		
	}

}
