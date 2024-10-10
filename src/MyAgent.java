import controllers.pacman.PacManControllerBase;
import game.core.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class MyAgent extends PacManControllerBase
{	
	@Override
	public void tick(Game game, long timeDue) {

		ArrayList<Game> fringe = new ArrayList<>();
		fringe.add(game);
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
					//calculate cost
					Integer gameCost = 0;
					if (visitedCosts.get(gameCopy) != null){
						//compare cost
						continue;
					}
					visitedCosts.put(gameCopy, gameCost);
					fringe.add(gameCopy);
				}
				pacman.set(directions[game.rand().nextInt(directions.length)]);
				return;
			}
		}
		if (timeDue <= 0){
			// pacman.set best dir
		}
		
	}
}
