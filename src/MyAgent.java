import controllers.pacman.PacManControllerBase;
import game.core.Game;

import java.util.*;

public final class MyAgent extends PacManControllerBase {
    private static class State {
        int subTreeDir;
        Game game;
        Integer hCost;
        Integer rCost;

        public State(int subTreeDir, Game game, Integer hCost, Integer rCost) {
            this.subTreeDir = subTreeDir;
            this.game = game;
            this.hCost = hCost;
            this.rCost = rCost;
        }
    }

    private static class StateComparator implements Comparator<State> {

        @Override
        public int compare(State o1, State o2) {
            int diff = o2.rCost - o1.rCost;
            if (diff == 0){
                return o1.hCost - o2.hCost;
            }
            return diff;
        }
    }

    @Override
    public void tick(Game game, long timeDue) {

        StateComparator comparator = new StateComparator();
        PriorityQueue<State> fringe = new PriorityQueue<>(comparator);

        int[] directions = game.getPossiblePacManDirs(false);
        for (int dir : directions) {
            State next = new State(dir, game.copy(), 0, 0);
            next.game.advanceGame(dir);
            next.hCost = Heuristic(next.game);
            next.rCost = EvaluateState(game, next.game);
            fringe.add(next);
        }

        while (!fringe.isEmpty() && System.currentTimeMillis() < timeDue - 10) {
            State current = fringe.poll();
            assert current != null;
            if (current.game.gameOver() && (current.game.getLivesRemaining() > 0 || current.game.getNumActivePills() <= 0)){
                pacman.set(current.subTreeDir);
                return;
            }
            for (int dir : current.game.getPossiblePacManDirs(false)) {
                State next = new State(current.subTreeDir, current.game.copy(), current.hCost, 0);
                next.game.advanceGame(dir);
                next.hCost = current.hCost + Heuristic(next.game);
                next.rCost = EvaluateState(current.game, next.game);
                fringe.add(next);
            }
        }
        assert fringe.peek() != null;
        pacman.set(fringe.peek().subTreeDir);
    }

    private int EvaluateState(Game prev, Game curr){
        int realCost = curr.getScore();

        if (prev.getNumActivePills() - curr.getNumActivePills() > 0) realCost += 100;
        if (prev.getCurLevel() - curr.getCurLevel() < 0) realCost += 3200;
        if (prev.getLivesRemaining() - curr.getLivesRemaining() > 0) {
            realCost -= 100000;
        } else if (curr.getLivesRemaining() - prev.getLivesRemaining() > 0){
            realCost += 1000;
        }
        return realCost;
    }

    private int Heuristic(Game stateToEval) {
        //TODO: implement heuristic function
        /*int realCost = Integer.MAX_VALUE;

        // Find the closest ghost
        for (int ghost = 0; ghost < 4; ghost++){
            int ghostDist = stateToEval.game.getManhattanDistance(stateToEval.game.getCurPacManLoc(), stateToEval.game.getCurGhostLoc(ghost));
            if (ghostDist < realCost){
                realCost = ghostDist;
            }
        }*/

        //return stateToEval.getDistanceToNearestPill();

        try {
            return stateToEval.getDistanceToNearestPill();
        } catch (Exception e) {
            return -1;
        }
    }
}
