import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class BounceBall extends Ball
{
	public int num_bounces;
	public BounceBall(int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW, int bounces, int points)
	{
		super(radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, outSound, player,   gameW, points);
		num_bounces = bounces;
	}
	public boolean isOut ()
	{
		int new_y =0, new_x = 0;
		
		if(num_bounces == 5)
		{
			new_x = -2 + (int)(Math.random() * 4  +1);
			new_y = -2 + (int)(Math.random() * 4 +1);
			if(new_x == 0){new_x+=1;}
			if(new_y == 0){new_y-=1;}
			resetBallPosition();
			num_bounces = 0;
			return true;
		}
		else if ((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout) || (pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) 
		{
			if (pos_x < gameW.x_leftout&&pos_y >gameW.y_downout) 
			{
				new_x = -x_speed;
				x_speed = new_x;
				new_y = -y_speed;
				y_speed = new_y;
				
	
			
			}
			else if(pos_x > gameW.x_rightout&&pos_y> gameW.y_downout)
			{
				new_x = -x_speed;
				x_speed = new_x;
				new_y = -y_speed;
				y_speed = new_y;
			}
			else if(pos_y < gameW.y_upout&&pos_x<gameW.x_leftout)
			{
				new_y = -y_speed;
				y_speed = new_y;
				new_x = -x_speed;
				x_speed = new_x;
				
			}
			else if (pos_y > gameW.y_downout&&pos_x>gameW.x_rightout) 
			{
				new_y = -y_speed;
				y_speed = new_y;
				new_x = -x_speed;
				x_speed = new_x;
			}
			else if(pos_y > gameW.y_downout)
			{	
				new_y = -y_speed;
				y_speed = new_y;
			}
			else if(pos_y <gameW.y_upout)
			{
				new_y = -y_speed;
				y_speed = new_y;
			}
			else if(pos_x>gameW.x_rightout)
			{
				new_x = -x_speed;
				x_speed = new_x;
			}
			else if(pos_x<gameW.x_leftout)
			{
				new_x = -x_speed;
				x_speed = new_x;
			}
			num_bounces += 1;
			outSound.play();
			return false;
		}
		else
		{
			return false;
		}
			
	
	}
	public void ballWasHit ()
	{	
		int new_x = -2 + (int)(Math.random() * 4  +1);
		int new_y = -2 + (int)(Math.random() * 4 +1);
		if(new_x == 0){new_x+=1;}
		if(new_y == 0){new_y-=1;}	
		resetBallPosition();
	}

}