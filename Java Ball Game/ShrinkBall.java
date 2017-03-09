import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class ShrinkBall extends Ball
{
	public int shrink_times;
	public int initialRadius;
	public ShrinkBall (int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW,int points)
	{
		super(radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, outSound, player,   gameW,points);
		initialRadius = radius;
		shrink_times = 0;
		
	}
	
	//this class adds one method. shrinkBall() shrinks the size of the ball every time it's hit
	//also overwrites userHit method
	public boolean userHit(int maus_x, int maus_y)
	{
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;

		double distance = Math.sqrt ((x*x) + (y*y));
		
		if (distance-this.radius < (int)(player.scoreConstant)) {
			if(shrink_times == 0||shrink_times == 1){
				player.addScore (1*(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant));
			}
			else if(shrink_times == 2)
			{
				player.addScore (2*(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant));
			}
			else
			{
				player.addScore (4*(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant));
			
			}
			shrinkBall();
			int new_x = -2 + (int)(Math.random() * 4  +1);
			int new_y = -2 + (int)(Math.random() * 4 +1);
			if(new_x == 0){new_x+=1;}
			if(new_y == 0){new_y-=1;}
			return true;
		}
		else
		{
			return false;
		}
	}
	public void shrinkBall()
	{
		int newRadius = (int)(radius*(70.0f/100.0f));
		
		
		if(shrink_times>=3)
		{
			radius =  initialRadius;
			shrink_times = 0;
		}
		else
		{
			shrink_times +=1;
			radius = newRadius;
		}
		
		
		
	}

	
	
}