package myMineSweeper;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
public class Minesweeper {
	public static int width=30;//how many columns there are
	public static int height=16;//how many rows there are
	public static Tile[][] Board=new Tile[height][width];
	public static int[] DRow= {-1,0,1,0,-1,-1,1,1};//used for checking surrounding tiles
	public static int[] DCol= {0,1,0,-1,1,-1,1,-1};//same as above
	public static int bombNum=99;//number of bombs on the field
	public static JFrame frame=new JFrame("Minesweeper");
	public static int numFlags=0;//number of placed flags
	public static JLabel Flags=new JLabel("Flags 0/99");//label to tell user how many flags theyve places
	
	public static void main(String[] args) {
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1500,900);
		drawBoard();//add all elements to the board
		frame.setVisible(true);
		//printBoard(); //prints the board inconsole used for testing

	}
	//reset the entire board
	public static void resetBoard() {
		numFlags=0; //set number of marked bombs to 0
		frame.getContentPane().removeAll();//removes all children of the jframe
		frame.repaint();
		drawBoard();//redraw the board
		frame.validate();//validate to force it to update
	
	}
	
	//this function generates the 2d tile array that is used for game logic
	public static Tile[][] generateBoard(){
		Tile[][] newBoard=new Tile[height][width];
		Random random=new Random();//random number generator constructor

		for (int i=0;i<height;i++) {
			for (int j=0;j<width;j++) {
				//loops through the entire board generating a new Tile object for each of them
				newBoard[i][j]=new Tile(i,j);
			}

		}
		
		for(int i=0;i<bombNum;i++) { //for each bomb
			int row=random.nextInt(height-0)+0;//generate a random row coordinate
			int col=random.nextInt(width-0)+0; //generate a random coloumn coordiante
			if(!newBoard[row][col].isBomb()) {//if those coordinates dont already have a bomb in it
				newBoard[row][col].setVal(true); //set the tile to be a bomb tile
				for (int j=0;j<DRow.length;j++) {//for each surrounding tile
					int newRow=row+DRow[j];
					int newCol=col+DCol[j];
					if(newRow>=0 && newRow<height &&newCol>=0 && newCol<width) {//make sure the surrounding area is not out of bounds
						if(!newBoard[newRow][newCol].isBomb()) {//if it is not a bomb
							newBoard[newRow][newCol].addBomb(newBoard[row][col]);//add 1 to the bomb array in Tiles which is used to calculate its value
						}
					}
				}
				
			}else {//if there is already a bomb there, repeat an iteration
				i--;
			}
			
		}
		return (newBoard);//return the 2d array of tiles
	}
	
	public static void setFlags() {
		//sets the text of the jlabel to have how many flags are on the field
		Flags.setText("Flags: "+numFlags+"/99");
	}
	
	//this function prints out the board into the console for testing reasons
	public static void printBoard() {
		for (int i=0;i<height;i++) {
			String line="";
			for (int j=0;j<width;j++) {
				if(!Board[i][j].isBomb()){
					line+=" "+Board[i][j].getVal()+" ";
				}else {
					line+=Board[i][j].getVal()+" ";
				}
			}
			System.out.println(line);

		}
	}
	
	//this function adds all the components of the Board to the Jframe
	public static void drawBoard() {
		Board=generateBoard();//generate a new board
		JPanel Top=new JPanel();//declare an area as the top of the board that uses a borderlayout

		Top.setLayout(new BorderLayout());
		JPanel flowPanel=new JPanel(new FlowLayout());//create a sub panel using a flow layout so the reset button doesnt stretch
		setFlags();//update flags
		Top.add(Flags,BorderLayout.WEST);//add the flags to the left side of the top segment of the board

		JButton reset=new JButton("Reset");//create reset button
		
		reset.addMouseListener(new MouseAdapter() {//when reset is clicked
			@Override
			public void mousePressed(MouseEvent e) {
				//run the reset method
				resetBoard();
			}
		});
		flowPanel.add(reset); //add the button to the flow panel
		Top.add(flowPanel,BorderLayout.CENTER); //add the flow panel to the center of the top panel
		frame.add(Top,BorderLayout.NORTH);// add the top panel to the top of the overall frame
		
		JPanel center=new JPanel();//create a panel to be the center of the screen
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));//set this to use a box layout
		for(int i=0;i<height;i++) {//for each row
			JPanel flow=new JPanel();//create a flow panel
			flow.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
			for (int j=0;j<width;j++) {//for each item in said row
				final int r=i;//had to do this so i could pass them into the click listener for some reason
				final int c=j;
				Board[r][c].getButton().addMouseListener(new MouseAdapter() {//add a onclick listener to the button generated in tile
					@Override
					public void mousePressed(MouseEvent e) {
						if(!Board[r][c].isClicked()) {//if that button hasnt been pressed before
						if(e.getButton()==MouseEvent.BUTTON1) {//and it is a left click
						if(Board[r][c].isBomb()) {//if that spot is a bomb
							int lose=JOptionPane.showConfirmDialog(null,"YOU LOSE\nOK to reset, Cancel to Close","Alert",JOptionPane.WARNING_MESSAGE);//display that the user has lost
							if (lose==JOptionPane.OK_OPTION) {//on ok
								resetBoard();//reset the game
							}else if(lose==JOptionPane.CANCEL_OPTION) { //on cancel 
								System.exit(0);//exit the app
							}
						}else if(!Board[r][c].isBomb()) {//if it not a bomb
							if(numFlags==bombNum &&CheckWin()) {//if all bombs are flagged correctly and all tiles are open
								int win=JOptionPane.showConfirmDialog(null,"YOU WIN\nOK to reset, Cancel to Close","Alert",JOptionPane.WARNING_MESSAGE);//display that the user has won
								if (win==JOptionPane.OK_OPTION) {//on ok reset
									resetBoard();
								}else if(win==JOptionPane.CANCEL_OPTION) {//on cancel close the game
									System.exit(0);
								}
							}else {//if they havent flagged all the bombs correctly or not all tiles are open
							Board[r][c].onClick();//set that position to be clicked
							Board[r][c].getButton().setText(Board[r][c].getVal()+""); //change the text on the button to be the value
							for(int i=0;i<DRow.length;i++) {//check surrounding blocks
								if(r+DRow[i]>=0 &&r+DRow[i]<height &&c+DCol[i]>=0 &&c+DCol[i]<width) {//ensure they are within bounds
								if(Board[r+DRow[i]][c+DCol[i]].getVal()==0) {// if the current tile is touching a tile that isnt touching any bombs
									openBoard(r,c,Board[r][c]);//run a breadth first search, opening all connected non bomb tiles that are next to tile that arent touching any bombs
									break;
								}
								}
							}
						}
						}
						}else if(e.getButton()==MouseEvent.BUTTON3) {//if right click
							
							if(!Board[r][c].getFlag()) {//if the tile is not already flagged
							Board[r][c].onRightClick();//set the tile to be flagged
							Board[r][c].getButton().setText("F");//set text to be F
							numFlags++;//increase number of flags on the board
							setFlags();//update the label
							if(numFlags==bombNum &&CheckWin()) {//this is the same as the win above
								int win=JOptionPane.showConfirmDialog(null,"YOU WIN\nOK to reset, Cancel to Close","Alert",JOptionPane.WARNING_MESSAGE);
								if (win==JOptionPane.OK_OPTION) {
									resetBoard();
								}else if(win==JOptionPane.CANCEL_OPTION) {
									System.exit(0);
								}
								}
							}else {//if the tile is already flagged
								Board[r][c].onRightClick();//remove flag
								Board[r][c].getButton().setText("");
								numFlags--;
								setFlags();
							}
						}
						
					}
					}
				});
				flow.add(Board[i][j].getButton());//add the button to the flow layout
			}
			center.add(flow);//add the flow layout to the box layout
		}
		frame.add(center,BorderLayout.CENTER);//add the box layout to the frame in the center of it
		
	}
	//this loops through all tiles, if they are all clicked and all flags are correctly placed return true else return false
	public static boolean CheckWin() {
		boolean won=true;
		outerloop:
		for (int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				if(!Board[i][j].isClicked() ||(Board[i][j].getFlag()&& !Board[i][j].isBomb())) {
					won=false;
					break outerloop;
			}
		}
	}
		return won;
	}
	//breadth first search that opens adjacent tiles
	public static void openBoard(int R,int C,Tile Root) {
		Boolean[][] visited=new Boolean[height][width];//create a visited array that mimics the board
		for (int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				visited[i][j]=false;//set all values in it to be false
			}
		}
		//create a queue of Tiles
		LinkedList<Tile> queue=new LinkedList<Tile>();
		queue.add(Root);//add the root to the tiles
		while(queue.size()!=0) {
			Tile curr=queue.poll();//pull the top of the queue
			R=curr.getCoords().Row;//get its coordinates
			C=curr.getCoords().Col;
			if(!curr.isBomb() &&!curr.getFlag()) {//if it is not a bomb and not flagged
				curr.onClick();//set it to be clicked
				curr.getButton().setText(curr.getVal()+""); //reveal its value
				visited[R][C]=true;//set that block to be true in the boolean array
				for(int i=0;i<DRow.length;i++) {//loop through surrounding Tiles to see if it is touching a tile with the value of 0
					if(R+DRow[i]>=0 &&R+DRow[i]<height &&C+DCol[i]>=0 &&C+DCol[i]<width) {//ensure it is in range
					Tile temp=Board[R+DRow[i]][C+DCol[i]];//get the current surrounding tile to make calling it cleaner
					boolean open=false;
					int tR=R+DRow[i];//do the same with coordinates
					int tC=C+DCol[i];
					for(int j=0;j<DRow.length;j++) {//loop through the surrounding Tiles of the current temp Tile
						if(tR+DRow[j]>=0 &&tR+DRow[j]<height &&tC+DCol[j]>=0 &&tC+DCol[j]<width &&Board[tR+DRow[j]][tC+DCol[j]].getVal()==0) { //if it in range and has a value of 0
							open=true;//allow the current tile to be opened up
							break;//stop the loop
						}
					}
					if(!temp.isClicked() &&!temp.isBomb() &&!temp.getFlag() &&!visited[R+DRow[i]][C+DCol[i]] &&(open||temp.getVal()==0)) {//if the tile hasnt been clicked, and its not a bomb, and it hasnt been flagged and it hasnt been visited before
						//and it is touching a tile with the value 0 or itself has the value of 0
						queue.add(temp);// then add it to the queue
					}
				}
				}
			}
		}
		
	}

}
