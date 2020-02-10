# Wumpus_World_AI
The Wumpus World is a cave consisting of rooms connected by passageways.
   Lurking somewhere in the cave is the terrible Wumpus, a beast that eats
   anyone who enters its room. The Wumpus can be shot by an agent, but the
   agent has only one arrow. Some rooms contain bottomless pits that will
   trap anyone who wanders into these rooms (except for the Wumpus, which
   is too big to fall in). The goal of the agent is finding gold and safely exiting the environment.
   



## MyAi.java

I call this Agent the “Dora the clumsy Explorer”. Essentially I implemented a “depth first search” algorithm by prioritizing my agent to explore the right tile/cave whenever possible. The agent determines whether it can move right by checking if the neighbor tile is safe or unexplored. The agent keeps track of its environment by using its world percepts and location to mark tiles as visited, safe, or as pits. 

If the agent hits a wall, or its current path becomes too dangerous; then it backtracks to a safe tile with unexplored neighbors until it has explored every path or it finds the gold. If the agent ever feels it is too dangerous to explore any further then it simply exits the cave because it value it’s life over gold. Although the agent recognizes where it’s been it does not keep track of how it arrived to its position, so it stumbles around until it finds its way out of the cave. This causes the agent to explore the cave even if it has already grabbed the gold. Due to the fact that my my agent prioritizes life, it does not take any risks, so it may fail to find the gold. This agent does not efficiently distinguish between a pit and a wumpus so it may falsely assume the danger is nearby is a pit and never attempts to kill the wumpus thus leaving some potential paths unexplored.

### Ai troubles 
The agent ran into an issue of backtracking and then revisiting the same path repeatedly. I solved this loop by marking the tiles as visited. Early in the development I found my agent getting caught in a loop when it hit a wall; this was remedied by doing a boundary check before making a move. Since the world size is unknown my algorithm needed a way to determine the size so it assumed the size and updated the world boundaries after it bumps a wall ensuring it never bumps a wall twice.
