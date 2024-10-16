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
            return o1.cost - o2.cost;
        }
    }

    @Override
    public void tick(Game game, long timeDue) {

        StateComparator comparator = new StateComparator();
        PriorityQueue<State> fringe = new PriorityQueue<>(comparator);

        int[] directions = game.getPossiblePacManDirs(false);
        for (int dir : directions) {
            State next = new State(dir, game.copy(), 0);
            next.game.advanceGame(dir);
            next.cost = Heuristic(next.game);
            fringe.add(next);
        }

        while (!fringe.isEmpty() && System.currentTimeMillis() < timeDue - 10) {
            State current = fringe.poll();
            assert current != null;
            for (int dir : current.game.getPossiblePacManDirs(false)) {
                State next = new State(current.subTreeDir, current.game.copy(), current.cost);
                next.game.advanceGame(dir);
                next.cost = current.cost + Heuristic(next.game);
                fringe.add(next);
            }
        }
        assert fringe.peek() != null;
        pacman.set(fringe.peek().subTreeDir);
    }


    private int Heuristic(Game stateToEval) {
        //TODO: implement heuristic function
        /*int nearestGhostDist = stateToEval.getScore();

        if (prev.getNumActivePills() - stateToEval.getNumActivePills() > 0) nearestGhostDist += 100;
        if (prev.getCurLevel() - stateToEval.getCurLevel() < 0) nearestGhostDist += 1000;
        if (prev.getLivesRemaining() - stateToEval.getLivesRemaining() > 0) {
            nearestGhostDist -= 100000;
        } else if (stateToEval.getLivesRemaining() - prev.getLivesRemaining() > 0){
            nearestGhostDist += 1000;
        }*/
        int nearestGhostDist = 10000;

        // Find the closest ghost
        for (int ghost = 0; ghost < 4; ghost++){
            int ghostDist = stateToEval.getPathDistance(stateToEval.getCurPacManLoc(), stateToEval.getCurGhostLoc(ghost));
            if (ghostDist < nearestGhostDist){
                nearestGhostDist = ghostDist;
            }
        }

        return stateToEval.getDistanceToNearestPill() + nearestGhostDist;
    }
}
