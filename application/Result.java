package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import application.Main;

import java.io.FileNotFoundException;

import application.Game;

public class Result {
	private Scene resultScene;
	private Pane resultPane;
	public Result() {
		/*
		 * Creating final scene.
		 */
		Pane resultPane = new Pane();
		Scene resultScene = new Scene(resultPane, 840, 600);
		this.resultPane = resultPane;
		this.resultScene = resultScene;
		System.out.print(Game.TOTAL_MOVES);
		resultPane.getStyleClass().add("resultPane");
		resultScene.getStylesheets().add("application/application.css");
		
		VBox finalPane = new VBox();
		finalPane.setLayoutX(270);
		finalPane.setLayoutY(100);
		
		StackPane scoreTable = new StackPane();
		
		Rectangle exit = new Rectangle(287,176);
		Rectangle score = new Rectangle(300,134);
		Rectangle home = new Rectangle(287,176);
		
		Label scoreL = new Label("Your Score: " + calculateScore());
		scoreL.getStyleClass().add("scoreL");
		
		exit.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/1.png"))));
		exit.setOnMousePressed(e->{
			System.exit(1);
		});
		score.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/score.png"))));
		home.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/2.png"))));
		home.setOnMousePressed(e->{
			try {
				Game.currentLevel = 1;
				Game.TOTAL_MOVES = 0;
				new Entry();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		
		scoreTable.getChildren().addAll(score, scoreL);
		finalPane.getChildren().addAll(scoreTable, home, exit);
		
		resultPane.getChildren().addAll(finalPane);
	
		Main.getStage().setScene(resultScene);
		Main.getStage().show();
	}
	
	public Scene getScene() {
		return resultScene;
	}
	
	public Pane getPane() {
		return resultPane;
	}
	/*
	 * You can make minimum 27 moves so i triple it and make this score system. 
	 */
	public int calculateScore() {
		int score = 181 - (Game.TOTAL_MOVES * 3);
		if(score > 0) {
			return score;
		}else {
			return 0;
			
		}
		
	}
}
