package itsamirrezah.qlearning.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import itsamirrezah.qlearning.R;
import itsamirrezah.qlearning.maze.Maze;
import itsamirrezah.qlearning.models.Cell;
import itsamirrezah.qlearning.models.Cells;

public class AppActivity extends AppCompatActivity implements Mvp.View {

    public static final int width = 10;
    RecyclerView recyclerView;
    Button btnStart, btnClear;
    rvMazeAdapter rvAdapter;
    Mvp.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        bindViews();
        setupRecyclerView();
        presenter = new Presenter(this, new Maze(width));

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartClick();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClearClick();
            }
        });

    }

    private void setupRecyclerView() {
        rvAdapter = new rvMazeAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), width));
        recyclerView.setAdapter(rvAdapter);
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recyclerview_maze);
        btnStart = findViewById(R.id.btnStartGame);
        btnClear = findViewById(R.id.btnClearMaze);
    }

    @Override
    public void showPolicy(List<Cell> policies) {
        rvAdapter.showPolicy(policies);
    }


    @Override
    public void setCellStatus(Cell cellStatus) {
        rvAdapter.setCellStatus(cellStatus);
    }

    @Override
    public void showGoalNotFoundError() {
        Toast.makeText(this, "Long press on any cell to mark goal state", Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetRecyclerView(List<Cell> mazeList) {
        rvAdapter.resetRecyclerView(mazeList);
    }

    @Override
    public void isStartClickable(boolean isClickable) {
        btnStart.setClickable(isClickable);
    }

    public class rvMazeAdapter extends RecyclerView.Adapter<rvMazeAdapter.ViewHolder> {

        private List<Cell> cells;
        private boolean isCellClickable;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.maze_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Cell block = cells.get(position);
            if (block.getStatus() == Cells.FREE_CELL) {
                if (block.getPolicy() != 0) {
                    holder.btnMazeBlock.setText("");
                    holder.btnMazeBlock.setTextColor(Color.BLACK);
                    holder.btnMazeBlock.setBackgroundResource(block.getArrow());
                } else {
                    holder.btnMazeBlock.setText(position + "");
                    holder.btnMazeBlock.setBackgroundColor(Color.WHITE);
                }
            } else if (block.getStatus() == Cells.BLOCK_CELL) {
                holder.btnMazeBlock.setBackgroundColor(Color.BLACK);
                holder.btnMazeBlock.setText("");
            } else if (block.getStatus() == Cells.GOAL_CELL) {
                holder.btnMazeBlock.setText("G");
                holder.btnMazeBlock.setBackgroundColor(Color.RED);
                holder.btnMazeBlock.setTextColor(Color.YELLOW);
            }
        }

        @Override
        public int getItemCount() {
            return cells.size();
        }

        public void showPolicy(List<Cell> policies) {
            this.cells = policies;
            isCellClickable =false;
            notifyItemRangeChanged(0, getItemCount());
        }

        public void resetRecyclerView(List<Cell> mazeList) {
            this.cells = mazeList;
            isCellClickable = true;
            notifyItemRangeChanged(0, getItemCount());
        }

        public void setCellStatus(Cell changedBlock) {
            cells.set(changedBlock.getPosition(), changedBlock);
            notifyItemChanged(changedBlock.getPosition());
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            Button btnMazeBlock;

            public ViewHolder(View itemView) {
                super(itemView);
                btnMazeBlock = itemView.findViewById(R.id.btnMazeCell);
                btnMazeBlock.setOnClickListener(this);
                btnMazeBlock.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {

                if (!isCellClickable)
                    return;

                int id = view.getId();
                if (id == R.id.btnMazeCell) {
                    presenter.onCellClick(getAdapterPosition());
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (!isCellClickable)
                    return false;

                int id = view.getId();

                if (id == R.id.btnMazeCell) {
                    presenter.onCellLongClick(getAdapterPosition());
                }
                return true;
            }

        }
    }
}
