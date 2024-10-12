import controllers.pacman.PacManControllerBase;
import game.core.Game;

import java.util.*;

public final class MyAgent extends PacManControllerBase {
    private static class State {
        int dir;
        Game game;
        Integer cost;

        public State(int prev, Game game, Integer cost) {
            this.dir = prev; // TODO: Track the first direction
            this.game = game;
            this.cost = cost;
        }
    }

    @Override
    public void tick(Game game, long timeDue) {

        Queue<State> fringe = new LinkedList<>();
        State state = new State(-1, game, 0);

        fringe.add(state);
        HashMap<State, Integer> visitedCosts = new HashMap<>() {
        };

        int[] directions = game.getPossiblePacManDirs(false);
        if (directions.length == 1) {
            pacman.set(directions[0]);
            return;
        }

        while (!fringe.isEmpty() && System.currentTimeMillis() < timeDue - 10) {
            State current = fringe.poll();
            // TODO: if goal
            for (int dir : current.game.getPossiblePacManDirs(false)) {
                State next = new State(dir, current.game.copy(), current.cost);
                next.game.advanceGame(dir);
                next.cost = current.cost + EvaluateState(next);
                if (visitedCosts.get(next) != null) {
                    if (next.cost >= visitedCosts.get(next)){
                        continue;
                    }
                }
                visitedCosts.put(next, next.cost);
                fringe.add(next);
            }

        }
        if (System.currentTimeMillis() >= timeDue - 10) {
            // TODO: pacman.set best dir
        } else {
            pacman.set(directions[game.rand().nextInt(directions.length)]);
        }
    }

    private int EvaluateState(State stateToEval) {
        //TODO: implement heuristic function
        return stateToEval.cost;
    }
}
