import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class Ball
{
    /*Properties of the basic ball. These are initialized in the constructor using the values read from the config.xml file*/
	public  int pos_x;			
	public int pos_y; 				
	public int radius;
	public int first_x;			
	public int first_y;					
	public int x_speed;			
	public int y_speed;			
	public int maxspeed;
	public int points2score;
	Color color;
	AudioClip outSound;
	
    GameWindow gameW;
	Player player;
	
	/*constructor*/
	public Ball (int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW,int points )
	{	
		this.radius = radius;
		points2score = points;
		pos_x = initXpos;
		pos_y = initYpos;

		first_x = initXpos;
		first_y = initYpos;

		x_speed = speedX;
		y_speed = speedY;

		maxspeed = maxBallSpeed;

		this.color = color;

		this.outSound = outSound;

		this.player = player;
		this.gameW = gameW;

	}

	/*update ball's location based on it's speed*/
	public void move ()
	{
		pos_x += x_speed;
		pos_y += y_speed;
		isOut();
	}

	/*when the ball is hit, reset the ball location to its initial starting location*/
	public void ballWasHit ()
	{	
		int new_x = -2 + (int)(Math.random() * 4  +1);
		int new_y = -2 + (int)(Math.random() * 4 +1);
		if(new_x == 0){new_x+=1;}
		if(new_y == 0){new_y-=1;}
		x_speed = new_x;
		y_speed = new_y;	
		resetBallPosition();
	}

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */	
	public boolean userHit (int maus_x, int maus_y)
	{
		
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;

		double distance = Math.sqrt ((x*x) + (y*y));
		
		if (distance-this.radius < (int)(player.scoreConstant)) {
			player.addScore (player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
			int tempScore = player.getScore();
			if((tempScore>= points2score))
			{
				player.plusLife();
				player.targetScore += points2score;
			}
		
			
			return true;
		}
		else
		{
			
			return false;
		}
	}

    /*reset the ball position to its initial starting location*/
	public void resetBallPosition()
	{
		
		pos_x = first_x;
		pos_y = first_y;
	}
	
	/*check if the ball is out of the game borders. if so, game is over!*/ 
	public boolean isOut ()
	{
		
		if ((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout) || (pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) {	
			int new_x = -2 + (int)(Math.random() * 4  +1);
			int new_y = -2 + (int)(Math.random() * 4 +1);
			if(new_x == 0){new_x+=1;}
			if(new_y == 0){new_y-=1;}
			x_speed = new_x;
			y_speed = new_y;
			resetBallPosition();	
			outSound.play();
			
			player.loseLife();
			if(player.getLives() == 0)
			{
				player.gameIsOver();
			}
			return true;
		}	
		else return false;
	}

	/*draw ball*/
	public void DrawBall (Graphics g)
	{
		g.setColor (color);
		g.fillOval (pos_x - radius, pos_y - radius, 2 * radius, 2 * radius);
	}

}