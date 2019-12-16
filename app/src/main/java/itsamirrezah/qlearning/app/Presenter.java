package itsamirrezah.qlearning.app;

import java.util.Arrays;
import java.util.List;

import itsamirrezah.qlearning.maze.Maze;
import itsamirrezah.qlearning.models.Arrows;
import itsamirrezah.qlearning.models.Cell;
import itsamirrezah.qlearning.maze.QLearningResult;

/**
 * Created by AmirR on 1/22/2018.
 */

public class Presenter implements Mvp.Presenter {

    private static final String TAG = "Presenter";
    private Mvp.View view;
    private Maze maze;

    public Presenter(Mvp.View view, Maze maze) {
        this.view = view;
        this.maze = maze;
        onClearClick();
    }

    @Override
    public void onCellClick(int position) {
        Cell cell = maze.setBlock(position);
        view.setCellStatus(cell);
    }

    @Override
    public void onCellLongClick(int position) {
        Cell cell = maze.setGoal(position);
        view.setCellStatus(cell);
    }

    @Override
    public void onStartClick() {
        maze.startQLearning(new QLearningResult() {
            @Override
            public void onSuccess(Cell[] maze) {
                view.showPolicy(setArrows(maze));
                view.isStartClickable(false);
            }

            @Override
            public void goalNotFound() {
                view.showGoalNotFoundError();
            }
        });
    }

    private List<Cell> setArrows(Cell[] maze) {
        int policy;
        for (int i = 0; i < maze.length; i++) {
            policy = maze[i].getPolicy();
            maze[i].setArrow(getArrowByPolicy(i, policy));
        }
        return Arrays.asList(maze);
    }

    private int getArrowByPolicy(int source, int destination) {
        int width = maze.getWidth();
        int j1 = source % width;
        int i1 = source / width;
        int j2 = destination % width;
        int i2 = destination / width;

        if (source == -1 || destination == -1)
            return Arrows.TRAP;

        if (i1 == i2 && j1 > j2)
            return Arrows.LEFT;
        else if (i1 == i2 && j1 < j2)
            return Arrows.RIGHT;
        else if (j1 == j2 && i1 > i2)
            return Arrows.UP;
        else if (j1 == j2 && i1 < i2)
            return Arrows.DOWN;
        return Arrows.TRAP;
    }

    @Override
    public void onClearClick() {
        maze.onClear();
        view.resetRecyclerView(Arrays.asList(maze.getMaze()));
        view.isStartClickable(true);
    }
}
