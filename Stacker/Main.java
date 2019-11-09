
public class Main {

	public static void main(String[] args) {
		Terminator end = new Terminator();
    	GameView frame = new GameView(end);
    	frame.setVisible(true);
    	while(true)
    	{
    		if(frame.gameStart())
    		{
    			end.setEndGame(false);
    			frame.beginNewGame();
    		}
    		//graphics will not display game without this line.
    		System.out.print("");
    	}
    }

}
