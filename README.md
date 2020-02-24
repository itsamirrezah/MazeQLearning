# Maze Q-Learning

### Introduction
Imagine we have an agent which only knows how to walk and where to go, but he doesn't know how to reach there. How can we teach him to find an optimal path to his destination ?
In this repository I've been using Q-Learning algorithm to solve this problem.
Q-Learning is a reinforcement learning technique (in simple words: learning by mistakes). The goal of Q-learning is to learn a policy, which tells the agent what action should take under what circumstances

### Problem
Imagine our agent is in a 2D envirement like below

![Maze-Problem](/assets/maze-problem.jpg)

Each blocks represent different state
* Red blocks represents obstacle which agent can't walk throgh it.
* Green block represents final state, the goal of agent is to find an optimal path to reach this state. 

Each state have its own reward value which is negative for obstacle states and positive for none-obstacle states.
At each block our agent can move with four actions: 
* Go left
* Go right
* Go up
* Go down

After each action he takes, it will takes his reward from envirement.
At first our agent randomly chooses an action to move to different states, but eventually he will learn by his mistakes and will find the optimal path to reach the final state.

### Implementation

* Set an starting value for Reward-Table:

Reward Table is a n*n table that contains reward value of each action at each state.
f.e: if we have an envirement that contains 9 states (like above), then we must have a 9*9 reward table that each value in it represents the reward of moving from one state to another. for instance reward[0][3] represents the reward value of moving from state 0 to state 3

```java

private void initRewards() {
        [k,r]
        for (int i = 0; i < rewards.length; i++) {
            int r1 = i % width;
            int k1 = i / width;

            for (int j = 0; j < rewards.length; j++) {
                int r2 = j % width;
                int k2 = j / width;
                
                //if reward[k1,r1] && reward[k2,r2] aren't neighbor
                //then set their reward value to -1
                if (!((r1 == r2 && Math.abs(k1 - k2) == 1) || (k1 == k2 && Math.abs(r1 - r2) == 1)))
                    rewards[i][j] = -1;
            }
        }
    }  
```

* Select a none-obstacle state as current state 
* Get current state's neighbors
* Select one of the neighbor as next state
* Update Q-Table with the Q-Function

Q-Table is a n*n table that used to calculate the maximum expected future rewards for action at each state. Basically, this table will guide our agent to the best action at each state. 

Q-Function: Q-Learning algorithm uses the Bellman equation to update the Q value

```java
private void QLearning() {
        List<Integer> possibleActions;
        Random rand = new Random();
        for (int i = 0; i < TRAIN_NUMBER; i++) {
            int currentState = randomState();
            //get all possible action for current state;
            while (!(possibleActions = getNeighbors(currentState)).isEmpty()) {
                //determine next state
                int idxNextState = rand.nextInt(possibleActions.size());
                int nextState = possibleActions.get(idxNextState);

                //calculate Q
                double r = getReward(currentState, nextState);
                double maxQ = maxQ(nextState);
                double q = Q[currentState][nextState];
                //q-function
                //q: q value of the current state
                //r: reward of the current state
                //maxQ: represents the max q value for each state on performing any action 
                //alpha: learning rate
                //gamma: discounted rate
                double value = q + ALPHA * (r + GAMMA * maxQ - q);
                Q[currentState][nextState] = value;

                //change current State
                currentState = nextState;
            }
        }
    }
```
