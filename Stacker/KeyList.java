import java.awt.event.*;
import java.awt.event.KeyListener;

class KeyList implements KeyListener
{
	boolean up, down, left, right, enter, space;
	
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case 27:	System.exit(0);			
			
			case KeyEvent.VK_LEFT:		if(!left)left = true;	break;
			case KeyEvent.VK_RIGHT:		if(!right)right= true;	break;
			case KeyEvent.VK_UP:		if(!up)up = true;		break;
			case KeyEvent.VK_DOWN:		if(!down)down = true;	break;
			case KeyEvent.VK_ENTER:		if(!enter)enter = true;	break;
			case KeyEvent.VK_SPACE:		if(!space)space = true;	break;
			
			
		}
	}
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_LEFT:		left = false;	break;
			case KeyEvent.VK_RIGHT:		right= false;	break;
			case KeyEvent.VK_UP:		up = false;		break;
			case KeyEvent.VK_DOWN:		down = false;	break;
			case KeyEvent.VK_ENTER:		enter = false;	break;
			case KeyEvent.VK_SPACE:		space = false;	break;
			
		}
	}
		
	public void keyTyped(KeyEvent e){}
}
