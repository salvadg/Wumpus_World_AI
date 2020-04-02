
import java.util.Random;

public class RandomAI extends Agent
{
	
	public Action getAction
	(
		boolean stench,
		boolean breeze,
		boolean glitter,
		boolean bump,
		boolean scream
	)
	{
		if ( glitter )
			return Action.GRAB;
		
		return actions [ rand.nextInt ( actions.length ) ];
	}
	
	private final Action[] actions =
	{
		Action.TURN_LEFT,
		Action.TURN_RIGHT,
		Action.FORWARD,
		Action.SHOOT,
		Action.GRAB,
		Action.CLIMB
	};
	
	private Random rand = new Random();
	
}
