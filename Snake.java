/**
 * @author Kamil Gabryjelski
 */

/**
 * Main class of the program
 * Includes main(String[] args) method, which creates model, view and control objects, sets the view as observer of model and starts a new thread
 */
public class Snake
{
	public static void main(String[] args) 
	{
		SnakeModel model = new SnakeModel(30,30);
		SnakeControl control = new SnakeControl(model);
		SnakeView view = new SnakeView(model,control);
		
		model.addObserver(view);
		
		(new Thread(model)).start();
	}
}