abstract class Agent
{
	
	// Actuators
	public enum Action
	{
		TURN_LEFT,
		TURN_RIGHT,
		FORWARD,
		SHOOT,
		GRAB,
		CLIMB
	}
	
	public abstract Action getAction
	(
		// Sensors
		boolean stench,
		boolean breeze,
		boolean glitter,
		boolean bump,
		boolean scream
	);
	
}
