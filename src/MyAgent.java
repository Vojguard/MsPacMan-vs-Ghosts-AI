import controllers.pacman.PacManControllerBase;
import game.core.Game;

import java.util.*;

public final class MyAgent extends PacManControllerBase {
    private static class State {
        int subTreeDir;
        Game game;
        Integer cost;

        public State(int subTreeDir, Game game, Integer cost) {
            this.subTreeDir = subTreeDir;
            this.game = game;
            this.cost = cost;
        }
    }

    @Override
    public void tick(Game game, long timeDue) {

        Queue<State> fringe = new LinkedList<>();
        // HashMap<State, Integer> visitedCosts = new HashMap<>() {        };
        State bestState = new State(-1, game, 0);

        int[] directions = game.getPossiblePacManDirs(false);
        for (int dir : directions) {
            State next = new State(dir, game.copy(), 0);
            next.game.advanceGame(dir);
            next.cost = next.game.getScore();
            fringe.add(next);
        }

        while (!fringe.isEmpty() && System.currentTimeMillis() < timeDue - 20) {
            State current = fringe.poll();
            assert current != null;
            for (int dir : current.game.getPossiblePacManDirs(false)) {
                State next = new State(current.subTreeDir, current.game.copy(), current.cost);
                next.game.advanceGame(dir);
                next.cost = next.game.getScore(); //current.cost + EvaluateState(next.game);
                fringe.add(next);

                if (bestState.cost <= next.cost){
                    bestState = next;
                }
            }
        }
        pacman.set(bestState.subTreeDir);
    }


    private Integer EvaluateState(Game stateToEval) {
        //TODO: implement heuristic function
        Integer stateCost = 0;
        //if (stateToEval.gameOver() && stateToEval.getLivesRemaining() <= 0) stateCost = 1000;
        return stateCost;
    }
}
