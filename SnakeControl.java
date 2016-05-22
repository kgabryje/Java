/**
 * @author Kamil Gabryjelski
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * SnakeControl handles the actions of user performed in the view.
 * When a key is pressed, SnakeControl decides which method to invoke and modifies a model
 * Key control is handled by KeyListener interface
 */
class SnakeControl implements KeyListener
{  
	/**
	 * model is modified through methods invoked when a key is pressed
	 */
	SnakeModel model;
	
	/**
	 * sets model field as model argument
	 * @param model model which should be modified by controller  
	 */
	SnakeControl(SnakeModel model)
	{  
		this.model = model;
	}
	
	/**
	 * handles the key events
	 * if the game is running and is not paused, user can invoke changeDirection() by pressing arrows or WSAD
	 * and speedUp() and speedDown() by pressing + or -
	 * When the game is running, user can invoke restart() by pressing enter or r and changePauseState() by pressing space or p
	 */
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();
		if (model.running && !model.paused)
		{
			switch (keyCode)
			{
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					model.changeDirection(SnakeModel.UP);
					break;
					
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					model.changeDirection(SnakeModel.DOWN);
					break;
					
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					model.changeDirection(SnakeModel.LEFT);
					break;
					
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					model.changeDirection(SnakeModel.RIGHT);
					break;
					
				case KeyEvent.VK_ADD:
					model.speedUp();
					break;
					
				case KeyEvent.VK_SUBTRACT:
					model.speedDown();
					break;
					
				
					
				default:
			}
		}
		
		if (model.running)
		{
			switch (keyCode)
			{
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_P:
					model.changePauseState();
					break;
					
				case KeyEvent.VK_R:
				case KeyEvent.VK_ENTER:
					model.reset();
					break;
			}
		}
	}
	 
	public void keyReleased(KeyEvent e) 
	{  
	 
	}
	 
	public void keyTyped(KeyEvent e)
	{
	  
	}	 
}