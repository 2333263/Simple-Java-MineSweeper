package myMineSweeper;
import java.util.*;
import javax.swing.*;
import java.awt.*;
public class Tile {
	private Coords coords;
	private int val;//value of tile, 0-8 for how many bombs its touching, -1 if it a bomb, -2 for flag
	private boolean clicked=false; //whether or not this tile has already been clicked
	private JButton Button;
	private ArrayList<Tile> bombs=new ArrayList<Tile>(); //arraylist of all adjacent bombs
	private boolean flagged;
	public Tile(int x, int y) { //constructor
		coords=new Coords(x,y);//save the coords to a coords object
		val=0;//default value of a tile
		Button=new JButton();//create a button for the time
		Button.setPreferredSize(new Dimension(40,40));//set that buttons size
		Button.setMaximumSize(Button.getPreferredSize());
		Button.setFont(new Font("Arial",Font.BOLD,7));//and text size
		flagged=false;//default of it being flagged is false
		
	}
	
	public boolean isBomb() {//returns if tile is a bomb
		if(val>-1) {//a value of minus 1 means the tile is a bomb
			return (false);
		}else {
			return (true);
		}
	}
	public boolean isClicked() {//returns if the tile is clicked or not
		return clicked;
	}
	public void onClick() {//set clicked to be true when the tile is clicked
		if(!clicked) {
			clicked=true;
		}
	}
	public int getVal() {//returns the value of the tile
		return val;
	}
	public void addBomb(Tile bomb) {//add adjacent bomb to adjacent bombs list
		bombs.add(bomb);
		setVal(false);
	}
	public void setVal(boolean isBomb) {//sets the value of the bombs
		if(isBomb && !clicked) {
			val=-1;
		}else {
			val=bombs.size();
		}
		
	}
	public Coords getCoords() {//returns the coordinates object
		return coords;
	}
	public JButton getButton() {//returns the stored button
		return Button;//i did this so i only had to have 1 2d array, instead of 1 for tiles and 1 for buttons
	}
	public void onRightClick() {//when the button is right clicked
		if(!flagged) {//and its not flagged
			flagged=true;//set it to be flagged
			clicked=true;//set it to be clicked, this is so the win condition works
		}else {// if it is flagged, reverse the previous changes
			flagged=false;
			clicked=false;
		}
	}
	public boolean getFlag() {//return if the tile has been flagged or not
		return flagged;
	}
	
}
