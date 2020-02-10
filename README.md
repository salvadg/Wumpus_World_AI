# Wumpus_World_AI
The Wumpus World is a cave consisting of rooms connected by passageways.
   Lurking somewhere in the cave is the terrible Wumpus, a beast that eats
   anyone who enters its room. The Wumpus can be shot by an agent, but the
   agent has only one arrow. Some rooms contain bottomless pits that will
   trap anyone who wanders into these rooms (except for the Wumpus, which
   is too big to fall in). The goal of the agent is finding gold and safely exiting the environment.
   



## MyAi.java
Implements DFS as a basic AI that prioritizes staying alive even at the risk of not finding any gold.
The Ai does not know the boundaries of its environment so it relys on the feedback of "bumping" a wall to map out the world
as it explores it.
