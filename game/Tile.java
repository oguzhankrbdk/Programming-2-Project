package game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
/*
 * Column and row index is for Tile[][], position is grid position(1 - 16)
 * posX, posY is for pane layout.
 */
public class Tile extends Rectangle{
	private double posX;
	private double posY;
	private Image tileImage;
	private boolean isMovable;
	private int position;
	private int columnIndex;
	private int rowIndex;
	private String id;
	
	public Tile() {
		
	}
	public Tile(boolean isMovable, int position) {
		super(100, 100);
		this.position = position;
		setIsMovable(isMovable);
		setPosViaGridPos();
		setColumnIndexviaPos();
		setRowIndexviaPos();
		setName("Tile");
	}
	
	public boolean isMovable() {
		return this.isMovable;
	}
	
	public void setIsMovable(boolean x) {
		this.isMovable = x;
	}
	
	public Image getImage() {
		return this.tileImage;
	}
	
	public void setImage(Image image) {
		this.tileImage = image;
	}
	
	public double getPosX() {
		return this.posX;
	}
	
	public double getPosY() {
		return this.posY;
	}
	
	public void setPosX(double x) {
		this.posX = x;
	}
	
	public void setPosY(double y) {
		this.posY = y;
	}
	/*
	 * I will override it for all tile types so we can add its image when its creating.
	 */
	public void setFill() {
		this.setFill(new ImagePattern(new Image(Tile.class.getResourceAsStream("../application/Images/empty.png"))));
	}
	public void setGridPos(int i) {
		this.position = i;
	}
	
	public int getGridPos() {
		return this.position;
	}
	
	public int getColumnIndex() {
		return this.columnIndex;
	}
	
	public int getRowIndex() {
		return this.rowIndex;
	}
	
	public void setColumnIndexviaPos() {
		this.columnIndex = ((getGridPos() - 1) % 4);
	}
	
	public void setRowIndexviaPos() {
		this.rowIndex = ((getGridPos() - 1) / 4);
	}
	
	public void setPosViaGridPos() {
		this.posX = ((getGridPos() - 1) % 4) * 100;
		this.posY = ((getGridPos() - 1) / 4) * 100;
	}
	
	public void setColumnIndex(int x) {
		this.columnIndex = x;
	}
	
	public void setRowIndex(int y) {
		this.rowIndex = y;
	}
	
	@Override
	public String toString() {
		return "ID: " + this.id + " PosX: " + this.posX + " PosY: " + this.posY + " GridPos: " + this.position + " ColumnIndex: " + this.columnIndex + " RowIndex: " + this.rowIndex;
	}
	
	public void setName(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.id;
	}
	
	
}
