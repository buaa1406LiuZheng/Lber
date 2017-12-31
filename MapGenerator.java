package lber;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class MapGenerator {
	/*generate a map according to the data in map file*/
	private final int MAX_X = 80;
	private final int MAX_Y = 80;
	
	private File mapfile;
	
	/**
	 * EFFECTS: constructs a(n) MapGenerator object
	 */
	public MapGenerator() {
		mapfile =  new File("map.txt");
	}
	
	/**
	 * REQUIRES: map.txt exists and is readable, and the format is correct
	 * MODIFIES: none
	 * EFFECTS: if content of map.txt has correct format,
	 * and no IOException or other Exception occur, return a map object contain the information;
	 * otherwise return null;
	 */
	public Map generateMap() {
	
		boolean[][] horizonRoad = new boolean[MAX_X-1][MAX_Y];
		boolean[][] verticalRoad = new boolean[MAX_X][MAX_Y-1];
		
		try (Scanner in = new Scanner(mapfile);){
			
			if(!mapfile.exists()){
				//map file dosen't exist
				throw new MapFileException("map file dosen't exist");
			}
			
			for(int y=MAX_Y-1;y>=0;y--){
				if(!in.hasNextLine()){
					throw new MapFileException("Map File Format incorrect");
				}
				String s = in.nextLine();
				if(!s.matches("[0-3]{80}")){
					throw new MapFileException("Map File Format incorrect");
				}
				
				for(int x=0;x<MAX_X;x++){
					char token = s.charAt(x);
					if(isValidToken(token, x, y)){
						switch (token) {
						case '0':
							break;
						case '1':
							horizonRoad[x][y] = true;
							break;
						case '2':
							verticalRoad[x][y-1] = true;
							break;
						case '3':
							horizonRoad[x][y] = true;
							verticalRoad[x][y-1] = true;
							break;
						default:
							break;
						}
					}
					else{
						throw new MapFileException("Map File Format incorrect");
					}
				}		
			}	
			
		}catch(MapFileException e){
			System.out.println(e.getMessage());
			return null;
		}catch (IOException e) {
			System.out.println("can't access mapfile");
			return null;
		}catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		Map map = new Map(horizonRoad, verticalRoad);
		return map;
	}
	
	/**
	 * REQUIRES: token is '0', '1', '2', or '3'; and 0<=x<MAX_X, 0<=y<MAX_Y
	 * MODIFIES: none
	 * EFFECTS: if x is right border and token is '1' or '3', or 
	 * y is bottom border and token is '2' or '3', return false;
	 * otherwise return true;
	 */
	private boolean isValidToken(char token,int x,int y){
		//check if token at (x,y) is valid
		if(x == MAX_X-1 && (token == '1'||token == '3')){
			return false;
		}
		if(y == 0 && (token == '2'||token == '3')){
			return false;
		}
		return true;
	}
	
}
