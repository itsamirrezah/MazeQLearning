package itsamirrezah.qlearning.app;

import java.util.List;

import itsamirrezah.qlearning.models.Cell;

/**
 * Created by AmirR on 1/22/2018.
 */

public interface Mvp {

    interface View {
        void showPolicy(List<Cell> policy);

        void setCellStatus(Cell changedBlock);

        void showGoalNotFoundError();

        void resetRecyclerView(List<Cell> characters);

        void isStartClickable(boolean isActivate);
    }

    interface Presenter {

        void onCellClick(int adapterPosition);

        void onCellLongClick(int adapterPosition);

        void onStartClick();

        void onClearClick();
    }
}
