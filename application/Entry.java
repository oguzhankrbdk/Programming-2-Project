package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.FileNotFoundException;
import application.Game;
import application.Main;

public class Entry {

	public Entry() throws FileNotFoundException {
		Pane entryPane = new Pane();
		Scene entryScene = new Scene(entryPane, 840, 600);
		entryPane.getStyleClass().add("entryPane");
		entryScene.getStylesheets().add("application/application.css");

		Rectangle header = new Rectangle(500, 170);
	
		header.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Images/gui/label.png"))));

		
		header.setLayoutX(170);
		header.setLayoutY(50);

		Button start = new Button();
		ImageView buttonImage = new ImageView(new Image(getClass().getResourceAsStream("Images/gui/play.png")));
	
		start.setGraphic(buttonImage);
		start.setBackground(null);


		Game game = new Game();
		
		Label me = new Label("Oðuzhan Karabudak 150118005");
		me.getStyleClass().add("me");
		me.setLayoutX(10);
		me.setLayoutY(570);

	
		start.setLayoutX(210);
		start.setLayoutY(233);


		start.setOnMousePressed(e -> {
			Main.getStage().setScene(game.getScene());
			Main.getStage().show();
		});

		entryPane.getChildren().addAll(start, header, me);
		Main.getStage().setScene(entryScene);
		Main.getStage().show();

	}

}
