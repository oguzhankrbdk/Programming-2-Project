/*
 * Oðuzhan KARABUDAK 	150118005
 */
package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
public class Main extends Application {
	private static Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		primaryStage.setResizable(false);
		Main.primaryStage = primaryStage;
		new Entry();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getStage() {
		return primaryStage;
	}
}
