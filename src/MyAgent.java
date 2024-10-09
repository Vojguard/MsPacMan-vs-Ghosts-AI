import controllers.pacman.PacManControllerBase;
import game.core.Game;

public final class MyAgent extends PacManControllerBase
{	
	@Override
	public void tick(Game game, long timeDue) {

		for (int dir : game.getPossiblePacManDirs(false)){
			Game gameCopy = game.copy();
			gameCopy.advanceGame(dir);
		}
		
		// Dummy implementation: move in a random direction.  You won't live long this way,
		int[] directions = game.getPossiblePacManDirs(false);	
		pacman.set(directions[game.rand().nextInt(directions.length)]);
		
	}
}
