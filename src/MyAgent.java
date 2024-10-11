import controllers.pacman.PacManControllerBase;
import game.core.Game;

import java.util.*;

public final class MyAgent extends PacManControllerBase
{
	private static class State {
		Game game;
		Integer cost;

		public State(Game game, Integer cost) {
			this.game = game;
			this.cost = cost;
		}
	}

	@Override
	public void tick(Game game, long timeDue) {

		Queue<State> fringe = new LinkedList<>();
		State state = new State(game, 0);

		fringe.add(state);
		HashMap<Game, Integer> visitedCosts = new HashMap<Game, Integer>() {};

		// better timeout
		while(!fringe.isEmpty() && timeDue > 0){
			// if goal

			int[] directions = game.getPossiblePacManDirs(false);
			if (directions.length == 1){
				pacman.set(directions[0]);
				return;
			} else {
				for (int dir : game.getPossiblePacManDirs(false)){
					Game gameCopy = game.copy();
					gameCopy.advanceGame(dir);
					State stateCopy = new State(gameCopy, 0);
					//calculate cost
					Integer gameCost = 0;
					if (visitedCosts.get(gameCopy) != null){
						//compare cost
						continue;
					}
					visitedCosts.put(gameCopy, gameCost);
					fringe.add(stateCopy);
				}
				pacman.set(directions[game.rand().nextInt(directions.length)]);
				return;
			}
		}
		if (timeDue <= 0){
			// pacman.set best dir
		}
		
	}

	private int EvaluateState(State stateToEval){
		return stateToEval.cost;
	}
}
