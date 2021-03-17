package application;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import application.Main;
import game.End;
import game.Pipe;
import game.Tile;
import javafx.animation.PathTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import application.Util;

public class Game {
	public static int currentLevel = 1;
	public static int TOTAL_MOVES = 0;
	private Scene rootScene;
	private Pane gamePane;
	private Tile[][] tiles;
	private ArrayList<Tile> swapArray;
	private ArrayList<Tile> path;
	private int MAX_LEVEL;
	private double prevX = 0;
	private double prevY = 0;
	private PathTransition pathTransition;
	private Circle ball;
	private boolean isAnimationPlaying;

	public Game() throws FileNotFoundException {
		/*
		 * Generating game pane and game scene and set their properties
		 */
		Pane rootPane = new Pane();
		Scene rootScene = new Scene(rootPane, 840, 600);
		Pane gamePane = new Pane();
		gamePane.maxWidth(400);
		gamePane.maxHeight(400);
		
		/* 
		 * Generating game util
		 */
		Util util = new Util();
		// Taking currentLevel's tileList from util class
		this.tiles = util.getList().get(currentLevel - 1);
		this.gamePane = gamePane;
		this.rootScene = rootScene;
		
		MAX_LEVEL = util.getFileCount();
		//Taking path which is completed level, its only contain pipes.
		path = util.findPath(tiles);
		
		rootPane.getStyleClass().add("rootPane");
		gamePane.getStyleClass().add("gamePane");
		rootScene.getStylesheets().add("application/application.css");
		gamePane.setLayoutX(100);
		gamePane.setLayoutY(100);

		ball = new Circle(14);
		ball.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/ball.png"))));

		StackPane scoreTable = new StackPane();

		Rectangle exit = new Rectangle(287, 176);
		Rectangle score = new Rectangle(300, 134);

		Label scoreL = new Label("Total moves: " + TOTAL_MOVES);
		scoreL.getStyleClass().add("scoreL");

		exit.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/1.png"))));
		score.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/score.png"))));
		exit.setOnMousePressed(e -> {
			System.exit(1);
		});
		scoreTable.setLayoutX(520);
		scoreTable.setLayoutY(50);
		exit.setLayoutX(526);
		exit.setLayoutY(350);

		scoreTable.getChildren().addAll(score, scoreL);
		rootPane.getChildren().addAll(gamePane, exit, scoreTable);
		
		/*
		 * Adding tiles to gamePane.
		 */
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 4; column++) {
				gamePane.getChildren().add(tiles[row][column]);
				tiles[row][column].setLayoutX(column * 100);
				tiles[row][column].setLayoutY(row * 100);
				/*
				 * Place the ball in pane on starter tile.
				 */
				if (tiles[row][column] instanceof game.Starter) {
					ball.setLayoutX(tiles[row][column].getLayoutX() + 50);
					ball.setLayoutY(tiles[row][column].getLayoutY() + 50);
				}

			}
		}
		
		//Placing events on tiles.
		generateGame();

		gamePane.getChildren().addAll(ball);

		Main.getStage().setScene(rootScene);
		Main.getStage().show();

	}

	public Scene getScene() {
		return this.rootScene;
	}
	
	public Pane getGamePane() {
		return this.gamePane;
	}

	public Tile[][] getTileList() {
		return this.tiles;
	}

	public void generateGame() throws FileNotFoundException {
		swapArray = new ArrayList<>();
		
		/*
		 * Take all the tiles one by one and set their event handlers.
		 */
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 4; column++) {
				Tile tile = tiles[row][column];
				tile.setOnMousePressed(e -> {
					/*
					 * Taking first position of the tile so we can use after. We adding tile to swapArray. 
					 */
					prevX = tile.getPosX();
					prevY = tile.getPosY();
					swapArray.add(tile);
					/*
					 * We removing tile first and add again otherwise it is not going to be top priority.
					 */
					gamePane.getChildren().remove(tile);
					gamePane.getChildren().add(tile);
				});

				tile.setOnMouseDragged(e -> {
					/*
					 * While dragging we update tile position. (-100 for offset -50 for tile height or width/2)
					 */
					tile.setLayoutX(e.getSceneX() - 150);
					tile.setLayoutY(e.getSceneY() - 150);
				});

				tile.setOnMouseReleased(e -> {
					/*
					 * Checking for is mouse location on the gamePane or out.
					 */
					if ((100 <= e.getSceneX() && e.getSceneX() <= 500)
							&& (100 <= e.getSceneY() && e.getSceneY() <= 500)) {
						/*
						 * Finding the second tile via mouse layout and adding it to swapArray.
						 */
						Tile second = findTileViaMouse((int) e.getSceneX(), (int) e.getSceneY());
						swapArray.add(second);
						
						/*
						 * Checking tile position is it available to swap and checking type for is it end, starter or free tile.
						 */
						if (checkTilesPos(tile, second) && checkTileTypes(tile, second)) {

							swapTiles(swapArray.get(0), swapArray.get(1));
						} 
						/*
						 * Else replace it back.
						 */
						else {
							tile.setLayoutX(prevX);
							tile.setLayoutY(prevY);
							swapArray.clear();
						}
					/*
					 * Else replace it back.
					 */
					} else {
						tile.setLayoutX(prevX);
						tile.setLayoutY(prevY);
						swapArray.clear();
					}
					/*
					 * Checking for is path constructed after all moves if its constructed create new Level.
					 * Checking for is animation playing because when animation playing if we move any tile because of 
					 * isPathConstructed property is true its going to increase currentLevel by one. And we skip 2 levels.
					 * We can avoid this with isAnimationPlaying property.
					 */
					if (isPathConstructed(path) && !isAnimationPlaying) {
						try {
							newLevel();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}

				});

			}
		}
	}
	/*
	 * Take mouse position (-100 for offset) , find row and column indexes.
	 */
	public Tile findTileViaMouse(int x, int y) {
		x = (x - 100) / 100;
		y = (y - 100) / 100;
		return tiles[y][x];
	}
	/*
	 * We take 2 tile as an argument and swap all properties of them. We increased TOTAL_MOVES by one.
	 */
	public void swapTiles(Tile tile1, Tile tile2) {
		TOTAL_MOVES++;

		int tile1ColumnIndex = tile1.getColumnIndex();
		int tile1RowIndex = tile1.getRowIndex();

		int tile2ColumnIndex = tile2.getColumnIndex();
		int tile2RowIndex = tile2.getRowIndex();

		double pos2X = tile2.getLayoutX();
		double pos2Y = tile2.getLayoutY();

		int tile1GridPos = tile1.getGridPos();
		int tile2GridPos = tile2.getGridPos();
		/*
		 * If tile2 is tile1 itself then replace it back.
		 */
		if (tile1ColumnIndex == tile2ColumnIndex && tile1RowIndex == tile2RowIndex) {
			tile2.setLayoutX(prevX);
			tile2.setLayoutY(prevY);
			swapArray.clear();
			return;
		}

		gamePane.getChildren().remove(tile1);
		gamePane.getChildren().remove(tile2);

		tile1.setLayoutX(pos2X);
		tile1.setLayoutY(pos2Y);
		tile1.setPosX(pos2X);
		tile1.setPosY(pos2Y);

		tile2.setLayoutX(prevX);
		tile2.setLayoutY(prevY);
		tile2.setPosX(prevX);
		tile2.setPosY(prevY);

		gamePane.getChildren().add(tile1);
		gamePane.getChildren().add(tile2);

		tile1.setColumnIndex(tile2ColumnIndex);
		tile1.setRowIndex(tile2RowIndex);

		tile2.setColumnIndex(tile1ColumnIndex);
		tile2.setRowIndex(tile1RowIndex);

		tile1.setGridPos(tile2GridPos);
		tile2.setGridPos(tile1GridPos);

		tiles[tile1RowIndex][tile1ColumnIndex] = tile2;
		tiles[tile2RowIndex][tile2ColumnIndex] = tile1;

		swapArray.clear();
	}
	/*
	 * We take their row and column index and checking their position.
	 * I prefer subtract row and column indexes of tiles. And take abs value for each subtraction.
	 * Check if its == 1 it means they re next to them each other else return false.
	 */
	public boolean checkTilesPos(Tile tile1, Tile tile2) {
		int tile1ColumnIndex = tile1.getColumnIndex();
		int tile1RowIndex = tile1.getRowIndex();

		int tile2ColumnIndex = tile2.getColumnIndex();
		int tile2RowIndex = tile2.getRowIndex();

		if (tile1ColumnIndex == tile2ColumnIndex) {
			return Math.abs(tile1RowIndex - tile2RowIndex) == 1;
		} else if (tile1RowIndex == tile2RowIndex) {
			return Math.abs(tile1ColumnIndex - tile2ColumnIndex) == 1;
		} else {
			return false;
		}
	}
	/*
	 * Checking tile types for swap operation.
	 * If any of them(tile1 or tile2) instance of starter or end return false.
	 */
	public boolean checkTileTypes(Tile tile1, Tile tile2) {
		return (!(tile2 instanceof game.Starter || tile1 instanceof game.Starter)
				&& !(tile2 instanceof game.End || tile1 instanceof game.End) && tile2 instanceof game.Free);
	}
	
	/*
	 * we comparing tiles with path if tiles contain all of the tiles on same grid position then we return true otherwise false. 
	 */
	public boolean isPathConstructed(ArrayList<Tile> path) {
		/*
		 * We doing this with checking matchedTileNumber if it is same with path array size then it means path is constructed.
		 */
		int matchedTileNumber = 0;
		for (Tile pathTile : path) {
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 4; col++) {
					Tile tile = tiles[row][col];

					if (tile instanceof Pipe && ((Pipe) tile).getProperty().equals(((Pipe) pathTile).getProperty())
							&& tile.getGridPos() == pathTile.getGridPos()) {
						matchedTileNumber++;
					}
				}
			}
		}
		if (matchedTileNumber == path.size()) {
			return true;
		}
		return false;
	}
	/*After play animation check current level > max level if its true switch scene to result if its false create next level.*/
	public void newLevel() throws FileNotFoundException {
		playAnimation();
		pathTransition.setOnFinished(e -> {
			currentLevel++;
			if (currentLevel > MAX_LEVEL) {
				Main.getStage().setScene(new Result().getScene());
				Main.getStage().show();
			} else {
				try {
					Main.getStage().setScene(new Game().getScene());
				} catch (FileNotFoundException e1) {
				}
				Main.getStage().show();
			}
			/*
			 * We updating isAnimationPlaying property in this method when its finished we setting it to false.
			 */
			isAnimationPlaying = false;
		});

	}
	/*
	 * Taking path array and creating animation path and play it.
	 */
	public void playAnimation() {
		/*
		 * We updating isAnimationPlaying property in this method too when its started we setting it to true.
		 */
		isAnimationPlaying = true;
	
		ArrayList<PathElement> paths = new ArrayList<>();
		int x, y;
		int index = 0;
		/*
		 * While its not end check if tile is starter then we using moveTo method for creating start point of path.
		 * If its not starter were using LineTo method. 
		 * For x and y value we using gridPosToLayout methods we give tile's gridPos as an argument and take layout value
		 * adding 50 for reaching center of the tile.
		 */
		while (!(path.get(index) instanceof game.End)) {
			Pipe tile = (Pipe) path.get(index);
			if (tile instanceof game.Starter) {

				x = gridPosToLayoutX(tile.getGridPos()) + 50;
				y = gridPosToLayoutY(tile.getGridPos()) + 50;

				paths.add(new MoveTo(x, y));
			}
			x = gridPosToLayoutX(tile.getGridPos()) + 50;
			y = gridPosToLayoutY(tile.getGridPos()) + 50;
			paths.add(new LineTo(x, y));

			index++;
		}

		End endTile = (End) path.get(path.size() - 1);
		x = gridPosToLayoutX(endTile.getGridPos()) + 50;
		y = gridPosToLayoutY(endTile.getGridPos()) + 50;
		paths.add(new LineTo(x, y));
		/*
		 * We removing ball for firs priority. And set its layout (0,0)
		 * because when we setPath its going to add layout value to path's layout value.
		 */
		gamePane.getChildren().remove(ball);
		ball.setLayoutX(0);
		ball.setLayoutY(0);
		gamePane.getChildren().add(ball);
		pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.seconds(2));
		pathTransition.setCycleCount(1);

		pathTransition.setNode(ball);

		pathTransition.setPath(new Path(paths));

		pathTransition.play();
	}

	public int gridPosToLayoutX(int pos) {

		return ((pos - 1) % 4) * 100;
	}

	public int gridPosToLayoutY(int pos) {
		return ((pos - 1) / 4) * 100;
	}

}
