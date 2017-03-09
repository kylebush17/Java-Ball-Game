public class Player
{
	
	private int score;			   //player score
	private boolean gameover=false;	
	public int scoreConstant = 10; //This constant value is used in score calculation. You don't need to change this. 		
	public int playerLives;
	public int targetScore = 100;
	
	public Player(int lives)
	{
		playerLives = lives;
		score = 0; //initialize the score to 0
	}

	/* get player score*/
	public int getScore ()
	{
		return score;
	}
	public void setLives(int lives){
		playerLives = lives;
	}
	public void loseLife()
	{
		playerLives -=1;
	}
	public void plusLife()
	{
		playerLives = playerLives + 1;
	}
	public int getLives()
	{
		return playerLives;
	}
	/*check if the game is over*/
	public boolean isGameOver ()
	{
		return gameover;
	}

	/*update player score*/
	public void addScore (int plus)
	{
		score += plus;
	}

	/*update "game over" status*/
	public void gameIsOver ()
	{
		gameover = true;
	}
}