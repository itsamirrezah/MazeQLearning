package itsamirrezah.qlearning.models;

public class Cell {
    private int position;
    private Cells status;
    private int policy;
    private int arrow;

    public Cell(int position, Cells status) {
        this.position = position;
        this.status = status;
    }

    public void setPolicy(int policy) {
        this.policy = policy;
    }

    public void setArrow(int arrow) {
        this.arrow = arrow;
    }

    public int getArrow() {
        return arrow;
    }

    public void setStatus(Cells status) {
        this.status = status;
    }

    public int getPolicy() {
        return policy;
    }

    public int getPosition() {
        return position;
    }

    public Cells getStatus() {
        return status;
    }
}
