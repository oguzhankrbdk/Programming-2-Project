package game;

public class Empty extends Tile{

	public Empty(int position) {
		super(true, position);
		setFill();
		setName("Empty");
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
	}

}
