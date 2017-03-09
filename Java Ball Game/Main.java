import java.awt.*;
import java.util.*;
import java.applet.*;
import java.net.*;
import java.awt.event.MouseEvent;
import javax.swing.event.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.ArrayList;
/*<applet code="Main" height=400 width=400></applet>*/


public class Main extends Applet implements Runnable
{

/* Configuration arguments. These should be initialized with the values read from the config.xml file*/					
    public ArrayList <Ball> ballArray;
/*end of config arguments*/

    private int refreshrate = 15;	           //Refresh rate for the applet screen. Do not change this value. 
	private boolean isStoped = true;		     
    Font f = new Font ("Arial", Font.BOLD, 18);
	
	private Player player;			           //Player instance.		
	public int num_clicks;
	public int num_hits;
	
	public String most_hit;

	Thread th;						           //The applet thread. 

	AudioClip shotnoise;	
	AudioClip hitnoise;		
	AudioClip outnoise;		
	  
    Cursor c;			
    
    private GameWindow gwindow ;                 // Defines the borders of the applet screen. A ball is considered "out" when it moves out of these borders.
	private Image dbImage;
	private Graphics dbg;

	
	class HandleMouse extends MouseInputAdapter 
	{
		
    	public HandleMouse() 
    	{
            addMouseListener(this);
        }
		
    	public void mouseClicked(MouseEvent e) 
    	{
    		num_clicks++;
        	if (!isStoped) {
				for(int i = 0; i<ballArray.size();i++){
					if(ballArray.get(i).userHit (e.getX(), e.getY())){
						hitnoise.play();
						ballArray.get(i).ballWasHit();
						num_hits++;
					}
				
					else {
						shotnoise.play();
					}
				}
			}
			else if (isStoped && e.getClickCount() == 2) {
				isStoped = false;
				init ();
			}
    	}

    	public void mouseReleased(MouseEvent e) 
    	{
           
    	}
        
    	public void RegisterHandler() 
    	{

    	}
    }
	
    /*initialize the game*/
	public void init ()
	{	
		int new_numLives = 0;
		int newX_leftout = 0;
		int newX_rightout = 0;
		int newY_upout = 0;
		int newY_downout = 0;
		int life_score = 0;
		int num_balls = 0;
		try{
			File inputFile = new File("/Users/kyle.bush/Desktop/HW6/config2.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
	
			//parts to initialize game window
			newX_leftout = Integer.parseInt(doc.getElementsByTagName("x_leftout").item(0).getTextContent());
			newX_rightout = Integer.parseInt(doc.getElementsByTagName("x_rightout").item(0).getTextContent());	  
			newY_upout = Integer.parseInt(doc.getElementsByTagName("y_upout").item(0).getTextContent());
			newY_downout = Integer.parseInt(doc.getElementsByTagName("y_downout").item(0).getTextContent());
			new_numLives = Integer.parseInt(doc.getElementsByTagName("numLives").item(0).getTextContent());
			
			/* The parameters for the GameWindow constructor (x_leftout, x_rightout, y_upout, y_downout) 
			should be initialized with the values read from the config.xml file*/	
			gwindow = new GameWindow(newX_leftout,newX_rightout,newY_upout,newY_downout);
			
			this.setSize(gwindow.x_rightout+30, gwindow.y_downout+30); //set the size of the applet window.
			
			//misc game info
			life_score = Integer.parseInt(doc.getElementsByTagName("score2EarnLife").item(0).getTextContent());
			num_balls = Integer.parseInt(doc.getElementsByTagName("numBalls").item(0).getTextContent());
			
			//initialize player and ball array along with noises
			player = new Player (new_numLives);
			ballArray = new ArrayList<Ball>(num_balls);
			hitnoise = getAudioClip (getCodeBase() , "gun.au");
			hitnoise.play();
			hitnoise.stop();
			shotnoise = getAudioClip (getCodeBase() , "miss.au");
			shotnoise.play();
			shotnoise.stop();
			outnoise = getAudioClip (getCodeBase() , "error.au");
			outnoise.play();
			outnoise.stop();
			
			//ball information
			String balltype = "";
			int new_radius = 0;
			int new_X = 0;
			int new_Y = 0;
			int newX_speed = 0;
			int newY_speed = 0;
			int newMaxSpeed = 0;
			String color = "";
			int bounces = 0;
			Color new_color = Color.red;
	
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Ball");
	
			for(int temp = 0; temp<nList.getLength();temp++){
				Node n = nList.item(temp);
				if(n.getNodeType()==Node.ELEMENT_NODE){
					Element eElement = (Element) n;
					balltype = eElement.getElementsByTagName("type").item(0).getTextContent();
					new_radius = Integer.parseInt(eElement.getElementsByTagName("radius").item(0).getTextContent());
					new_X = Integer.parseInt(eElement.getElementsByTagName("initXpos").item(0).getTextContent());
					new_Y = Integer.parseInt(eElement.getElementsByTagName("initYpos").item(0).getTextContent());
					newX_speed = Integer.parseInt(eElement.getElementsByTagName("speedX").item(0).getTextContent());
					newY_speed = Integer.parseInt(eElement.getElementsByTagName("speedY").item(0).getTextContent());
					newMaxSpeed = Integer.parseInt(eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent());
					color = eElement.getElementsByTagName("color").item(0).getTextContent();
					
					//for bounce ball
					if(balltype.equals("bounceball")){
						bounces= Integer.parseInt(eElement.getElementsByTagName("bounceCount").item(0).getTextContent());
						if(color.equals("red")){
							new_color = Color.red;
						}
						else if(color.equals("blue")){
							new_color = Color.blue;
						}
						else new_color = Color.green;
					
						BounceBall b = new BounceBall(new_radius,new_X,new_Y,newX_speed,newY_speed,newMaxSpeed,new_color,outnoise,player,gwindow,bounces,life_score);
						ballArray.add(b);
					}
					
					//for shrinkball
					if(balltype.equals("shrinkball")){
						if(color.equals("red")){
							new_color = Color.red;
						}
						else if(color.equals("blue")){
							new_color = Color.blue;
						}
						else new_color = Color.green;
						
						ShrinkBall s = new ShrinkBall(new_radius,new_X,new_Y,newX_speed,newY_speed,newMaxSpeed,new_color,outnoise,player,gwindow,life_score);
						ballArray.add(s);
					}
					//end shrinkball
					//begin basic ball
					if(balltype.equals("basicball")){
						if(color.equals("red")){
							new_color = Color.red;
						}
						else if(color.equals("blue")){
							new_color = Color.blue;
						}
						else new_color = Color.green;
						
						Ball basic = new Ball(new_radius,new_X,new_Y,newX_speed,newY_speed,newMaxSpeed,new_color,outnoise,player,gwindow,life_score);
						ballArray.add(basic);
					}
				
					//end basic ball
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		c = new Cursor (Cursor.CROSSHAIR_CURSOR);
		this.setCursor (c);
		HandleMouse hm = new HandleMouse();	
				
        Color superblue = new Color (0, 0, 255);  
		setBackground (Color.black);
		setFont (f);

		if (getParameter ("refreshrate") != null) {
			refreshrate = Integer.parseInt(getParameter("refreshrate"));
		}
		else refreshrate = 15;
		

	}
	
	/*start the applet thread and start animating*/
	public void start ()
	{		
		if (th==null){
			th = new Thread (this);
		}
		th.start ();
	}
	
	/*stop the thread*/
	public void stop ()
	{
		th=null;
	}

    
	public void run ()
	{	
		/*Lower this thread's priority so it won't infere with other processing going on*/
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        /*This is the animation loop. It continues until the user stops or closes the applet*/
		while (true) {
			if (!isStoped) {
				for(int j = 0; j< ballArray.size(); j++){
					ballArray.get(j).move();
					
				}
			}
            /*Display it*/
			repaint();
            
			try {
				
				Thread.sleep (refreshrate);
			}
			catch (InterruptedException ex) {
				
			}			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	
	public void paint (Graphics g)
	{
		/*if the game is still active draw the ball and display the player's score. If the game is active but stopped, ask player to double click to start the game*/ 
		if (!player.isGameOver()) {
			g.setColor (Color.yellow);
			
			g.drawString ("Score: " + player.getScore(), 10, 40);
			g.drawString("Lives: " + player.getLives(),10,60);
			for(int j = 0; j< ballArray.size(); j++){
				ballArray.get(j).DrawBall(g);
			}
			
			if (isStoped) {
				g.setColor (Color.yellow);
				g.drawString ("Doubleclick on Applet to start Game!", 40, 200);
			}
		}
		/*if the game is over (i.e., the ball is out) display player's score*/
		else {
			g.setColor (Color.yellow);

			
			g.drawString ("Game over!", 130, 100);
			g.drawString ("You scored " + player.getScore() + " Points!", 90, 140);

			
			if (player.getScore() < 300) g.drawString ("Well, it could be better!", 100, 190);
			else if (player.getScore() < 600 && player.getScore() >= 300) g.drawString ("That was not so bad", 100, 190);
			else if (player.getScore() < 900 && player.getScore() >= 600) g.drawString ("That was really good", 100, 190);
			else if (player.getScore() < 1200 && player.getScore() >= 900) g.drawString ("You seem to be very good!", 90, 190);
			else if (player.getScore() < 1500 && player.getScore() >= 1200) g.drawString ("That was nearly perfect!", 90, 190);
			else if (player.getScore() >= 1500) g.drawString ("You are the Champion!",100, 190);

			g.drawString ("Doubleclick on the Applet, to play again!", 50, 220);
			g.drawString("***STATS***", 140, 260);
			g.drawString("Total Clicks: "+num_clicks, 120, 280);
			g.drawString("Total Hits: "+num_hits+"("+Math.floor(((double)num_hits/num_clicks)*100)+"% Accuracy)", 120, 300);
			g.drawString("Total Misses: " +(num_clicks-num_hits), 120, 320);

			isStoped = true;	
		}
	}

	
	public void update (Graphics g)
	{
		
		if (dbImage == null)
		{
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		
		dbg.setColor (getForeground());
		paint (dbg);

		
		g.drawImage (dbImage, 0, 0, this);
	}
}