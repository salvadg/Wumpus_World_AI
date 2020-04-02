
import java.util.*;

public class MyAI extends Agent
{

	class MyPair
	{
		private int left;
		private int right;
		public MyPair(int l, int r)
		{
			this.left= l;
			this.right = r;
		}
		public int getX() {return left;}
		public int getY() {return right;}
		public void setY(int val) {this.right = val;}
		public void setX (int val) {this.left = val;}
	}
	/////////////////////////// END OF HELPER CLASSES ///////////////////////////////////////////////////

	private static final String RIGHT = "Right";
	private static final String LEFT= "Left";
	private static final String UP = "Up";
	private static final String DOWN = "Down";


	private boolean haveArrow; 	// tells us if he any arrowsLEFT
	private boolean grabbedGold; // tells us if we have grabbed the gold.

	private boolean debug = false;	//boolean flag my print debugging

	private int maxCol;
	private int maxRow;

	private Action action = Action.CLIMB;

	//Cell [][] board;
	String [] [] board;

	private final String SAFE = "S";
	private final String VISITED = "V";
	private final String UNKNOWN = ".";
	private final String PIT = "P";
	private final String WUMPUS = "W";
	private boolean deadWumpus = false;
	private int currentX;
	private int currentY;
	private String agentDir;	// holds the current direction of the agent.

	private Stack<MyPair> previousTiles = new Stack<MyPair>();
	private int score = 0;
	private boolean [][] visited;
	boolean backTracking = false;

	int count = 0;
	



	public MyAI ( )
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================

		// we assume its max size board
		maxRow = 7;
		maxCol = 7;
		board = new String[maxCol][maxRow];
		visited = new boolean[maxCol][maxRow];

		//initialize game board
		for(int col = 0; col < maxCol; col++) {
			for (int row = 0; row < maxRow; row++) {
				board[col][row] = UNKNOWN;
				visited[col][row] = false;
			}
		}


		//agent info
		this.agentDir = RIGHT; // starts facing right
		this.currentX = 0;
		this.currentY = 0;

		this.haveArrow = true; // haveArrow is true if we have arrows available
		this.grabbedGold = false; // false if don't have gold bar

		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}
	
	public Action getAction(
		boolean stench,
		boolean breeze,
		boolean glitter,
		boolean bump,
		boolean scream
	)
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================

		score--;


		//if we get stuck
		if(score < -100 && (currentY == 0 && currentX == 0))
			return Action.CLIMB;
		if(score < -100)
		{
			if(isInBounds(this.currentX, this.currentY-1) && checkTile(this.currentX, this.currentY-1)) // else go down
			{
				if(!this.agentDir.equals(DOWN))
					return goDown();
				else
				{

					return moveForward();
				}
			}
			else if(isInBounds(this.currentX-1, this.currentY) && checkTile(this.currentX-1, this.currentY)) // else go left
			{
				if(!this.agentDir.equals(LEFT))
					return goLeft();
				else
				{

					return moveForward();
				}
			}
			else if(isInBounds(this.currentX+1, this.currentY) && checkTile(this.currentX+1, this.currentY)) // right
			{
				// its safe to move right, now check if we facing right else rotate
				if(!this.agentDir.equals(RIGHT))
					return goRight();
				else // agent facing right
				{
					return moveForward();
				}

			}
			else if(isInBounds(this.currentX, this.currentY+1) && checkTile(this.currentX, this.currentY+1)) // else go UP
			{
				if(!this.agentDir.equals(UP))
					return goUp();
				else
				{
					return moveForward();
				}
			}
		}


		MyPair newPair = new MyPair(this.currentX, this.currentY); // add previous tiles to stack
		if(!this.previousTiles.contains(newPair))
		{
			this.previousTiles.add(newPair);
		}

		if(debug)
		{
			if(!this.previousTiles.empty())
			{

				System.out.println("(" + this.previousTiles.peek().getX() + ", " + this.previousTiles.peek().getY() + ")");

			}
		}

		if(this.debug)
		{
			System.out.println("DIRECTION: " +this.agentDir);
			System.out.println("CURRENT X: "+ this.currentX);
			System.out.println("CURRENT Y: "+this.currentY);
		}

		if(!grabbedGold && glitter) // if we found gold and we haven't grabbed it
		{
			grabbedGold = true;
			return Action.GRAB;
		}

		if(grabbedGold) // if we found the gold and are back home exit
		{
			if(this.currentY == 0 && this.currentX == 0) // and we are home. CLimb out.
				return Action.CLIMB;

			else if(isInBounds(this.currentX, this.currentY-1) && checkTile(this.currentX, this.currentY-1)) // else go down
			{
				if(!this.agentDir.equals(DOWN))
					return goDown();
				else
				{

					return moveForward();
				}
			}
			else if(isInBounds(this.currentX-1, this.currentY) && checkTile(this.currentX-1, this.currentY)) // else go left
			{
				if(!this.agentDir.equals(LEFT))
					return goLeft();
				else
				{

					return moveForward();
				}
			}
			else if(isInBounds(this.currentX+1, this.currentY) && checkTile(this.currentX+1, this.currentY)) // right
			{
				// its safe to move right, now check if we facing right else rotate
				if(!this.agentDir.equals(RIGHT))
					return goRight();
				else // agent facing right
				{
					return moveForward();
				}

			}
			else if(isInBounds(this.currentX, this.currentY+1) && checkTile(this.currentX, this.currentY+1)  && !this.visited[this.currentX][this.currentY+1]) // else go UP
			{
				if(!this.agentDir.equals(UP))
					return goUp();
				else
				{
					return moveForward();
				}
			}

		} // end grabbedGold

		if(stench && !scream && haveArrow && !breeze) // percept a stench the shoot arrow and mark tile ahead as safe
		{
			haveArrow = false;
//			updateNeighbors(this.currentX,this.currentY,WUMPUS);
			if(this.agentDir.equals(RIGHT) && !isInBounds(this.currentX+1,this.currentY))
			{
				return goUp();
			}
			else if(this.agentDir.equals(LEFT) && !isInBounds(this.currentX-1,this.currentY))
			{
				return goUp();
			}
			if(this.agentDir.equals(RIGHT) && isInBounds(this.currentX+1, this.currentY))
				this.board[this.currentX+1][this.currentY] = SAFE;
			else if(this.agentDir.equals(UP) && isInBounds(this.currentX, this.currentY+1))
				this.board[this.currentX][this.currentY+1] = SAFE;
			else if(this.agentDir.equals(DOWN) && isInBounds(this.currentX, this.currentY-1))
				this.board[this.currentX][this.currentY-1] = SAFE;
			else if(this.agentDir.equals(LEFT) && isInBounds(this.currentX-1, this.currentY))
				this.board[this.currentX-1][this.currentY] = SAFE;

			return Action.SHOOT;
		}

		if(scream)
		{
			stench = false;
			deadWumpus = true;
			for(int i = 0; i < maxCol; ++i) {
				for (int j = 0; j < maxRow; ++j) {
					if(this.board[i][j].equals(WUMPUS) && !this.board[i][j].equals(PIT))
						this.board[i][j] = SAFE;
				}
			}
		}


		if((this.currentX == 0 && this.currentY == 0) && (stench || breeze) ) //if its unsafe to move from start
		{
			if(debug)
				printBoard();
			if(!scream && haveArrow)
				return Action.CLIMB;
		}

		if(bump) // we bump a wall
		{
			previousTiles.pop();
			if(this.agentDir.equals(RIGHT)) // it was the east wall
			{
				this.maxCol = currentX;
				this.currentX--; // go back one cell
			}
			if(this.agentDir.equals(UP)) // hit the north wall
			{
				this.maxRow = currentY;
				this.currentY--;
			}

			if(debug)
			{
				System.out.println("maxRow = "+ maxRow);
				System.out.println("maxCOl = "+ maxCol);
			}
		}

		// if unvisited then update board
		if(this.board[this.currentX][this.currentY].equals(UNKNOWN) || this.board[this.currentX][this.currentY].equals(SAFE))
		{
			this.board[this.currentX][this.currentY] = VISITED; // mark as visited
			this.visited[this.currentX][this.currentY] = true;

			if(breeze)
			{
				updateNeighbors(this.currentX, this.currentY, PIT);
			}
			if(stench &&  !deadWumpus)
			{
				updateNeighbors(this.currentX, this.currentY, WUMPUS);
			}

			if(!breeze && !stench)
			{
				updateNeighbors(this.currentX, this.currentY, SAFE);
			}

		}


		if(debug)
		{
			printBoard();
			System.out.println("Agent Facing: " + this.agentDir);
		}

		//move right if possible
		if(isInBounds(this.currentX+1, this.currentY) && checkTile(this.currentX+1, this.currentY) && !this.visited[this.currentX+1][this.currentY])
		{

			// its safe to move right, now check if we facing right else rotate
			if(!this.agentDir.equals(RIGHT))
				return goRight();
			else // agent facing right
			{
				return moveForward();
			}

		}
		else if(isInBounds(this.currentX, this.currentY+1) && checkTile(this.currentX, this.currentY+1) && !this.visited[this.currentX][this.currentY+1]) // else go UP
		{
			if(!this.agentDir.equals(UP))
				return goUp();
			else
			{
				return moveForward();
			}
		}
		else if(isInBounds(this.currentX-1, this.currentY) && checkTile(this.currentX-1, this.currentY)) // else go left
		{

			if(!this.agentDir.equals(LEFT))
				return goLeft();
			else
			{

				return moveForward();
			}
		}
		else if(isInBounds(this.currentX, this.currentY-1) && checkTile(this.currentX, this.currentY-1)) // else go down
		{
			if(!this.agentDir.equals(DOWN))
				return goDown();
			else
			{

				return moveForward();
			}
		}

		return action; // defaults to Climb
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}

	// ======================================================================
	// YOUR CODE BEGINS
	// ========================m==============================================




	private Action goBack()
	{
		MyPair pair = this.previousTiles.peek();
		Action newAction = null;

		if(pair.getX() == this.currentX && pair.getY() == this.currentY)
			previousTiles.pop();
		// if previous tile is to the left
		if((this.currentX-1) == pair.getX()-1)
		{
//			s = LEFT;
			newAction = goLeft();
		}
		if((this.currentX+1) == pair.getX()-1)
		{
//			s = RIGHT;
			newAction = goRight();
		}
		if((this.currentY-1) == pair.getY()-1)
		{
//			s = DOWN;
			newAction = goDown();
		}
		if((this.currentY+1) == pair.getY()-1)
		{

			newAction = goUp();
		}

		return newAction;

	}

	private boolean checkTile(int x, int y)
	{
		if(!this.board[x][y].equals(PIT) && !this.board[x][y].equals(WUMPUS)) // has neither pit or wumpus
		{
			return true;
		}
		else if(this.board[x][y].equals(WUMPUS) && deadWumpus) // else if has dead wumpus then its safe
		{
			return true;
		}
		return false;
//		return !this.board[x][y].equals(PIT);
	}

	private Action goLeft()
	{
		Action act = null;
		if(this.agentDir.equals(UP))
		{
			this.agentDir = LEFT;
			act = Action.TURN_LEFT;
		}
		if(this.agentDir.equals(DOWN))
		{
			this.agentDir = LEFT;
			act = Action.TURN_RIGHT;
		}
		if(this.agentDir.equals(RIGHT))
		{
			this.agentDir = UP;
			act = Action.TURN_LEFT;
		}

		if(debug)
		{
			System.out.println("goLeft Dir: "+agentDir);
		}

		return act;

	}

	private Action goRight()
	{

		Action act = null;
		if(this.agentDir.equals(UP))
		{
			this.agentDir = RIGHT;
			act = Action.TURN_RIGHT;
		}
		if(this.agentDir.equals(DOWN))
		{
			this.agentDir = RIGHT;
			act = Action.TURN_LEFT;
		}
		if(this.agentDir.equals(LEFT))
		{
			this.agentDir = UP;
			act = Action.TURN_RIGHT;
		}

		if(debug)
		{
			System.out.println("goRight Dir: "+agentDir);
		}

		return act;


	}

	private Action goUp()
	{
		Action act = null;
		if(this.agentDir.equals(DOWN))
		{
			this.agentDir = LEFT;
			act = Action.TURN_RIGHT;
		}
		if(this.agentDir.equals(RIGHT))
		{
			this.agentDir = UP;
			act = Action.TURN_LEFT;
		}
		if(this.agentDir.equals(LEFT))
		{
			this.agentDir = UP;
			act = Action.TURN_RIGHT;
		}

		if(debug)
		{
			System.out.println("goUp Dir: "+agentDir);
		}

		return act;
	}

	private Action goDown()
	{
		Action act = null;
		if(debug)
		{
			System.out.println("goDownBefore Dir: "+this.agentDir);
		}
		if(agentDir.equals(UP))
		{
			this.agentDir = RIGHT;
			act = Action.TURN_RIGHT;
		}
		else if(this.agentDir.equals(RIGHT))
		{
			this.agentDir = DOWN;
			act = Action.TURN_RIGHT;
		}
		else if(this.agentDir.equals(LEFT))
		{
			this.agentDir = DOWN;
			act = Action.TURN_LEFT;
		}

		if(debug)
		{
			System.out.println("goDown Dir: "+this.agentDir);
		}

		return act;
	}

	private boolean isInBounds(int x, int y)
	{
		if((x < maxCol && x >= 0) && (y < maxRow && y >= 0))
			return true;
		return false;
	}


	private boolean checkBounds(int x, int y)
	{
		if(this.agentDir.equals(UP))
		{
			if(isInBounds(x,y+1))
			{
				return true;
			}
		}
		if(this.agentDir.equals(DOWN))
		{
			if(isInBounds(x,y-1))
			{
				return true;
			}
		}
		if(this.agentDir.equals(LEFT))
		{
			if(isInBounds(x-1,y))
			{
				return true;
			}
		}
		if(this.agentDir.equals(RIGHT))
		{
			if(isInBounds(x+1,y))
			{
				return true;
			}
		}

		return false;
	}

	private boolean isValid(int x,int y)
	{
		if(agentDir.equals(UP))
		{
			if(isInBounds(x,y+1) && !(board[x][y+1].equals(PIT)))
			{
				return true;
			}
		}
		if(agentDir.equals(DOWN))
		{
			if(isInBounds(x,y-1) && !(board[x][y-1].equals(PIT)))
			{
				return true;
			}
		}
		if(agentDir.equals(LEFT))
		{
			if(isInBounds(x-1,y) && !(board[x-1][y].equals(PIT)))
			{
				return true;
			}
		}
		if(agentDir.equals(RIGHT))
		{
			if(isInBounds(x+1,y) && !(board[x+1][y].equals(PIT)))
			{
				return true;
			}
		}

		return false;
	}

	private String turnDirection(Action action, String currentDir)
	{
		String dir = "";
		if(currentDir.equals(RIGHT))
		{
			if(action.equals(Action.TURN_LEFT))
				dir = UP;
			if(action.equals(Action.TURN_RIGHT))
				dir = DOWN;
		}
		if(currentDir.equals(UP))
		{
			if(action.equals(Action.TURN_LEFT))
				dir = LEFT;
			if(action.equals(Action.TURN_RIGHT))
				dir= RIGHT;
		}
		if(currentDir.equals(DOWN))
		{
			if(action.equals(Action.TURN_LEFT))
				dir= RIGHT;
			if(action.equals(Action.TURN_RIGHT))
				dir= LEFT;
		}
		if(currentDir.equals(LEFT))
		{
			if(action.equals(Action.TURN_LEFT))
				dir = DOWN;
			if(action.equals(Action.TURN_RIGHT))
				dir = UP;
		}
		if(debug)
			System.out.println("direction:: "+dir);
		return dir;

	}


	private Action generateRandomMove()
	{
		final Action[] actions =
		{
				Action.TURN_LEFT,
				Action.TURN_RIGHT,
				Action.FORWARD
		};

		Random rand = new Random();
		int i = rand.nextInt ( actions.length );
		Action action = actions[i];

		if(isValid(currentX,currentY) && action.equals(Action.FORWARD))
		{
			if(debug)
				System.out.println("generateForward");
			return moveForward();
		}
		else if(!checkBounds(currentX,currentY))
		{
			i = rand.nextInt (1);
			action = actions[i];
			agentDir = turnDirection(action, agentDir);
		}

		else {
			agentDir = turnDirection(actions[i], agentDir);
			action = actions[i];
		}

		if(debug)
		{
			System.out.println("DIRGen : " + agentDir);
		}

		return action;
	}

	// update the status of neighboring tiles
	private void updateNeighbors(int x, int y, String s)
	{


		if(s.equals(SAFE))
		{
			if(isInBounds(x, y+1) && !this.board[x][y+1].equals(VISITED))
				this.board[x][y+1] = s;

			// south neighbor
			if(isInBounds(x, y-1) && !this.board[x][y-1].equals(VISITED))
				this.board[x][y-1] = s;

			// west neighbor
			if(isInBounds(x-1, y) && !this.board[x-1][y].equals(VISITED))
				this.board[x-1][y] = s;

			// east neighbor
			if(isInBounds(x+1, y) && !this.board[x+1][y].equals(VISITED) )
				this.board[x+1][y] = s;
		}
		 //north neighbor
		if(isInBounds(x, y+1) && (this.board[x][y+1].equals(UNKNOWN) || this.board[x][y+1].equals(WUMPUS)) )
			this.board[x][y+1] = s;

		// south neighbor
		if(isInBounds(x, y-1) && (this.board[x][y-1].equals(UNKNOWN) || board[x][y-1].equals(WUMPUS)))
			this.board[x][y-1] = s;

		// west neighbor
		if(isInBounds(x-1, y) && (this.board[x-1][y].equals(UNKNOWN) || this.board[x-1][y].equals(WUMPUS)))
			this.board[x-1][y] = s;

		// east neighbor
		if(isInBounds(x+1, y) && (this.board[x+1][y].equals(UNKNOWN) || this.board[x+1][y].equals(WUMPUS)))
			this.board[x+1][y] = s;

	}



	//handle moving forward.
	Action moveForward()
	{
		if(this.agentDir.equals(UP))
		{
			this.currentY++;
		}
		if(this.agentDir.equals(DOWN))
		{
			this.currentY--;
		}
		if(this.agentDir.equals(LEFT))
		{
			this.currentX--;
		}
		if(this.agentDir.equals(RIGHT))
		{
			this.currentX++;
		}

		if(debug)
			printBoard();
		if(debug)
		{
			System.out.println("X After move Forward: "+currentX);
			System.out.println("Y After Move Forward: "+currentY);
		}

		return Action.FORWARD;
	}


	void printBoard()
	{
		for(int i  = maxRow-1; i >= 0; i--)
		{
			for (int j = 0; j < maxCol; j++)
			{
				if(this.currentX == j && this.currentY == i)
					System.out.print("@"+"   ");
				else
					System.out.print(board[j][i] + "   ");
			}
			System.out.println();
		}

	}
	// ======================================================================
	// YOUR CODE ENDS
	// ======================================================================
}
