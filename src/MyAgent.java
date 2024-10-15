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

    private static class StateComparator implements Comparator<State> {

        @Override
        public int compare(State o1, State o2) {
            return o2.cost - o1.cost;
        }
    }

    @Override
    public void tick(Game game, long timeDue) {

        StateComparator comparator = new StateComparator();
        PriorityQueue<State> fringe = new PriorityQueue<State>(comparator);
        // HashMap<State, Integer> visitedCosts = new HashMap<>() {        };
        State bestState = new State(-1, game, 0);

        int[] directions = game.getPossiblePacManDirs(true);
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
                next.cost = EvaluateState(current.game, next.game);
                if (next.cost > 0) fringe.add(next);

                if (bestState.cost <= next.cost){
                    bestState = next;
                }
            }
        }
        pacman.set(bestState.subTreeDir);
    }


    private int EvaluateState(Game prev, Game stateToEval) {
        //TODO: implement heuristic function
        int stateCost = stateToEval.getScore();

        if (prev.getNumActivePills() - stateToEval.getNumActivePills() > 0) stateCost += 100;
        if (prev.getNumActivePowerPills() - stateToEval.getNumActivePowerPills() > 0) stateCost -= 1600;
        if (prev.getCurLevel() - stateToEval.getCurLevel() < 0) stateCost += 1000;
        if (prev.getLivesRemaining() - stateToEval.getLivesRemaining() > 0) {
            stateCost -= 100000;
        } else if (stateToEval.getLivesRemaining() - prev.getLivesRemaining() > 0){
            stateCost += 1000;
        }

        return stateCost;
    }
}
