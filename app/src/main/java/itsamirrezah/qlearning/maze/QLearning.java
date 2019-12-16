package itsamirrezah.qlearning.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AmirR on 1/22/2018.
 */

class QLearning {

    private static final String TAG = "QLearning";
    private static final int TRAIN_NUMBER = 1000;
    private static final double GAMMA = 0.09;
    private static final double ALPHA = 0.01;
    private static final int GOAL_REWARD = 100;
    private static final int PENALTY = -10;
    private int size;
    private double[][] Q;
    private double[][] rewards;
    private int width;
    private List<Integer> policy;

    QLearning(int width) {
        this.width = width;
        this.size = width * width;
        onClear();
    }

    void onClear() {
        policy = new ArrayList<>();
        Q = new double[size][size];
        rewards = new double[size][size];
        initRewards();
    }

    private void initRewards() {
        //[k,r]
        for (int i = 0; i < rewards.length; i++) {
            int r1 = i % width;
            int k1 = i / width;

            for (int j = 0; j < rewards.length; j++) {
                int r2 = j % width;
                int k2 = j / width;

                //define neighbor [k1,r1] va [k2,r2]
                //if !neighbor then set value to -1
                if (!((r1 == r2 && Math.abs(k1 - k2) == 1) || (k1 == k2 && Math.abs(r1 - r2) == 1)))
                    rewards[i][j] = -1;
            }
        }
    }

    private void setReward(int position, int value) {
        //[k,r]
        int r = position % width;
        int k = position / width;

        if (k - 1 >= 0) {
            rewards[(k - 1) * width + r][position] = value;
            rewards[position][(k - 1) * width + r] = -1;
        }
        if (k + 1 < width) {
            rewards[(k + 1) * width + r][position] = value;
            rewards[position][(k + 1) * width + r] = -1;
        }
        if (r - 1 >= 0) {
            rewards[k * width + r - 1][position] = value;
            rewards[position][k * width + r - 1] = -1;
        }
        if (r + 1 < width) {
            rewards[k * width + r + 1][position] = value;
            rewards[position][k * width + r + 1] = -1;
        }
    }

    void setGoal(int goalLocation) {
        setReward(goalLocation, GOAL_REWARD);
    }

    void setBlock(int position) {
        setReward(position, PENALTY);
    }


    private double getReward(int currentState, int nextState) {
        return rewards[currentState][nextState];
    }

    private int randomState() {
        Random rand = new Random();
        int currentState;

        do {
            currentState = rand.nextInt(rewards.length);
        } while (getNeighbors(currentState).isEmpty());


        return currentState;
    }

    void QLearning() {
        List<Integer> possibleActions;

        Random rand = new Random();
        for (int i = 0; i < TRAIN_NUMBER; i++) {
            int currentState = randomState();
            //get all possible action for current state;
            int counter = 0;
            while (!(possibleActions = getNeighbors(currentState)).isEmpty()) {
                //determine next state
                int idxNextState = rand.nextInt(possibleActions.size());
                int nextState = possibleActions.get(idxNextState);

                //calculate Q
                double r = getReward(currentState, nextState);
                double maxQ = maxQ(nextState);
                double q = Q[currentState][nextState];
                double value = q + ALPHA * (r + GAMMA * maxQ - q);
                Q[currentState][nextState] = value;

                //change current State
                currentState = nextState;

                if (++counter > TRAIN_NUMBER)
                    break;
            }
        }
    }

    private List<Integer> getNeighbors(int currentState) {
        List<Integer> allNeighbors = new ArrayList<>();
        int j = currentState % width;
        int i = currentState / width;

        if (i - 1 >= 0) {
            int index = (i - 1) * width + j;
            allNeighbors.add(index);
        }
        if (i + 1 < width) {
            int index = (i + 1) * width + j;
            allNeighbors.add(index);
        }
        if (j - 1 >= 0) {
            int index = i * width + (j - 1);
            allNeighbors.add(index);
        }
        if (j + 1 < width) {
            int index = i * width + (j + 1);
            allNeighbors.add(index);
        }

        List<Integer> possibleNeighbors = new ArrayList<>();
        for (Integer idx : allNeighbors) {
            if (rewards[currentState][idx] >= 0) {
                possibleNeighbors.add(idx);
            }
        }
        return possibleNeighbors;
    }

    private double maxQ(int nextState) {
        List<Integer> possibleActions = getNeighbors(nextState);
        if (possibleActions.isEmpty())
            return 100;

        double maxValue = Integer.MIN_VALUE;

        for (int nextAction : possibleActions) {
            if (Q[nextState][nextAction] > maxValue)
                maxValue = Q[nextState][nextAction];
        }
        return maxValue;
    }

    public List<Integer> getPolicies() {
        for (int i = 0; i < size; i++) {
            double maxValue = Double.MIN_VALUE;
            int destinationIndex = -1;

            for (int j = 0; j < getNeighbors(i).size(); j++) {
                int k = getNeighbors(i).get(j);
                if (Q[i][k] > maxValue) {
                    maxValue = Q[i][k];
                    destinationIndex = k;
                }
            }
            policy.add(destinationIndex);
        }
        return policy;
    }
}
