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
    public void newGame()
    {
    	//first indicates whether or not it is the first time the computer senses enter being pressed after it sensed that it was not pressed.
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
    	while(!end.endGame)
    	{
    		if(kl.enter)
    		{
    			if(first)
    			{
    				first = false;
    				System.out.println(xpos);
    				layer+=1;
    				currentTime = System.currentTimeMillis();
    				while(System.currentTimeMillis()-currentTime < 1000);
        			if(layer != 14)
        			{
            			JLabel lbl = new JLabel(new ImageIcon(this.getClass().getResource(image)));
            			lbl.setOpaque(true);
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
            			
            			if(maxWidth <= 0)
            			{
            				System.out.println("YOU LOSE");
            				currentTime = System.currentTimeMillis();
            				while(System.currentTimeMillis()-currentTime < 1000);
            				frame.returnToMainMenu();
            				
            				break;
            			}
            			lbl.setBounds(xpos, (600-layer*40), maxWidth, 40);
            			lbl.setBackground(Color.BLACK);
            			remove(temp);
            			rect.remove(rect.size()-1);
            			
            			add(lbl);
            			rect.add(lbl);
            			validate();
            			repaint();
            			
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
            			
            			if(maxWidth <= 0)
            			{
            				System.out.println("YOU LOSE");
            				currentTime = System.currentTimeMillis();
            				while(System.currentTimeMillis()-currentTime < 1000);
            				frame.returnToMainMenu();
            				
            				break;
            			}
            			lbl.setBounds(xpos, (600-layer*40), maxWidth, 40);
            			lbl.setBackground(Color.BLACK);
            			
            			add(lbl);
            			rect.add(lbl);
            			validate();
            			repaint();
            			
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
    			first = true;
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
    		System.out.print("");
    		
    		
    	}
    	frame.addMoney((layer-1)*2);
    	removeRectangles();
    }
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
    public void setImages(String img, String fadedImg)
    {
    	image = img;
    	fadedImage = fadedImg;
    }

    

    
}
