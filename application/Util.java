package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import game.Curved;
import game.Empty;
import game.End;
import game.Free;
import game.Pipe;
import game.Starter;
import game.Tile;

public class Util {
	private int fileCount;
	private ArrayList<Tile[][]> list;
	private Scanner reader;

	public Util() throws FileNotFoundException {
		this.list = generateTileList();
	}

	public int fileCount(File file) {
		return file.list().length;
	}
	/*
	 * Take file number in that direction and generate all the levels by calling generateSublist method and add them to ArrayList.
	 */
	public ArrayList<Tile[][]> generateTileList() throws FileNotFoundException {
		ArrayList<Tile[][]> list = new ArrayList<>();

		int fileNumber = fileCount(new File("application/LevelFile"));
		this.fileCount = fileNumber;
		String fileName = "";

		for (int i = 1; i <= fileNumber; i++) {
			fileName = "application/LevelFile/level" + i + ".txt";
			File file = new File(fileName);
			list.add(generateSublist(file));
		}
		return list;

	}
	/*
	 * Take file as an argument. Read the lines one by one. Split the lines to the string array. 
	 * Switch case for its property and create tiles. Add them to Tile array.
	 */
	public Tile[][] generateSublist(File file) throws FileNotFoundException {
		Tile[][] temp = new Tile[4][4];
		reader = new Scanner(file);
		ArrayList<Tile> tileList = new ArrayList<>();
		/*
		 * This index for tile positions.
		 */
		int index = 1;
		while (index != 17) {
			String[] tempName = new String[3];

			if (!reader.hasNextLine()) {
				return temp;
			}

			while (reader.hasNextLine()) {
				/*
				 * Taking the line and check if its null then go next line else move on.
				 */
				String line = reader.nextLine();
				if (line.length() != 0) {
					tempName = line.split(",");
					/*
					 * Switching second argument of level file which is type of tile. 
					 */
					switch (tempName[1]) {
						/*
						 * We do not need to classify them as vertical, horizontal or etc.
						 * I override setFill method for all tile classes and it can classify it by its property.
						 */
						case "Starter": {
							tileList.add(new Starter(tempName[2], index));
							index++;
							break;
						}
	
						case "End": {
							tileList.add(new End(tempName[2], index));
							index++;
							break;
						}
	
						case "Empty": {
							if (tempName[2].equals("none")) {
								tileList.add(new Empty(index));
								index++;
								break;
							}
							if (tempName[2].equals("Free")) {
								tileList.add(new Free(index));
								index++;
								break;
							}
						}
						/*
						 * We should check property for pipe because curved pipe is classified as pipe too.
						 */
						case "Pipe": {
							if (tempName[2].equals("Vertical") || tempName[2].equals("Horizontal")) {
								tileList.add(new Pipe(true, tempName[2], index));
								index++;
								break;
							} else {
								tileList.add(new Curved(true, tempName[2], index));
								index++;
								break;
							}
						}
	
						case "PipeStatic": {
							if (tempName[2].equals("Vertical") || tempName[2].equals("Horizontal")) {
								tileList.add(new Pipe(false, tempName[2], index));
								index++;
								break;
							} else {
								tileList.add(new Curved(false, tempName[2], index));
								index++;
								break;
							}
	
						}
	
						default: {
							break;
						}
					}

				}
			}

		}
		reader.close();
		/*
		 * We place it array list to out format.
		 */
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				temp[i][j] = tileList.get(k);
				k++;
			}
		}

		return temp;
	}

	public ArrayList<Tile[][]> getList() {
		return this.list;
	}
	
	/*
	 * Finding path.
	 */
	public ArrayList<Tile> findPath(Tile[][] tiles) {
		End endTile = new End();
		Starter starterTile = new Starter();

		ArrayList<Tile> path = new ArrayList<>();
		
		int rowGap, columnGap;

		for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
			for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
				Tile tile = tiles[rowIndex][columnIndex];
				/*
				 * We take the starter and end tile first we will use their positions to find path.
				 */
				if (tile instanceof Starter) {
					starterTile = (Starter) tile;
					path.add(starterTile);
				}

				else if (tile instanceof End) {
					endTile = (End) tile;
				}

			}
		}
		/*
		 * We subtract their RowIndex and ColumnIndex for row gap and column gap. 
		 */
		rowGap = endTile.getRowIndex() - starterTile.getRowIndex();
		columnGap = endTile.getColumnIndex() - starterTile.getColumnIndex();
		
		/*
		 * If row gap > 0 than end tile position is under the starter tile (for y axis).
		 */
		if (rowGap > 0) {
			
			int pos = 4;
			/*
			 * If both are vertical than we should start from starter tile and go to end tile.
			 * In this first while loop we should know that when rowGap is 0 it means we are same on same
			 * row position with end tile. We adding vertical pipes which is started from under the starter tile
			 * We increased rowGap by one and continue to add one after the other.
			 */
			if (starterTile.getProperty().equals("Vertical") && endTile.getProperty().equals("Vertical")) {
				while (rowGap != 0) {

					path.add(new Pipe(true, "Vertical", starterTile.getGridPos() + pos));
					pos += 4 + starterTile.getGridPos();
					rowGap--;

				}
				/*
				 * When we reach to same row position with end  we should add curved pipe. We assume that starter tile
				 * is left of the end tile. Increased pos by one because now we re on horizontal way.
				 */
				path.add(new Curved(true, "01", pos));
				pos++;
				/*
				 * We adding horizontal pipe while columnGap is equals 1. When its equals 1 we understand that 
				 * we should add last curved pipe for reach end tile. And of course we increasing pos by one.
				 */
				while (columnGap != 1) {

					path.add(new Pipe(true, "Horizontal", pos));
					pos++;
					columnGap--;

				}
				/*
				 * We adding last curved pipe.
				 */
				path.add(new Curved(true, "00", pos));
				
				
			}
			/*
			 * Case that starter tile is vertical and end tile is horizontal.
			 * In this case row gap should stop at 1 because we should add curved pipe to same row position with end tile.
			 */
			else if (starterTile.getProperty().equals("Vertical") && endTile.getProperty().equals("Horizontal")) {
				while (rowGap != 1) {

					path.add(new Pipe(true, "Vertical", starterTile.getGridPos() + pos));
					pos += 4;
					rowGap--;

				}
				pos++;
				path.add(new Curved(true, "01", pos));
				pos++;
				while (columnGap != 1) {

					path.add(new Pipe(true, "Horizontal", pos));
					pos++;
					columnGap--;

				}
			} 
			/*
			 * I didnt write this cases its going to be too much work and low possibility to for this level format.
			 */
			else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
			}

		}

		else {
			/*
			 * If row gap =< 0 than end tile position is above the starter tile (for y axis).
			 */
			
			int pos = 4;
			/*
			 * In this part we differently take reference as endTile because its position more available for me to implement.
			 * We differently increasing rowGap because its negative and decreasing pos some placeses because we move opposite way in
			 * x axis. 
			 */
			if (starterTile.getProperty().equals("Vertical") && endTile.getProperty().equals("Vertical")) {
				while (rowGap != 0) {

					path.add(new Pipe(true, "Vertical", endTile.getGridPos() + pos));
					pos += 4;
					rowGap++;

				}
				path.add(new Curved(true, "00", pos));
				pos--;
				while (columnGap != 1) {

					path.add(new Pipe(true, "Horizontal", pos));
					pos--;
					columnGap--;

				}
				path.add(new Curved(true, "01", pos));
			} 
			/*
			 * I didnt write this cases its going to be too much work and low possibility to for this level format.
			 */
			else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
			}

		}
		/*
		 * At the end we adding end tile because this way its going to exact position on array.
		 */
		path.add(endTile);
		return path;
	}

	public int getFileCount() {
		return this.fileCount;
	}
}
