package itsamirrezah.qlearning.maze;

import java.util.List;

import itsamirrezah.qlearning.models.Cell;
import itsamirrezah.qlearning.models.Cells;

/**
 * Created by AmirR on 1/24/2018.
 */

public class Maze {
    private int goalPosition = -1;
    private int size;
    private int width;
    private QLearning qLearning;
    private Cell[] _maze;


    public Maze(int mazeWidth) {
        this.width = mazeWidth;
        this.size = mazeWidth * mazeWidth;
        this.qLearning = new QLearning(mazeWidth);
        this._maze = new Cell[size];
        initMaze();
    }

    private boolean isGoalEstablished() {
        return goalPosition >= 0;
    }

    public Cell[] initMaze() {
        goalPosition = -1;
        for (int i = 0; i < size; i++)
            _maze[i] = new Cell(i,Cells.FREE_CELL);
        return _maze;
    }

    public Cell[] getMaze() {
        return _maze;
    }

    public void startQLearning(QLearningResult qLearningListener) {
        if (!isGoalEstablished())
            qLearningListener.goalNotFound();
        else {
            initRewards();
            qLearning.QLearning();
            qLearningListener.onSuccess(getPolicies());
        }
    }

    private void initRewards() {
        setGoalReward();
        setBlockReward();
    }

    private void setGoalReward() {
        qLearning.setGoal(goalPosition);
    }

    private void setBlockReward() {
        for (int i = 0; i < _maze.length; i++) {
            if (_maze[i].getStatus() == Cells.FREE_CELL || _maze[i].getStatus() == Cells.GOAL_CELL)
                continue;
            if (_maze[i].getStatus() == Cells.BLOCK_CELL)
                qLearning.setBlock(i);
        }
    }

    private Cell[] getPolicies() {
        List<Integer> policies = qLearning.getPolicies();

        for (int i = 0; i < policies.size(); i++)
            _maze[i].setPolicy(policies.get(i));

        return _maze;
    }

    public int getWidth() {
        return width;
    }

    private Cells getCellStatus(int position) {
        return _maze[position].getStatus();
    }

    private void setCell(int position, Cells cellStatus) {

        if (_maze[position].getStatus() == Cells.GOAL_CELL)
            goalPosition = -1;

        if (cellStatus == Cells.GOAL_CELL)
            goalPosition = position;

        _maze[position].setStatus(cellStatus);
    }

    public Cell setBlock(int position) {
        Cells cellStatus = getCellStatus(position);

        if (cellStatus == Cells.FREE_CELL)
            setCell(position, Cells.BLOCK_CELL);
        else
            setCell(position, Cells.FREE_CELL);

        return _maze[position];
    }

    public Cell setGoal(int position) {

        if (!isGoalEstablished()) {
            setCell(position, Cells.GOAL_CELL);
        }
        return _maze[position];
    }

    public void onClear(){
        initMaze();
        qLearning.onClear();
    }

}