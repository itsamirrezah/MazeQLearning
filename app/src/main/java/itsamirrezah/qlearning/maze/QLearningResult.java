package itsamirrezah.qlearning.maze;

import itsamirrezah.qlearning.models.Cell;

public interface QLearningResult {
    void onSuccess(Cell[] maze);
    void goalNotFound();
}
