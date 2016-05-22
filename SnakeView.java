/**
 * @author Kamil Gabryjelski
 */
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
 
/**
 * SnakeView creates and modifies the graphics of game based on data from model.
 * Observing changes of model is handled by Observer interface.
 * Actions of user are transfered to control.
 */
class SnakeView implements Observer 
{
	/**
	 * handles KeyListener
	 */
	SnakeControl control = null;
	
	/**
	 * contains data on basis of which the view is created
	 */
	SnakeModel model = null;
	
	/**
	 * main frame of program
	 */
	private JFrame mainFrame;
	
	/**
	 * field that the snake moves on
	 */
	private Canvas canvas;
	
	/**
	 * contains current score
	 */
	private JLabel labelScore;
	
	/**
	 * contains manual of key controls
	 */
	private JLabel labelHelp;
	
	/**
	 * contains information from labelHelp
	 */
	private JPanel panelButton = new JPanel();
	
	/**
	 * width of the field the snake moves on
	 */
	private static int canvasWidth;
	
	/**
	 * height of the field the snake moves on
	 */
	private static int canvasHeight;
	
	/**
	 * width of the window containing canvas and information (score and manuals)
	 */
	private static int cpWidth;
	
	/**
	 * height of the window containing canvas and information (score and manuals)
	 */
	private static int cpHeight;
	
	/**
	 * width of a node
	 */
	private static final int nodeWidth = 10;
	
	/**
	 * height of a node
	 */
	private static final int nodeHeight = 10;
	
	/**
	 * Constructor of SnakeView
	 * Creates the main frame and places canvas and panels in it
	 * Sets location of main frame in the middle of the screen
	 * @param model model which should be used to create view
	 * @param control handles pressing the keys by user
	 */
	SnakeView(final SnakeModel model, final SnakeControl control) 
	{ 
		this.model = model;
		this.control = control;
		
		
		canvasWidth = (model.maxX)*nodeWidth;
		canvasHeight =(model.maxY)*nodeHeight;
		
		mainFrame = new JFrame("Snake");
		
		Container cp = mainFrame.getContentPane();
		
		cp.setSize(canvasWidth+10, canvasHeight+10);

		labelScore = new JLabel("Score:");
		cp.add(labelScore, BorderLayout.NORTH);
		
		canvas = new Canvas();
		canvas.setSize(canvasWidth, canvasHeight);
		//canvas.setSize(mainFrameWidth-20, mainFrameWidth-20);
		
		cp.add(canvas, BorderLayout.CENTER);
		
		
		panelButton.setLayout(new GridLayout(0,1));
		
		labelHelp = new JLabel("Press arrows or WSAD to move.", JLabel.CENTER);
		panelButton.add(labelHelp);
		
		labelHelp = new JLabel("Press \"+\" or \"-\" to accelerate or decelerate.", JLabel.CENTER);
		panelButton.add(labelHelp);
		
		labelHelp = new JLabel("Press ENTER or R to restart.", JLabel.CENTER);
		panelButton.add(labelHelp);
		
		labelHelp = new JLabel("Press SPACE or P to pause", JLabel.CENTER);
		panelButton.add(labelHelp);
		
		cp.add(panelButton, BorderLayout.SOUTH);
		
		cpWidth = cp.getSize().width;
		cpHeight = cp.getSize().height;
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		mainFrame.addKeyListener(control);
		mainFrame.pack();
		mainFrame.setResizable(true);
		mainFrame.setLocation((dim.width-cpWidth)/2, (dim.height-cpHeight)/2);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
	
	/**
	 * draws background, food and snake
	 * location of food and snake is based on data from model
	 * invokes updateScore() method
	 */
	void repaint() 
	{		
		Graphics g = canvas.getGraphics();
		 
		//draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		
		// draw the snake
		g.setColor(Color.WHITE);
		
		LinkedList<SnakeModel.Node> na = model.nodeArray;
		Iterator<SnakeModel.Node> it = na.iterator();
		
		while (it.hasNext()) 
		{ 
			SnakeModel.Node n = (SnakeModel.Node) it.next();
			drawNode(g, n);
		}
	 
		// draw the food
		g.setColor(Color.RED);
		SnakeModel.Node n = model.food;
		drawNode(g, n);
		
		updateScore();
	}
	
	/**
	 * draws a rectangular node in a given location specified by argument SnakeModel.Node
	 * @param g an object which handles drawing a rectangular
	 * @param n node which is going to be drew
	 */
	private void drawNode(Graphics g, SnakeModel.Node n) 
	{
		g.fillRect(n.x * nodeWidth,	n.y * nodeHeight, nodeWidth - 1, nodeHeight - 1); 
	}
	
	/**
	 * sets the text displayed by labelScore to current score
	 */
	private void updateScore() 
	{
		String s = "Score: " + model.score;
		labelScore.setText(s);
	}
	
	/**
	 * method from Observer interface invoked each time a model changes, which is handled by methods setChanged() and notifyObservers() from Observable class
	 * if game is over, displays a message
	 * invokes repaint()
	 */
	public void update(Observable o, Object arg)
	{
		if (model.failed)
			JOptionPane.showMessageDialog(null,	"You failed", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		repaint();
	}
}