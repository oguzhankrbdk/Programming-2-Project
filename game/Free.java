package game;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Free extends Tile{

	public Free(int position) {
		super(false, position);
		setFill();
		setName("Free");
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
	}
	
	@Override
	public void setFill() {
		this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/emptyFree.png"))));
	}

}
