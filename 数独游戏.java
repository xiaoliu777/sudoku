import java.util.*;
import java.lang.*;
import java.io.*;


public class Game {
	
	Board sudoku;
	
	public class Cell{
		private int row = 0;
		private int column = 0;
		
		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		public int getRow() {
			return row;
		}
		public int getColumn() {
			return column;
		}
	}
	
	public class Region{
		private Cell[] matrix;
		private int num_cells;
		public Region(int num_cells) {
			this.matrix = new Cell[num_cells];
			this.num_cells = num_cells;
		}
		public Cell[] getCells() {
			return matrix;
		}
		public void setCell(int pos, Cell element){
			matrix[pos] = element;
		}
		
	}
	
	public class Board{
		private int[][] board_values;
		private Region[] board_regions;
		private int num_rows;
		private int num_columns;
		private int num_regions;
		
		public Board(int num_rows,int num_columns, int num_regions){
			this.board_values = new int[num_rows][num_columns];
			this.board_regions = new Region[num_regions];
			this.num_rows = num_rows;
			this.num_columns = num_columns;
			this.num_regions = num_regions;
		}
		
		public int[][] getValues(){
			return board_values;
		}
		public int getValue(int row, int column) {
			return board_values[row][column];
		}
		public Region getRegion(int index) {
			return board_regions[index];
		}
		public Region[] getRegions(){
			return board_regions;
		}
		public void setValue(int row, int column, int value){
			board_values[row][column] = value;
		}
		public void setRegion(int index, Region initial_region) {
			board_regions[index] = initial_region;
		}	
		public void setValues(int[][] values) {
			board_values = values;
		}

	}
	
	private boolean search(int index, int num_regions, int k, int rows, int columns){
		if( index==num_regions ) return true;
		
		Region region = sudoku.getRegion(index);
		Cell[] cell = region.getCells();
		int region_size = cell.length;
		if( k==region_size ) return search(index+1, num_regions, 0, rows, columns);
		
		int x = cell[k].getRow(), y = cell[k].getColumn();
		if( sudoku.getValue(x,y)!=-1 ) return search(index, num_regions, k+1, rows, columns);
		
		int[] dir = {-1,-1, -1,0, -1,1, 0,-1, 0,1, 1,-1, 1,0, 1,1};
		for(int i=1; i<=region_size; i++){
			boolean ok = true;
			for(int di=0; di<16 && ok; di+=2){
				int x2 = x+dir[di], y2 = y+dir[di+1];
				if( 0<=x2 && x2<rows && 0<=y2 && y2<columns && sudoku.getValue(x2,y2)==i )
					ok = false;
			}
			for(int j=0; j<region_size; j++){
				int x2 = cell[j].getRow(), y2 = cell[j].getColumn();
				if( sudoku.getValue(x2,y2)==i ) ok = false;
			}
			if( ok ){
				sudoku.setValue(x,y,i);
				if( search(index,num_regions,k+1,rows,columns) ) return true;
				sudoku.setValue(x,y,-1);
			}
		}
		return false;
	}
	
	public int[][] solver() {
		//To Do => Please start coding your solution here

		int[][] boards = sudoku.getValues();
		int rows = boards.length, columns = boards[0].length;
		Region[] regions = sudoku.getRegions();
		int num_regions = regions.length;
		
		search(0,num_regions,0,rows,columns);
		
		return sudoku.getValues();
	}

	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int rows = sc.nextInt();
		int columns = sc.nextInt();
		int[][] board = new int[rows][columns];
		//Reading the board
		for (int i=0; i<rows; i++){
			for (int j=0; j<columns; j++){
				String value = sc.next();
				if (value.equals("-")) {
					board[i][j] = -1;
				}else {
					try {
						board[i][j] = Integer.valueOf(value);
					}catch(Exception e) {
						System.out.println("Ups, something went wrong");
					}
				}	
			}
		}
		int regions = sc.nextInt();
		Game game = new Game();
	    game.sudoku = game.new Board(rows, columns, regions);
		game.sudoku.setValues(board);
		for (int i=0; i< regions;i++) {
			int num_cells = sc.nextInt();
			Game.Region new_region = game.new Region(num_cells);
			for (int j=0; j< num_cells; j++) {
				String cell = sc.next();
				String value1 = cell.substring(cell.indexOf("(") + 1, cell.indexOf(","));
				String value2 = cell.substring(cell.indexOf(",") + 1, cell.indexOf(")"));
				Game.Cell new_cell = game.new Cell(Integer.valueOf(value1)-1,Integer.valueOf(value2)-1);
				new_region.setCell(j, new_cell);
			}
			game.sudoku.setRegion(i, new_region);
		}
		int[][] answer = game.solver();
		for (int i=0; i<answer.length;i++) {
			for (int j=0; j<answer[0].length; j++) {
				System.out.print(answer[i][j]);
				if (j<answer[0].length -1) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	


}


