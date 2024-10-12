import controllers.pacman.PacManControllerBase;
import game.core.Game;

import java.util.*;

public final class MyAgent extends PacManControllerBase {
    private static class State {
        int dir;
        State prev;
        Game game;
        Integer cost;

        public State(int dir, State prev, Game game, Integer cost) {
            this.dir = dir; // TODO: Track the first direction
            this.game = game;
            this.cost = cost;
        }
    }

    @Override
    public void tick(Game game, long timeDue) {

        Queue<State> fringe = new LinkedList<>();
        State state = new State(-1, null, game, 0);

        fringe.add(state);
        HashMap<State, Integer> visitedCosts = new HashMap<>() {
        };

        int[] directions = game.getPossiblePacManDirs(false);
        if (directions.length == 1) {
            pacman.set(directions[0]);
            return;
        }

        while (!fringe.isEmpty() && System.currentTimeMillis() < timeDue - 20) {
            State current = fringe.poll();
            assert current != null;
            if (current.game.gameOver()) {
                pacman.set(GetBestDir(current));
                return;
            }
            for (int dir : current.game.getPossiblePacManDirs(true)) {
                State next = new State(dir, current, current.game.copy(), current.cost);
                next.game.advanceGame(dir);
                next.cost = next.game.getScore(); //current.cost + EvaluateState(next.game);
                if (visitedCosts.get(next) != null) {
                    if (next.cost <= visitedCosts.get(next)){ //TODO: score based... higher cost the better.
                        continue;
                    }
                }
                visitedCosts.put(next, next.cost);
                fringe.add(next);
            }

        }
        if (System.currentTimeMillis() >= timeDue - 20) {
            // TODO: pacman.set best dir
            int bestCost = -1;
            State bestState = null;
            for (State st : fringe){
                if (bestCost < st.cost){
                    bestCost = st.cost;
                    bestState = st;
                }
            }
            assert bestState != null;
            pacman.set(GetBestDir(bestState));

        } else {
            pacman.set(directions[game.rand().nextInt(directions.length)]);
        }
    }

    private Integer EvaluateState(Game stateToEval) {
        //TODO: implement heuristic function
        Integer stateCost = 0;
        //if (stateToEval.gameOver() && stateToEval.getLivesRemaining() <= 0) stateCost = 1000;
        return stateCost;
    }

    private int GetBestDir(State bestState){
        int bestDir = bestState.dir;
        while (bestState.prev != null){
            bestDir = bestState.dir;
            bestState = bestState.prev;
        }
        return bestDir;
    }
}
