/**
 * @author Kamil Gabryjelski
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

/**
 * A class which contains data of model and methods modifying it.
 * Handles the movement of snake, the placement of snake, counting score, collisions and creating food.
 * Includes initial values and fields responsible for additional functionalities such as pause.
 * Running the thread is handled by the Runnable interface
 * Notifying the view about changes is handled by Observable class
 */
class SnakeModel extends Observable implements Runnable
{
	/**
	 * Inner class which handles the location of snake's nodes. Includes x and y coordinates
	 */
	class Node
	{
		int x;
		int y;
			
		Node(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * Max x value
	 */
	int maxX;
	
	/**
	 * Max y value
	 */
	int maxY;
	
	/**
	 * Matrix which checks if a field is empty (false) or taken by a snake node or food node (true)
	 */
	private boolean[][] matrix;
	
	/**
	 * List of snake nodes
	 */
	LinkedList<Node> nodeArray = new LinkedList<Node>(); 
	
	/**
	 * location of food
	 */
	Node food;
	
	/**
	 * true if thread is running
	 */
	boolean running = false;
	
	/**
	 * true if game is over
	 */
	boolean failed = false;
	
	/**
	 * true if a key has been pressed in current loop, changes value to false at the beginning of next loop
	 */
	private boolean clicked = false;
	
	/**
	 * time of thread's sleep before updating, equivalent of 1/speed
	 */
	int time = 200;
	
	/**
	 * rate of movement speed changing, equivalent of 1/acceleration
	 */
	double speedChangeRate = 0.75;
	
	/**
	 * true if game is paused
	 */
	boolean paused = false; 
	
	/**
	 * current score, calculated on the basis of current speed, number of moves made to get to the food and size of playing field.
	 * Minimum score added after eating food is 10. 
	 */
	int score = 0; 
	
	/**
	 * number of moves made before eating next food.
	 */
	int countMove = 0;
	
	/**
	 * direction of snake's movement.
	 * 1 - left
	 * 2 - up
	 * 3 - right
	 * 4 - down
	 */
	private int direction = 2; 
	
	/**
	 * Equals 2, snake moves up
	 */
	static final int UP = 2;
	
	/**
	 * Equals 4, snake moves down
	 */
	static final int DOWN = 4;
	
	/**
	 * Equals 1, snake moves left
	 */
	static final int LEFT = 1;
	
	/**
	 * Equals 3, snake moves right
	 */
	static final int RIGHT = 3;
	
	/**
	 * Constructor of SnakeModel. Invokes method reset(). Sets maxX and maxY.
	 * @param maxX the value maxX will be set to
	 * @param maxY the value maxY will be set to
	 */
	SnakeModel(int maxX, int maxY) 
	{
		this.maxX = maxX;
		this.maxY = maxY;
		
		reset();
	}
	
	/**
	 * changes model's parameters to initial values:
	 * direction - up
	 * time - 200
	 * score, countMove - 0
	 * failed, paused, clicked - false
	 * Creates new matrix and new nodeArray
	 */
	void reset()
	{
		direction = SnakeModel.UP; 
		time = 200; 
		score = 0; 
		countMove = 0;
		failed = false;
		paused = false;
		clicked = false;
		

		matrix = new boolean[maxX][];
		for (int i = 0; i < maxX; ++i) 
		{
			matrix[i] = new boolean[maxY];
			Arrays.fill(matrix[i], false);
		}
		
		// initialize the snake
		
		int initArrayLength = maxX > 20 ? 10 : maxX / 2; //initial size depends on size of the field
		nodeArray.clear();
		for (int i = 0; i < initArrayLength; ++i) 
		{
		
			int x = maxX / 2 + i;
			int y = maxY / 2;
			
			nodeArray.addLast(new Node(x, y));
			matrix[x][y] = true;
		}

		food = createFood();
		matrix[food.x][food.y] = true;
	}
	
	/**
	 * Changes direction of movement
 	 * sets field clicked to true.
	 * Doesn't change direction if direction % 2 == newDirection % 2
	 * @param newDirection the value of new direction
	 */
	void changeDirection(int newDirection) //executed in controller
	{
		if ((direction % 2 != newDirection % 2) && !clicked) //can change direction only by 90 degrees
		{
			direction = newDirection;
			clicked = true;
		}
	}
	
/**
 * Handles snake's movement.
 * Creates a new node at the beginning of nodeArray (a "head") and deletes the last one (a "tail")
 * Location of the new node depends on direction of movement
 * If food was eaten, creates an additional new node at the beginning of nodeArray
 * Increases moveCount by one
 * @return true if move is possible, false if snake eats itself
 */
	private boolean moveOn() 
	{
		Node n = (Node) nodeArray.getFirst();
		int x = n.x; //location of head of the snake
		int y = n.y;
		
		switch (direction)
		{
			case UP:
				y--;
				break;
				
			case DOWN:
				y++;
				break;
				
			case LEFT:
				x--;
				break;
				
			case RIGHT:
				x++;
				break;
		}
		
		if (x<0) //moving through the walls
			x = maxX-1;
		else if (x>=maxX)
			x = 0;
		else if (y<0)
			y = maxY-1;
		else if (y>=maxY)
			y = 0;

		if (matrix[x][y]) //snake gets to taken field
		{
			if (x == food.x && y == food.y) //that field was food
			{
				nodeArray.addFirst(food);
	
				int scoreGet = 200 * (maxX - countMove) / time;
				score += scoreGet > 10 ? scoreGet : 10; //if too long, score += 10
				countMove = 0;
				food = createFood(); 
				matrix[food.x][food.y] = true; 
				return true;
			} 
				
			else //that field wasn't food - snake eats itself
				return false;
		}
			
		else //snake doesn't hit anything, moves on empty fields
		{
			nodeArray.addFirst(new Node(x, y));
			matrix[x][y] = true;
			n = (Node) nodeArray.removeLast();
			matrix[n.x][n.y] = false;
			countMove++;
			return true;
		}
	}	
	
/**
 * Creates food in random empty place (matrix[x][y] == false)
 * @return a node representing a food
 */
	private Node createFood() //creates 
	{
		int x = 0;
		int y = 0;
		
		while(true)
		{
			Random r = new Random(); 
			x = r.nextInt(maxX-1);
			y = r.nextInt(maxY-1);
			
			if(!matrix[x][y])
				return new Node(x,y);
		}
	}
	
	/**
	 * accelerates by speedChangeRate
	 */
	void speedUp() //executed in controller
	{
		if (time > 30)
		time *= speedChangeRate;
	}
	
	/**
	 * decelerates by speedChangeRate
	 */
	void speedDown() //executed in controller
	{
		if (time < 300)
		time /= speedChangeRate;
	}
	
	/**
	 * pauses the game
	 */
	void changePauseState() //executed in controller
	{
		paused = !paused;
	}
	
	
	/**
	 * handles the thread.
	 * While field running == true and game isn't paused, invokes moveOn() in a loop and notifies the observer (object of SnakeView class) about changes.
	 * Thread sleeps at the beginning of each loop for time milliseconds
	 */
	public void run() 
	{
		running = true;
		
		while (running) 
		{
			try 
			{
				Thread.sleep(time); //handles the movement sleep by putting the thread to sleep for given time. The shorter the time, the faster snake moves
				clicked = false;
			}
			
			catch (Exception e)
			{
				break;
			}
			
			if (!paused) 
			{
				if (moveOn()) //moveOn() returns false if snake eats itself
				{
					setChanged();
					notifyObservers();
				} 
				
				else
				{
					failed = true;
					setChanged();
					notifyObservers();
		
					this.reset();
				}
			}	
		}
	}
}