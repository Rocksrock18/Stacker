import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class StackerGame extends JPanel{
	GameView frame;
	int layer;
	Terminator end;
	ArrayList<JLabel> rect;
	JLabel temp;
	String fadedImage;
	String image;

    public StackerGame(GameView frame1, Terminator end1) {
    	fadedImage = "images/whiteRectangleFaded.png";
    	image = "images/whiteRectangle.png";
    	frame = frame1;
    	layer = 0;
    	end = end1;
    	rect = new ArrayList<JLabel>();
        
    }
    /**
     * Plays a full game of stacker.
     */
    public void newGame()
    {
    	//first indicates whether or not it is the first time the computer senses 
    	//enter being pressed after it sensed that it was not pressed.
    	boolean first = true;
    	boolean goingRight = true;
    	int xpos = 925;
    	int leftBound = 440;
    	int maxWidth = 75;
    	temp = new JLabel(new ImageIcon(this.getClass().getResource(fadedImage)));
    	
    	JLabel base = new JLabel(new ImageIcon(this.getClass().getResource(image)));
    	base.setOpaque(true);
		base.setBounds(440, 600, 75, 40);
		base.setBackground(Color.BLACK);
		add(base);
		rect.add(base);
		layer = 0;
    	
    	add(temp);
		rect.add(temp);
		temp.setOpaque(true);
		temp.setBackground(Color.BLACK);
		
    	//make key listener
    	KeyList kl = new KeyList();
    	setFocusable(true);
    	requestFocusInWindow();
    	addKeyListener(kl);
    	
    	frame.setStart(false);
    	
    	long currentTime = System.currentTimeMillis();
    	long startTime = System.currentTimeMillis();
    	//plays until game is over
    	while(!end.endGame)
    	{
    		//listener detecting enter key being pressed
    		if(kl.enter)
    		{
    			//ensures this is the first time [enter] was pressed,
    			//so next rectangle wont start until enter is released
    			if(first)
    			{
    				first = false;
    				System.out.println(xpos);
    				layer+=1;
    				currentTime = System.currentTimeMillis();
    				//1 second pause
    				while(System.currentTimeMillis()-currentTime < 1000);
    				//if layer == 14, it has reached the top
        			if(layer != 14)
        			{
            			JLabel lbl = new JLabel(new ImageIcon(this.getClass().getResource(image)));
            			lbl.setOpaque(true);
            			//trims rectangle as necessary
            			if(xpos < leftBound)
            			{
            				maxWidth = maxWidth - (leftBound-xpos);
            				xpos = leftBound;
            			}
            			else if(xpos > leftBound)
            			{
            				maxWidth = maxWidth - (xpos-leftBound);
            				leftBound = xpos;
            			}
            			//if rectangle misses stack, game ends.
            			if(maxWidth <= 0)
            			{
            				System.out.println("YOU LOSE");
            				currentTime = System.currentTimeMillis();
            				while(System.currentTimeMillis()-currentTime < 1000);
            				frame.returnToMainMenu();
            				
            				//ends game
            				break;
            			}
            			//update game state
            			lbl.setBounds(xpos, (600-layer*40), maxWidth, 40);
            			lbl.setBackground(Color.BLACK);
            			remove(temp);
            			rect.remove(rect.size()-1);
            			
            			add(lbl);
            			rect.add(lbl);
            			validate();
            			repaint();
            			//adds next rectangle
            			System.out.println(xpos + " " + maxWidth + " " + layer);
            			currentTime = System.currentTimeMillis();
        				while(System.currentTimeMillis()-currentTime < 1000);
        				add(temp);
        				rect.add(temp);
        				validate();
            			repaint();
        			}
        			else
        			{
        				remove(temp);
        				rect.remove(rect.size()-1);
        				JLabel lbl = new JLabel(new ImageIcon(this.getClass().getResource(image)));
            			lbl.setOpaque(true);
            			//trims rectangle as necessary
            			if(xpos < leftBound)
            			{
            				maxWidth = maxWidth - (leftBound-xpos);
            				xpos = leftBound;
            			}
            			else if(xpos > leftBound)
            			{
            				maxWidth = maxWidth - (xpos-leftBound);
            				leftBound = xpos;
            			}
            			//handles case where you missed the last rectangle
            			if(maxWidth <= 0)
            			{
            				System.out.println("YOU LOSE");
            				currentTime = System.currentTimeMillis();
            				while(System.currentTimeMillis()-currentTime < 1000);
            				frame.returnToMainMenu();
            				
            				break;
            			}
            			//updates game state
            			lbl.setBounds(xpos, (600-layer*40), maxWidth, 40);
            			lbl.setBackground(Color.BLACK);
            			
            			add(lbl);
            			rect.add(lbl);
            			validate();
            			repaint();
            			//if you've reached this point, you win
            			layer+=1;
        				System.out.println("YOU WIN");
        				currentTime = System.currentTimeMillis();
        				while(System.currentTimeMillis()-currentTime < 1000);
        				frame.returnToMainMenu();
        				
        				break;
        			}
        			
    			}
    			startTime = System.currentTimeMillis();
    			xpos = 1000-maxWidth;
    		}
    		else
    		{
    			//if enter not pressed, reset first
    			first = true;
    			//update moving rectangle
    			if(goingRight)
    			{
    				xpos += (int)((System.currentTimeMillis()-startTime))/2;
    				if((int)((System.currentTimeMillis()-startTime))/2 > 0)
    				{
    					startTime = System.currentTimeMillis();
    				}
    				if(xpos > 1000-maxWidth)
    				{
    					xpos = 2000-xpos-(2*maxWidth);
    					goingRight = false;
    				}
    			}
    			else
    			{
    				xpos -= (int)((System.currentTimeMillis()-startTime))/2;
    				if((int)((System.currentTimeMillis()-startTime))/2 > 0)
    				{
    					startTime = System.currentTimeMillis();
    				}
    				if(xpos < 0)
    				{
    					xpos*=-1;
    					goingRight = true;
    				}
    			}
    			temp.setBounds(xpos, (600-(layer+1)*40), maxWidth, 40);
    			validate();
    			repaint();
    		}
    		//needed to ensure listener detection
    		System.out.print("");
    		System.out.print("");
    		
    		
    	}
    	//update game state as necessary
    	frame.addMoney((layer-1)*2);
    	removeRectangles();
    }
    /**
     * Updates graphics to remove rectangles.
     */
    public void removeRectangles()
    {
    	for(int i = rect.size()-1; i >= 0; i--)
		{
			remove(rect.get(i));
			rect.remove(i);
		}
    	validate();
    	repaint();
    }
    /**
     * 
     * @param img
     * 		The new image of the rectangle
     * @param fadedImg
     * 		The new faded image of the rectangle
     */
    public void setImages(String img, String fadedImg)
    {
    	image = img;
    	fadedImage = fadedImg;
    }

    

    
}
